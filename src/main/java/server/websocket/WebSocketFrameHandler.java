/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 仅处理某种特定的数据类型的handler
 *
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:36
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	private Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);
	
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
			logger.info("玩家退出游戏,移除" + OpenID);
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
				logger.info("响应心跳包 " + request);
				ctx.channel().writeAndFlush(new TextWebSocketFrame("0"));
				return;
			}
			logger.info(getStrDate() + ctx.channel().remoteAddress() + "\t" + request);
			String OpenID = "", otherOpenID = "";
			ChannelHandlerContext oCtx = null;
			int i1 = request.indexOf("&", 1); //第一个"&"的位置；
			String opt = "";
			if (opt.equals("wxLogin")) {
				String userInfo = request.substring(i1 + 1);
				RedisServer.getInstance().setUserInfo(OpenID, userInfo);
				mapUserCtxReverse.put(ctx, OpenID);
				mapUserCtx.put(OpenID, ctx);
			} else if (opt.equals("startgamematch")) {
			
			} else if (opt.equals("lose")) {
				oCtx = mapCtx.get(ctx);
				if (oCtx != null) {
					mapCtx.remove(oCtx);
				}
				mapCtx.remove(ctx);
			} else if (opt.equals("quit")) {
				lUsers.remove(OpenID);
			}
		} else {
			String message = "unsupported frame type: " + frame.getClass().getName();
			throw new UnsupportedOperationException(message);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		logger.warn("Error : " + getStrDate() + ctx.channel().remoteAddress() + "\t" + cause);
		ctx.close();
	}
	
}
