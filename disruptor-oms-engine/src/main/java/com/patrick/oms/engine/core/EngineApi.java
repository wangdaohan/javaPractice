package com.patrick.oms.engine.core;

import com.client.bean.order.CmdType;
import com.client.bean.order.OrderCmd;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.patrick.oms.engine.bean.RbCmd;
import com.patrick.oms.engine.bean.command.CmdResultCode;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EngineApi {
    private final RingBuffer<RbCmd> ringBuffer;

    public EngineApi(RingBuffer<RbCmd> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
    public void submitCommand(OrderCmd cmd){
        switch (cmd.type) {
            case HQ_PUB:
                ringBuffer.publishEvent(HQ_PUB_TRANSLATOR, cmd);
                break;
            case NEW_ORDER:
                ringBuffer.publishEvent(NEW_ORDER_TRANSLATOR, cmd);
                break;
            case CANCEL_ORDER:
                ringBuffer.publishEvent(CANCEL_ORDER_TRANSLATOR, cmd);
                break;
            default:
                throw new IllegalArgumentException("UnSupported cmdType:" + cmd.getClass().getSimpleName());
        }
    }

    public static final EventTranslatorOneArg<RbCmd, OrderCmd> NEW_ORDER_TRANSLATOR = (rbCmd, seq, newOrderCmd) -> {
        rbCmd.command = CmdType.NEW_ORDER;
        rbCmd.timestamp = newOrderCmd.timestamp;
        rbCmd.mid = newOrderCmd.mid;
        rbCmd.uid = newOrderCmd.uid;
        rbCmd.code = newOrderCmd.code;
        rbCmd.direction = newOrderCmd.direction;
        rbCmd.price = newOrderCmd.price;
        rbCmd.volume = newOrderCmd.volume;
        rbCmd.orderType = newOrderCmd.orderType;
        rbCmd.oid = newOrderCmd.oid;
        rbCmd.resultCode = CmdResultCode.SUCCESS;
    };

    public static final EventTranslatorOneArg<RbCmd, OrderCmd> CANCEL_ORDER_TRANSLATOR = (rbCmd, seq, newOrderCmd) -> {
        rbCmd.command = CmdType.CANCEL_ORDER;
        rbCmd.timestamp = newOrderCmd.timestamp;
        rbCmd.mid = newOrderCmd.mid;
        rbCmd.uid = newOrderCmd.uid;
        rbCmd.code = newOrderCmd.code;
        rbCmd.oid = newOrderCmd.oid;
        rbCmd.resultCode = CmdResultCode.SUCCESS;
    };
    public static final EventTranslatorOneArg<RbCmd, OrderCmd> HQ_PUB_TRANSLATOR = (rbCmd, seq, newOrderCmd) -> {
        rbCmd.command = CmdType.HQ_PUB;
        rbCmd.resultCode = CmdResultCode.SUCCESS;
    };
}
