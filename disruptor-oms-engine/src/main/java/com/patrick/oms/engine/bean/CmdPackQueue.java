package com.patrick.oms.engine.bean;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.storage.KVEntry;
import com.alipay.sofa.jraft.rhea.util.Lists;
import com.alipay.sofa.jraft.util.Bits;
import com.client.bean.msg.CmdPack;
import com.client.bean.order.OrderCmd;
import com.client.codec.IBodyCodec;
import com.patrick.oms.engine.core.EngineApi;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Log4j2
public class CmdPackQueue {

    private static CmdPackQueue ourInstance = new CmdPackQueue();

    private CmdPackQueue() {

    }

    public static CmdPackQueue getInstance() {
        return ourInstance;
    }

    ////////////////////////////////
    private final BlockingQueue<CmdPack> recvCache = new LinkedBlockingQueue<>();

    public void cache(CmdPack pack) {
        recvCache.offer(pack);
    }

    private RheaKVStore orderKVStore;
    private IBodyCodec bodyCodec;
    private EngineApi engineApi;

    public void init(RheaKVStore orderKVStore, IBodyCodec bodyCodec, EngineApi engineApi){
        this.orderKVStore = orderKVStore;
        this.bodyCodec = bodyCodec;
        this.engineApi = engineApi;

        //单独一个线程监听cache数据变动，并处理消费
        new Thread(() -> {
            while(true) {
                try{
                    CmdPack cmds = recvCache.poll(10, TimeUnit.SECONDS);
                    if (cmds != null) {
                        handleCmd(cmds);
                    }
                } catch (Exception e) {
                    log.error("msg pack recvcache error, continue", e);
                }
            }

        }).start();

    }

    private long lastPackNo = -1;
    private void handleCmd(CmdPack cmd) throws Exception {
        log.info("start consume CmdPack:{}", cmd);
        //NACK 校验 - NACK:对于有错误的包，进行主动请求的方式就叫NACK
        long packNo = cmd.getPackNo();
        if(packNo == lastPackNo) {
            //发送到撮合核心进行撮合
                if(CollectionUtils.isEmpty(cmd.getOrderCmds())){
                    return;
                }
                for(OrderCmd orderCmd : cmd.getOrderCmds()) {
                    engineApi.submitCommand(orderCmd);
                }
        } else if(packNo <= lastPackNo) {
            //收到历史重复的包
            //丢弃
            log.warn("rece duplicate pakcId:{}", packNo);
        } else {
            //收到packNo > lastPackNo
            //跳号
            log.info("packNo lost from {} to {}, begin query from sequencer", lastPackNo + 1, packNo);
            //请求被跳号的数据
            byte[] firstKey = new byte[8];
            Bits.putLong(firstKey, 0, lastPackNo + 1);

            byte[] lastKey = new byte[8];
            Bits.putLong(lastKey, 0, packNo + 1);

            final List<KVEntry> kvEntryList = orderKVStore.bScan(firstKey, lastKey);
            if (CollectionUtils.isNotEmpty(kvEntryList)) {
                List<CmdPack> collect = Lists.newArrayList();
                for(KVEntry entry : kvEntryList) {
                    byte[] value = entry.getValue();
                    if(ArrayUtils.isNotEmpty(value)){
                        collect.add(bodyCodec.deserialize(value, CmdPack.class));
                    }
                }
                collect.sort((o1, o2) -> (int) (
                    o1.getPackNo() - o2.getPackNo()
                ));
                //发送到撮合核心进行撮合
                for(CmdPack cmdPack:collect) {
                    if(CollectionUtils.isEmpty(cmdPack.getOrderCmds())){
                        continue;
                    }
                    for(OrderCmd orderCmd : cmdPack.getOrderCmds()) {
                        engineApi.submitCommand(orderCmd);
                    }
                }
            } else {
                //排队机出错，导致出现跳号
                //这种情况就直接更新lastPackNo
                lastPackNo = packNo;

            }
        }
    }

}
