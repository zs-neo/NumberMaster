/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.rtsp.RtspResponseStatuses;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static server.websocket.Constant.LOGIN;

/**
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:12
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	
	private Logger logger = LoggerFactory.getLogger(SimpleChannelInboundHandler.class);
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
		if (msg instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) msg;
			String uri = req.uri();
			int loginIndex = uri.indexOf(LOGIN);
			//http请求，用来调微信的api获得openid
			if (loginIndex != -1) {
				String code = uri.substring(loginIndex + 5);
				logger.info("get uri " + uri + "code " + code);
				String data = HttpUtils.getOpenid(code);
				JSONObject jsonData = JSON.parseObject(data);
				String openTd = jsonData.getString("openid");
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
						msg.decoderResult().isSuccess() ? HttpResponseStatus.OK : RtspResponseStatuses.BAD_REQUEST,
						Unpooled.copiedBuffer(openTd, CharsetUtil.UTF_8));
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
				ChannelFuture f = ctx.write(response);
				f.addListener(ChannelFutureListener.CLOSE);
				return;
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
}
