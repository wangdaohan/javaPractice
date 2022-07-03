package com.patrick.netty.client;

import com.patrick.netty.common.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.lang.ref.Reference;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        //为什么一开始要try finally? 由于netty特性, 因为msg是要缓存里的，所以msg用完，必须要释放缓存

        try {
            TranslatorData response = (TranslatorData) msg;
            System.out.println("Client received response: "+response.getId()+":"+response.getName()+":"+response.getMessage());
        } finally {
            //一定要注意，用完了一定要释放缓存
            ReferenceCountUtil.release(msg);
        }
    }
}
