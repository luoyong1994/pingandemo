package com.github.netty.http.handler;

import com.github.netty.http.model.Stocks;
import com.github.netty.http.zipMessage.ZipUtil;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        super();
        this.group = group;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {

        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(HttpRequestHandler.class);

            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined!"));

            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                TextWebSocketFrame msg) throws Exception {
//        String message = msg.text();
//        System.out.println(message);
//        Gson gson = new Gson();
//        List stockInfoList = createStockInfoList();
//        System.out.println("推送股票信息条数："+stockInfoList.size());
//        String stockInfos = gson.toJson(stockInfoList);
//        System.out.println(stockInfos);
//        BigDecimal bigDecimal1 = new BigDecimal(stockInfos.getBytes("UTF-8").length);
//        BigDecimal bigDecimal2 = new BigDecimal(1024*1024);
//        System.out.println("推送股票消息大小为："+bigDecimal1.divide(bigDecimal2).toString()+"M");
//        String zipMessage = ZipUtil.gzip(stockInfos);
//        bigDecimal1 = new BigDecimal(zipMessage.getBytes("UTF-8").length);
//        System.out.println("推送股票消息压缩后大小："+(bigDecimal1.divide(bigDecimal2).toString())+"M");
//        TextWebSocketFrame ZipMsg = new TextWebSocketFrame(zipMessage);
//        group.writeAndFlush(ZipMsg.retain());



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace();
    }




    public static void main(String[] args) throws UnsupportedEncodingException {

    }
}
