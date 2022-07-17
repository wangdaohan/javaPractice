package com.client.bean.msg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 网关内外沟通用消息体
 */
@Getter
@Setter
@ToString
public class CommonMsg implements Serializable {

    //    包头[ 包体长度 int（4个字节） + 校验和 byte（1个字节） + src short （2个字节） + dst short （2个字节）  + 消息类型 short （2个字节） + 消息状态 byte（1个字节） + 包编号 long （8个字节） ]
    //    包体[ 数据 byte[] ]
    //
    //    =  长度20个字节 + 包体正文长度 = 1个报文消息

    private int bodyLength;

    private byte checksum; //校验和

    private short msgSrc; //消息源

    private short msgDst; //消息目的

    private short msgType;

    private byte status;

    private long msgNo; //消息ID，全局唯一

    @ToString.Exclude
    private byte[] body;


    ////////////////////////////////
    private boolean isLegal;

    private short errCode;

    private long timestamp;





}
