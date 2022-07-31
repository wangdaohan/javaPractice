package com.patrick.oms.engine.handler.pub;

import com.client.bean.msg.CommonMsg;
import com.client.bean.order.CmdType;
import com.client.hq.MatchData;
import com.patrick.oms.engine.bean.EngineConfig;
import com.patrick.oms.engine.bean.RbCmd;
import com.client.hq.L1MarketData;
import com.patrick.oms.engine.bean.orderbook.MatchEvent;
import com.patrick.oms.engine.handler.BaseHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.collections.api.tuple.primitive.ShortObjectPair;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ShortObjectHashMap;

import java.util.List;

import static com.client.bean.msg.MsgConstants.MATCH_HQ_DATA;
import static com.client.bean.msg.MsgConstants.NORMAL;

@Log4j2
@RequiredArgsConstructor
public class L1PubHandler extends BaseHandler {

    public static final int HQ_PUB_RATE = 5000;

    @NonNull
    private final ShortObjectHashMap<List<MatchData>> matcherEventMap;

    @NonNull
    private EngineConfig config;

    @Override
    public void onEvent(RbCmd rbCmd, long l, boolean b) throws Exception {
        final CmdType cmdType = rbCmd.command;
        if (cmdType == CmdType.NEW_ORDER || cmdType == CmdType.CANCEL_ORDER) {
            for (MatchEvent e: rbCmd.mathEventList) {
                matcherEventMap.get(e.mid).add(e.copy());
            }
        } else if(cmdType == CmdType.HQ_PUB) {
            //1. 五档行情
            pubMarketData(rbCmd.marketDataMap);
            //2. 给柜台发送MatchData
            pubMatcherData();
        }
    }

    private void pubMatcherData() {
        if (matcherEventMap.size() == 0) {
            return;
        }
        log.info(matcherEventMap);
        try{
        for (ShortObjectPair<List<MatchData>> s : matcherEventMap.keyValuesView()) {
            if (CollectionUtils.isEmpty(s.getTwo())) {
                continue;
            }
            config.getBodyCodec().serialize(s.getTwo().toArray(new MatchData[0]));
            //清空已发送数据
            s.getTwo().clear();
        }
        }catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * 上下游约定：往什么样的地址发消息，表示是行情消息
     */
    public static final short HQ_ADDRESS = -1;
    private void pubMarketData(IntObjectHashMap<L1MarketData> marketDataMap) {
        log.info(marketDataMap);
        byte[] serialize = null;
        try{
            serialize = config.getBodyCodec().serialize(marketDataMap.values().toArray(new L1MarketData[0]));
        }catch (Exception e) {
            log.error(e);
        }
        if (serialize == null) {
            return;
        }
        pubData(serialize, HQ_ADDRESS, MATCH_HQ_DATA);

    }

    private void pubData(byte[] data, short dst, short msgType) {
        CommonMsg msg = new CommonMsg();
        msg.setBodyLength(data.length);
        msg.setChecksum(config.getCheckSum().getChecksum(data));
        msg.setMsgSrc(config.getId());
        msg.setMsgDst(dst);
        msg.setMsgType(msgType);
        msg.setStatus(NORMAL);
        msg.setBody(data);
        config.getBusSender().publish(msg);
    }
}
