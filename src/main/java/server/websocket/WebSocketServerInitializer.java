/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;

/**
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:35
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final String WEBSOCKET_PATH = "/guessNumber";
	
	// ssl证书
	private final SslContext sslCtx;
	
	public WebSocketServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		if (sslCtx != null) {
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		}
		// codec
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		// 用POST方式请求服务器时，参数信息是保存在message body的,如果只单纯用HttpServerCodec是无法完全的解析Http POST请求的
		// 因为HttpServerCodec只能获取uri中参数，所以需要加上HttpObjectAggregator.
		pipeline.addLast(new HttpObjectAggregator(65536));
		pipeline.addLast(new WebSocketServerCompressionHandler());
		// 这个处理程序为运行一个网络套接字服务器做了所有繁重的工作
		pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
		// 自定义处理
		pipeline.addLast(new WebSocketFrameHandler());
		// 处理HTTP请求,调用腾讯的接口获取openid
		pipeline.addLast(new HttpServerExpectContinueHandler());
		pipeline.addLast(new HttpServerHandler());
	}
	
}
