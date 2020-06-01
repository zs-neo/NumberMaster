/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:36
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	static List<String> lUsers = new ArrayList<String>(); //存放所有参与匹配的OpenID
	static HashMap<ChannelHandlerContext, ChannelHandlerContext> mapCtx = new HashMap<ChannelHandlerContext, ChannelHandlerContext>(); //索引为A方，字段为与A对战的B方
	static HashMap<ChannelHandlerContext, String> mapUserCtxReverse = new HashMap<ChannelHandlerContext, String>(); //索引为玩家进程，字段OpenID
	static HashMap<String, ChannelHandlerContext> mapUserCtx = new HashMap<String, ChannelHandlerContext>(); //索引为玩家进程，字段OpenID
	
	//玩家退出游戏处理
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String OpenID = mapUserCtxReverse.remove(ctx);
		if (OpenID != null) {
			mapUserCtx.remove(OpenID);
			lUsers.remove(OpenID);
		}
		mapCtx.remove(ctx);
	}
	
	String getStrDate() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		if (frame instanceof TextWebSocketFrame) {
			// Send the uppercase string back.
			String request = ((TextWebSocketFrame) frame).text();
			// 心跳包
			if (request.equals("0")) {
				ctx.channel().writeAndFlush(new TextWebSocketFrame("0"));
				return;
			}
			System.out.println(getStrDate() + ctx.channel().remoteAddress() + "\t" + request);
			int iColon = request.indexOf(":");
			if (iColon == -1) return;
			String cmd = request.substring(0, iColon);
			String OpenID = "", otherOpenID = "";
//            String sResponse = "200"; //请求成功
			ChannelHandlerContext oCtx = null;
			int i1 = request.indexOf("&", 1); //第一个"&"的位置；
			if (i1 != -1) OpenID = request.substring(iColon + 1, i1);
			if (cmd.equals("wxLogin")) {
				String userInfo = request.substring(i1 + 1);
				RedisServer.getInstance().setUserInfo(OpenID, userInfo);
				mapUserCtxReverse.put(ctx, OpenID);
				mapUserCtx.put(OpenID, ctx);
			} else if (cmd.equals("match")) {
				OpenID = request.substring(iColon + 1);
				if (lUsers.contains(OpenID)) return;
				if (lUsers.size() > 0) {
					while (lUsers.size() > 0) {
						otherOpenID = lUsers.remove(0);
//                            System.out.println(getStrDate()+"\t"+"me="+OpenID+"\t"+"other="+otherOpenID+"\t"+mapUserCtx.size());
						oCtx = mapUserCtx.get(otherOpenID);
						if (oCtx != null) {
							RedisServer redis = RedisServer.getInstance();
							String other = redis.getUserInfo(otherOpenID);
							if (other == null) other = "";
							String me = redis.getUserInfo(OpenID);
							if (me == null) me = "";
//                                System.out.println(getStrDate()+"\t"+"me="+me+"&"+OpenID+"\t"+"other="+other+"&"+otherOpenID);
							ctx.channel().writeAndFlush(new TextWebSocketFrame(cmd + ":" + other));
							oCtx.channel().writeAndFlush(new TextWebSocketFrame(cmd + ":" + me));
							mapCtx.put(ctx, oCtx);
							mapCtx.put(oCtx, ctx);
							break;
						}
					}
				} else lUsers.add(OpenID);
			} else if (cmd.equals("lose")) {
				oCtx = mapCtx.get(ctx);
				if (oCtx != null) {
					oCtx.channel().writeAndFlush(new TextWebSocketFrame(cmd + ":"));
					mapCtx.remove(oCtx);
				}
				mapCtx.remove(ctx);
			} else if (cmd.equals("quit")) {
				OpenID = request.substring(iColon + 1);
				lUsers.remove(OpenID);
			}
		} else {
			String message = "unsupported frame type: " + frame.getClass().getName();
			throw new UnsupportedOperationException(message);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
		System.out.println(getStrDate() + ctx.channel().remoteAddress() + "\t" + cause);
		ctx.close();
	}
	
}
