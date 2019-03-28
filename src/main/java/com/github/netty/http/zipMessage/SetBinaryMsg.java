package com.github.netty.http.zipMessage;

import io.netty.channel.Channel;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

import java.util.Base64;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class SetBinaryMsg {
    /**
     * 发送消息
     * @param set 频道通道集合
     * @param msg 发送消息内容
     */
    public static void sendBinaryMsg(CopyOnWriteArraySet<Channel> set, String msg, String channelName){

        if(set==null)return;
        try {
            if(set.size()==0)return;
            String frameType = "";
            msg = ZipUtil.gzip(msg);//压缩数据
            AttributeKey<String> key = AttributeKey.valueOf(channelName+"_binary");
            Iterator<Channel> it=set.iterator();
            while(it.hasNext()){
                Channel channel=it.next();
                if(channel.isActive()){
                    if(channel.attr(key)!=null){
                        frameType = channel.attr(key).get();
                    }
                    if(frameType != null && frameType.equals("false")){
                        channel.writeAndFlush(new TextWebSocketFrame(msg));
                    }else{
                        channel.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(msg.getBytes())));
                    }
                }else{
                    set.remove(channel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){



    }
}
