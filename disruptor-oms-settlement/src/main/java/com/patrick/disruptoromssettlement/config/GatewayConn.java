package com.patrick.disruptoromssettlement.config;

import com.client.bean.order.OrderCmd;
import com.client.bean.msg.CommonMsg;
import com.client.tcp.TcpDirectSender;
import com.patrick.disruptoromssettlement.uuid.GudyUuid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.client.bean.msg.MsgConstants.COUNTER_NEW_ORDER;
import static com.client.bean.msg.MsgConstants.NORMAL;

@Log4j2
@Configuration
public class GatewayConn {
    @Autowired
    private AppConfig config;

    /**
     * 考虑是否用spring管理类：即用@Service @component， 要考虑2个因素
     * 1。 是不是一个公共类，如果是公共类，最好不用依赖spring, 当成一个普通类处理，如TcpDirectSender  ——>因为并不是所有工程都有spring依赖。
     * 2。 如不是一个公共类，只在这个服务用到，而这个服务又有spring 依赖，就最好用@Service @component
     */
    private TcpDirectSender sender;

    @PostConstruct
    private void init(){
        sender = new TcpDirectSender(config.getSendIp(), config.getPort(), config.getVertx());
        sender.startup();
    }

    public void sendOrder(OrderCmd orderCmd) {
        byte[] data = null;
        try{
            data = config.getBodyCodec().serialize(orderCmd);
        } catch (Exception e) {
            log.error("encode error for ordercmd: {}", orderCmd, e);
            return;
        }
        CommonMsg msg = new CommonMsg();
        msg.setBodyLength(data.length);
        msg.setChecksum(config.getCheckSum().getChecksum(data));
        msg.setMsgSrc(config.getId());
        msg.setMsgDst(config.getGatewayId());
        msg.setMsgType(COUNTER_NEW_ORDER);
        msg.setStatus(NORMAL);
        msg.setMsgNo(GudyUuid.getInstance().getUUID());
        msg.setBody(data);
        sender.send(config.getMsgCodec().encodeToBuffer(msg));
    }

    private CommonMsg genMsg(byte[] data, short msgSrc, short msgDst, short msgType, byte status, long packetNo) {
        if (data == null) {
            log.error("empty body");
            return null;
        }
        CommonMsg msg = new CommonMsg();
        msg.setBodyLength(data.length);
        msg.setChecksum(config.getCheckSum().getChecksum(data));
        msg.setMsgSrc(msgSrc);
        msg.setMsgDst(msgDst);
        msg.setMsgType(msgType);
        msg.setStatus(status);
        msg.setMsgNo(packetNo);
        msg.setBody(data);
        return msg;
    }
}
