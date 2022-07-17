package com.example.disruptoromsgateway.handler;

import com.client.bean.OrderCmdContainer;
import com.client.bean.msg.CommonMsg;
import com.client.bean.order.OrderCmd;
import com.client.codec.IBodyCodec;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@AllArgsConstructor
public class MsgHandlerImpl implements IMsgHandler{
    private IBodyCodec bodyCodec;

    @Override
    public void onData(CommonMsg msg) {
        OrderCmd orderCmd;

        try {
            orderCmd = bodyCodec.deserialize(msg.getBody(), OrderCmd.class);

            log.info("recv cmd:{}", orderCmd);

            if(!OrderCmdContainer.getInstance().cache(orderCmd)) {
                log.error("geteway queue insert failed, queue length:{}, odercmd:{}", OrderCmdContainer.getInstance().size(), orderCmd);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("decode cmd error");
        }
    }
}
