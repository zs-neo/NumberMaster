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
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel>{
	
	private static final String WEBSOCKET_PATH = "/websocket";
	
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
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(65536));
		pipeline.addLast(new WebSocketServerCompressionHandler());
		pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
		pipeline.addLast(new WebSocketFrameHandler());
		pipeline.addLast(new HttpServerExpectContinueHandler());
		pipeline.addLast(new HttpServerHandler());
	}
	
}
