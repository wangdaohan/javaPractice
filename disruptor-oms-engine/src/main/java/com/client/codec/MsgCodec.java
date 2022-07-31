package com.client.codec;

import com.client.bean.msg.CommonMsg;
import io.vertx.core.buffer.Buffer;


public class MsgCodec implements IMsgCodec{
    @Override
    public Buffer encodeToBuffer(CommonMsg msg) {
        return Buffer.buffer().appendInt(msg.getBodyLength())
                .appendByte(msg.getChecksum())
                .appendShort(msg.getMsgSrc())
                .appendShort(msg.getMsgDst())
                .appendShort(msg.getMsgType())
                .appendByte(msg.getStatus())
                .appendLong(msg.getMsgNo())
                .appendBytes(msg.getBody());
    }

    @Override
    public CommonMsg decodeFromBuffer(Buffer buffer) {
        int bodyLength = buffer.getInt(0);
        byte checkSum = buffer.getByte(4);
        short msgSrc = buffer.getShort(5);
        short msgDst = buffer.getShort(7);
        short msgType = buffer.getShort(9);
        byte status = buffer.getByte(11);
        long packetNo = buffer.getLong(12);
        byte[] bodyBytes = buffer.getBytes(20, 20 + bodyLength);
        CommonMsg msg = new CommonMsg();
        msg.setBodyLength(bodyLength);
        msg.setChecksum(checkSum);
        msg.setMsgSrc(msgSrc);
        msg.setMsgDst(msgDst);
        msg.setMsgType(msgType);
        msg.setStatus(status);
        msg.setMsgNo(packetNo);
        msg.setBody(bodyBytes);
        return msg;
    }
}
