package com.patrick.netty.server;

import com.patrick.netty.common.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        /**
         * 由于netty特性, 因为msg是要缓存里的，所以msg用完，必须要释放缓存
         * ReferenceCountUtil.release(msg);
         *
         * 因为读完request 还有写操作（发送response) 发送response的writeAndFlush已经帮忙release了缓存
         *
         * 如果只有读request操作，则需要去手动release一下
         */
        TranslatorData request = (TranslatorData) msg;
        System.out.println("server receive:" + request.getId() + ":"+request.getName()+":"+request.getMessage());

        TranslatorData response = TranslatorData.builder().id("resp:"+request.getId()).name("resp:"+request.getName()).message("resp:"+request.getMessage()).build();
        ctx.writeAndFlush(response);
    }
}
