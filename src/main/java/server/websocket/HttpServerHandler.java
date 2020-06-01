/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.rtsp.RtspResponseStatuses;
import io.netty.util.CharsetUtil;

/**
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:12
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	
	public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
		if (msg instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) msg;
			String uri = req.uri();
			int idx = uri.indexOf("code=");
			if (idx != -1) {
				String code = uri.substring(idx + 5);
				String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wxfbc58b2a422781e5&secret=e398010913c4c60568e4c91cff448fe8&js_code=" + code + "&grant_type=authorization_code";
				String data = HttpUtils.get(url);
				String s1 = "session_key";
				String s2 = "openid";
				int iSession_key = data.indexOf(s1);
				if (iSession_key == -1) {
					return;
				}
				
				int iOpenid = data.indexOf(s2);
				String openid = data.substring(iOpenid + s2.length() + 3, data.length() - 2);
				boolean keepAlive = HttpUtil.isKeepAlive(req);
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, msg.decoderResult().isSuccess() ? HttpResponseStatus.OK : RtspResponseStatuses.BAD_REQUEST, Unpooled.copiedBuffer(openid, CharsetUtil.UTF_8));
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
				if (keepAlive) {
					response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
					response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
				}
				
				ChannelFuture f = ctx.write(response);
				if (!keepAlive) {
					f.addListener(ChannelFutureListener.CLOSE);
				}
			}
		}
		
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
}
