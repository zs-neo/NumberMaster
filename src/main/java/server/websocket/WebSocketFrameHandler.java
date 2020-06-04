/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static server.websocket.Constant.*;

/**
 * 仅处理某种特定的数据类型的handler
 *
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:36
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	private Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);
	
	/**
	 * 存放所有参与匹配的OpenID
	 */
	static LinkedBlockingQueue<String> matchingUsers = new LinkedBlockingQueue<String>();
	/**
	 * 索引为A方，字段为与A对战的B方
	 */
	static ConcurrentHashMap<ChannelHandlerContext, ChannelHandlerContext> mapCtx = new ConcurrentHashMap<ChannelHandlerContext, ChannelHandlerContext>();
	/**
	 * 索引为玩家进程，字段为对手openId
	 */
	static ConcurrentHashMap<ChannelHandlerContext, String> mapUserCtxReverse = new ConcurrentHashMap<ChannelHandlerContext, String>();
	/**
	 * 索引为openid，字段为玩家进程
	 */
	static ConcurrentHashMap<String, ChannelHandlerContext> mapUserCtx = new ConcurrentHashMap<String, ChannelHandlerContext>();
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ChannelHandlerContext otherCtx = mapCtx.get(ctx);
		if (otherCtx != null) {
			// 说明还在对局中,移除对局，同时给对手发消息，我退出了
			otherCtx.writeAndFlush(new TextWebSocketFrame("error"));
			String openid = mapUserCtxReverse.get(otherCtx);
			String otherOpenId = mapUserCtxReverse.get(ctx);
			mapCtx.remove(ctx);
			mapCtx.remove(otherCtx);
			mapUserCtx.remove(ctx);
			mapUserCtxReverse.remove(otherOpenId);
			//清除要猜的数
			RedisServer db = RedisServer.getInstance();
			db.setUserResult(openid, ((int) (100 * Math.random())) + "");
			db.setUserResult(otherOpenId, ((int) (100 * Math.random())) + "");
			logger.info("玩家异常退出游戏,移除" + openid);
		} else {
			// 无当前玩家的对局
			logger.info("玩家退出游戏");
		}
	}
	
	String getStrDate() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		if (frame instanceof TextWebSocketFrame) {
			RedisServer db = RedisServer.getInstance();
			String request = ((TextWebSocketFrame) frame).text();
			logger.info(getStrDate() + ctx.channel().remoteAddress() + "\t" + request);
			if (HEARTBEAT.equals(request)) {
				// 心跳包
				logger.info("heartbeat");
				ctx.channel().writeAndFlush(new TextWebSocketFrame(HEARTBEAT));
				return;
			}
			int loginIndex = request.indexOf(CTXLOGIN);
			if (loginIndex != -1) {
				// 用户已经通过http登录,game页面初始化时登录角色信息
				JSONObject userObject = JSON.parseObject(request.substring(CTXLOGIN.length()));
				String openId = userObject.getString("openid");
				String nickname = userObject.getString("nickname");
				mapUserCtx.put(openId, ctx);
				db.setUserInfo(openId, nickname);
				//如果没有积分，说明是新用户，要初始化1000积分
				if (db.getUserScore(openId) == null) {
					db.setUserScore(openId, 1000);
				}
				db.setUserResult(openId, ((int) (100 * Math.random())) + "");
				ctx.channel().writeAndFlush(new TextWebSocketFrame("loginok"));
				return;
			}
			int numberIndex = request.indexOf(NUMBER);
			if (numberIndex != -1) {
				// 说明是选了数字
				JSONObject userObject = JSON.parseObject(request.substring(NUMBER.length()));
				userObject.put("code", 2);
				String matchedUserOpenId = userObject.getString("openid");
				ChannelHandlerContext otherCtx = mapUserCtx.get(matchedUserOpenId);
				if (otherCtx != null) {
					otherCtx.writeAndFlush(new TextWebSocketFrame(userObject.toJSONString()));
				} else {
					//游戏没有结束，但是对手线程不在了，说明对手线程异常了
					otherCtx.writeAndFlush(new TextWebSocketFrame("error"));
				}
				return;
			}
			int winIndex = request.indexOf(WIN);
			if (winIndex != -1) {
				// 某一方用户获胜了
				JSONObject userObject = JSON.parseObject(request.substring(WIN.length()));
				String winnerOpenId = userObject.getString("openid");
				String loserOpenId = mapUserCtxReverse.get(ctx);
				ChannelHandlerContext loserCtx = mapCtx.get(ctx);
				ctx.writeAndFlush(new TextWebSocketFrame("win"));
				loserCtx.writeAndFlush(new TextWebSocketFrame("lose"));
				//移除对局
				mapCtx.remove(ctx);
				mapCtx.remove(loserCtx);
				//清除要猜的数
				db.setUserResult(winnerOpenId, ((int) (100 * Math.random())) + "");
				db.setUserResult(loserOpenId, ((int) (100 * Math.random())) + "");
				return;
			}
			int matchIndex = request.indexOf(GAMEMATCH);
			if (matchIndex != -1) {
				// 说明是匹配对手的请求
				ChannelHandlerContext otherCtx;
				JSONObject userObject = JSON.parseObject(request.substring(GAMEMATCH.length()));
				String openId = userObject.get("openid").toString();
				matchingUsers.put(openId);
				while (true) {
					// 等待有人参与匹配，等待队列大小大于零
					if (matchingUsers.size() > 0) {
						String matchedUserOpenId = matchingUsers.poll();
						if (openId.equals(matchedUserOpenId)) {
							matchingUsers.put(openId);
							continue;
						}
						if (matchedUserOpenId != null) {
							// 匹配成功
							String matchedUsername = RedisServer.getInstance().getUserInfo(matchedUserOpenId);
							otherCtx = mapUserCtx.get(matchedUserOpenId);
							if (otherCtx != null) {
								logger.info(getStrDate() + "\t"
										+ "one= " + openId + "\t"
										+ "other= " + matchedUserOpenId);
								// 设置要猜的数,如果对手已经设置了就不要再重新设置了
								int number = Integer.parseInt(db.getUserResult(openId));
								int otherNumber = Integer.parseInt(db.getUserResult(matchedUserOpenId));
								int resNumber = number > otherNumber ? number : otherNumber;
								logger.info("now other result is " + resNumber);
								JSONObject otherUserObject = new JSONObject();
								otherUserObject.put("code", 1);
								otherUserObject.put("openid", matchedUserOpenId);
								otherUserObject.put("nickname", matchedUsername);
								otherUserObject.put("res", resNumber);
								double otherUserScore = RedisServer.getInstance().getUserScore(matchedUserOpenId);
								otherUserObject.put("score", otherUserScore);
								ctx.channel().writeAndFlush(new TextWebSocketFrame(otherUserObject.toJSONString()));
								// 匹配成功
								mapCtx.put(ctx, otherCtx);
								mapUserCtxReverse.put(ctx, matchedUserOpenId);
								logger.info("openid " + openId + " and " + matchedUserOpenId + "result is " + resNumber);
								break;
							} else {
								// 对手已经登录但是此时没有ctx，说明对手环境异常
								ctx.channel().writeAndFlush(new TextWebSocketFrame("error"));
							}
						} else {
							//没取到匹配到的用户，继续循环
							continue;
						}
					}
				}
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