package com.github.netty.http.client;

import com.sun.javafx.iio.png.PNGImageLoader2;
import com.sun.jndi.toolkit.url.UrlUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatClient implements Runnable{

    public void init(String host,int port){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                            pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                            pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new SimpleClientChannelMessageHandler());
                        }

                    });
            final ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            if (channelFuture.isSuccess()) {
                System.out.println(String.format("connect server(%s:%s) sucess", host, port));
                Channel channel = channelFuture.channel();
                if(channel.isWritable()){
                    while(true){
                        channel.writeAndFlush("hello");
                        Thread.sleep(1000);
                        System.out.println("发送了一个hello");
                    }
                }
                channel.writeAndFlush("hello");
            }
//            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//                @Override
//                public void run() {
//                    channelFuture.channel().writeAndFlush("hello i am navigator");
//                }
//            },0,2, TimeUnit.SECONDS);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void run() {

    }


    public void testConnetion(){

    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.init("192.168.0.100",2048);
    }


}
