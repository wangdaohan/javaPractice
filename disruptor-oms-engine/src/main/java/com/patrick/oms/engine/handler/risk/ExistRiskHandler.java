package com.patrick.oms.engine.handler.risk;

import com.client.bean.order.CmdType;
import com.patrick.oms.engine.bean.RbCmd;
import com.patrick.oms.engine.bean.command.CmdResultCode;
import com.patrick.oms.engine.handler.BaseHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.api.set.primitive.MutableLongSet;

@Log4j2
@RequiredArgsConstructor
public class ExistRiskHandler extends BaseHandler {
    @NonNull
    private MutableLongSet uidSet;
    @NonNull
    private MutableIntSet codeSet;

    //发布行情Event
    //新委托event
    //撤单event
    @Override
    public void onEvent(RbCmd rbCmd, long l, boolean b) throws Exception {
        //系统内部指令， 不用起下面逻辑
        if(rbCmd.command == CmdType.HQ_PUB){
            return;
        }
        //1.用户是否存在
        //2。股票代码是否合法
        if (rbCmd.command == CmdType.NEW_ORDER || rbCmd.command == CmdType.CANCEL_ORDER) {
            if (!uidSet.contains(rbCmd.uid)) {
                log.error("illegal uid[{}] exist", rbCmd.uid);
                rbCmd.resultCode = CmdResultCode.RISK_INVALID_USER;
                return;
            }

            if(!codeSet.contains(rbCmd.code)) {
                log.error("illegal stock code [{}] exist",rbCmd.code);
                rbCmd.resultCode = CmdResultCode.RISK_INVALID_CODE;
                return;
            }
        }
    }
}
