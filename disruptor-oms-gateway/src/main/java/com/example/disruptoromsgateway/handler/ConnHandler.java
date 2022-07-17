package com.example.disruptoromsgateway.handler;

import com.client.bean.msg.CommonMsg;
import com.example.disruptoromsgateway.GatewayConfig;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class ConnHandler implements Handler<NetSocket> {

    @NonNull
    private GatewayConfig config;

    //    包头[ 包体长度 int（4个字节） + 校验和 byte（1个字节） + src short （2个字节） + dst short （2个字节）  + 消息类型 short （2个字节） + 消息状态 byte（1个字节） + 包编号 long （8个字节） ]
    //    包体[ 数据 byte[] ]
    //
    //    =  长度20个字节 + 包体正文长度 = 1个报文消息

    private static final int PACKET_HEADER_LENGTH = 4 + 1 + 2 + 2 + 2 + 1 + 8;;  //初始为包头的长度

    @Override
    public void handle(NetSocket netSocket) {

        IMsgHandler msgHandler = new MsgHandlerImpl(config.getBodyCodec());
        msgHandler.onConnect(netSocket);


        //自定义解析器 -> 解析HTTP报文，服务端解析客户端的消息
        //报文：报头（报文长度）+包体（数据）
        final RecordParser parser = RecordParser.newFixed(PACKET_HEADER_LENGTH);
        parser.setOutput(new Handler<Buffer>() {
            //    包头[ 包体长度 int（4个字节） + 校验和 byte（1个字节） + src short （2个字节） + dst short （2个字节）  + 消息类型 short （2个字节） + 消息状态 byte（1个字节） + 包编号 long （8个字节） ]
            int bodyLength = -1;
            byte checksum = -1;
            short msgSrc = -1;
            short msgDst = -1;
            short msgType = -1;
            byte status = -1;
            long packetNo = -1;
            @Override
            public void handle(Buffer buffer) { //开始解析原始报文数据
                log.info("handling coming data");
                if (bodyLength == -1) { //读取报头
                    bodyLength = buffer.getInt(0);
                    checksum = buffer.getByte(4);
                    msgSrc = buffer.getShort(5);
                    msgDst = buffer.getShort(7);
                    msgType = buffer.getShort(9);
                    status = buffer.getByte(11);
                    packetNo = buffer.getLong(12);
                    parser.fixedSizeMode(bodyLength);
//                    bodyLength = buffer.getInt(0); //获取报头数据即报文长度，因为一个Int就是4个字节，因为getInt(0)拿到的就是报头信息
//                    checksum = buffer.getByte(4); // 因为前面bodyLength占4个字节，改checksum从第5位（index:4)读取byte
//                    msgSrc = buffer.getShort(5);
//                    msgDst = buffer.getShort(7);
//                    msgType = buffer.getShort(9);
//                    status = buffer.getByte(11);
//                    packetNo = buffer.getLong(12);
//                    parser.fixedSizeMode(bodyLength); //TCP编程原理之一： 会有一个异步线程不断读取报文数据，当达到bodylength时，会再次调用handle方法
                } else {
                    log.info("parsing body");
                    //读取数据
                    byte[] bodyBytes = buffer.getBytes();
                    //组装对象
                    CommonMsg msg;

                    if (checksum != config.getCheckSum().getChecksum(bodyBytes)) {
                        log.error("illegal byte body exist from client:{}", netSocket.remoteAddress());
                        return;
                    } else {
                        if (msgDst != config.getId()) {
                            log.error("recv error msgDst dst: {} from client:{}", msgDst, netSocket.remoteAddress());
                            return;
                        }

                        msg = new CommonMsg();
                        msg.setBodyLength(bodyLength);
                        msg.setChecksum(checksum);
                        msg.setMsgSrc(msgSrc);
                        msg.setMsgDst(msgDst );
                        msg.setMsgType(msgType );
                        msg.setStatus(status);
                        msg.setMsgNo(packetNo);
                        msg.setBody(bodyBytes);
                        msg.setTimestamp(System.currentTimeMillis());
                        msgHandler.onData(msg);
                    }
                    log.info("get msg from client:{}", new String(bodyBytes));
                    //一条数据读取完毕后，恢复现场
                    //恢复现场
                    bodyLength = -1;
                    checksum = -1;
                    msgSrc = -1;
                    msgDst = -1;
                    msgType = -1;
                    status = -1;
                    packetNo = -1;
                    parser.fixedSizeMode(PACKET_HEADER_LENGTH);
                }
            }
        });
        netSocket.handler(parser);
        //关闭连接处理器
        netSocket.closeHandler(c -> {
            msgHandler.onDisconnect(netSocket);
        });
        //异常处理器
        netSocket.exceptionHandler(e -> {
            msgHandler.onException(netSocket, e);
            netSocket.close();
        });
    }
}
