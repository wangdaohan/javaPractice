package com.patrick.oms.engine.bean;

import com.alipay.sofa.jraft.rhea.util.Lists;
import com.lmax.disruptor.EventFactory;
import com.patrick.oms.engine.bean.command.CmdResultCode;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

public class RbCmdFactory implements EventFactory<RbCmd> {

    @Override
    public RbCmd newInstance() {
        return RbCmd.builder()
                .resultCode(CmdResultCode.SUCCESS)
                .mathEventList(Lists.newArrayList())
                .marketDataMap(new IntObjectHashMap<>())
                .build();
    }
}
