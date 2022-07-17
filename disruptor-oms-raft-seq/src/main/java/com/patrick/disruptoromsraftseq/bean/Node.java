package com.patrick.disruptoromsraftseq.bean;


import com.alipay.sofa.jraft.rhea.LeaderStateListener;
import com.alipay.sofa.jraft.rhea.client.DefaultRheaKVStore;
import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicLong;

@Log4j2
@RequiredArgsConstructor
public class Node {

    @NonNull
    private final RheaKVStoreOptions options;
    private RheaKVStore rheaKVStore;

    private final AtomicLong leaderTerm = new AtomicLong(-1);

    public boolean isLeader(){
        return leaderTerm.get() > 0;
    }

    /**
     * 停止节点
     */
    public void stop() {
        rheaKVStore.shutdown();
    }

    /**
     * 开始节点
     */
    public void start() {
        rheaKVStore = new DefaultRheaKVStore();
        rheaKVStore.init(this.options);
        rheaKVStore.addLeaderStateListener(-1, new LeaderStateListener() {
            @Override
            public void onLeaderStart(long newTerm) {
                log.info("#####################node become leader");
                leaderTerm.set(newTerm);
            }

            @Override
            public void onLeaderStop(long newTerm) {
                leaderTerm.set(-1);
            }
        });
    }
}
