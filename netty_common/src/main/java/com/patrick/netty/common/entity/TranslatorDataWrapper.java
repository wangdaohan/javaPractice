package com.patrick.netty.common.entity;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class TranslatorDataWrapper {
    private TranslatorData data;
    private ChannelHandlerContext ctx;
}
