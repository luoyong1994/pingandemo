package com.github.netty.http.bootstrap;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.netty.http.client.ChatClient;
import com.github.netty.http.handler.ChatServerInitializer;

import com.github.netty.http.model.Stocks;
import com.github.netty.http.zipMessage.ZipUtil;
import com.google.gson.Gson;
import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.ImmediateEventExecutor;


public class ChatServer {
	private final static ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	
	private final static EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private Channel channel;
	
	public ChannelFuture start(InetSocketAddress address){
		ServerBootstrap boot = new ServerBootstrap();
		boot.childOption(ChannelOption.SO_KEEPALIVE,true);
		boot.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(createInitializer(group));
		ChannelFuture f = boot.bind(address).syncUninterruptibly();
		channel = f.channel();
		return f;
	}

	protected ChannelHandler createInitializer(ChannelGroup group2) {
		return new ChatServerInitializer(group2);
	}
	
	public void destroy(){
		if(channel != null)
			channel.close();
		group.close();
		workerGroup.shutdownGracefully();
	}

	public void heartCheckOtime(){
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				System.out.println(group.size());
				Gson gson = new Gson();
				String pingCode = gson.toJson("1 << 8 | 220");
				group.writeAndFlush(new TextWebSocketFrame(pingCode).retain());
			}
		}, 0, 2, TimeUnit.SECONDS);
	}
	
	public static void main(String[] args) throws InterruptedException {
		final ChatServer server = new ChatServer();
		ChannelFuture f = server.start(new InetSocketAddress(2048));
		System.out.println("server start................");
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				server.destroy();
			}
		});
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				DecimalFormat format;
				Gson gson = new Gson();
				List<Stocks> stockInfoList = new ArrayList<Stocks>();
				BigDecimal dig;
				for(int i=0;i<100;i++) {
					Stocks stocks = new Stocks();
					stocks.setDate();
					stocks.setS("EURJPY");
					stocks.setTick("1553652958");
					stocks.setP(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					stocks.setN("EUR/JPY");
					stocks.setB1(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					stocks.setS1(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					stocks.setV(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					stocks.setA("0.0");
					stocks.setO(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					stocks.setH(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					stocks.setL(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					stocks.setYC(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					stockInfoList.add(stocks);
				}
				try {
					System.out.println("推送股票信息条数："+stockInfoList.size());
					String stockInfos = gson.toJson(stockInfoList);
					BigDecimal bigDecimal1 = new BigDecimal(stockInfos.getBytes("UTF-8").length);
					BigDecimal bigDecimal2 = new BigDecimal(1024);
					System.out.println("推送股票消息大小为："+bigDecimal1.divide(bigDecimal2).toString()+"K");
					String zipMessage = ZipUtil.gzip(stockInfos);
					bigDecimal1 = new BigDecimal(zipMessage.getBytes("UTF-8").length);
					System.out.println("推送股票消息压缩后大小："+(bigDecimal1.divide(bigDecimal2).toString())+"K");
					TextWebSocketFrame ZipMsg = new TextWebSocketFrame(zipMessage);//
					group.writeAndFlush(ZipMsg.retain());
				}catch (Exception e){
					e.printStackTrace();
				}

			}
		},0,1000, TimeUnit.MILLISECONDS);
		f.channel().closeFuture().syncUninterruptibly();

	}

	
}
