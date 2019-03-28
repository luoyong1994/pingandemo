package com.github.netty.http.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;


public class ChatServerInitializer extends ChannelInitializer<Channel> {

	private final ChannelGroup group;
	public ChatServerInitializer(ChannelGroup group) {
		super();
		this.group = group;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(new HttpServerCodec());

		pipeline.addLast(new ChunkedWriteHandler());
		
		pipeline.addLast(new HttpObjectAggregator(64*1024));
		
		pipeline.addLast(new HttpRequestHandler("/ws"));
		
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

		pipeline.addLast("decoder", new StringDecoder(CharsetUtil.ISO_8859_1));

		pipeline.addLast("encoder",  new StringEncoder(CharsetUtil.ISO_8859_1));

		pipeline.addLast(new TextWebSocketFrameHandler(group));

		
	}

}
