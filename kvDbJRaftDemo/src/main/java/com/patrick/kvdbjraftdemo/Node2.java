package com.patrick.kvdbjraftdemo;

import com.alipay.sofa.jraft.rhea.LeaderStateListener;
import com.alipay.sofa.jraft.rhea.client.DefaultRheaKVStore;
import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.options.PlacementDriverOptions;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import com.alipay.sofa.jraft.rhea.options.StoreEngineOptions;
import com.alipay.sofa.jraft.rhea.options.configured.MemoryDBOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.PlacementDriverOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.RheaKVStoreOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.StoreEngineOptionsConfigured;
import com.alipay.sofa.jraft.rhea.storage.StorageType;
import com.alipay.sofa.jraft.util.Endpoint;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class Node2 {

    private static final AtomicLong leaderTerm = new AtomicLong(-1);

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 8892;
        String dataPath = "d:/server2/";
        String serverList = "127.0.0.1:8891,127.0.0.1:8892,127.0.0.1:8893,127.0.0.1:8894,127.0.0.1:8895";

        final StoreEngineOptions storeOpts = StoreEngineOptionsConfigured
                .newConfigured()
                .withStorageType(StorageType.Memory)//可选memory 或 database
                .withMemoryDBOptions(MemoryDBOptionsConfigured.newConfigured().config())
                .withRaftDataPath(dataPath)
                .withServerAddress(new Endpoint(ip,port))
                .config();
        final PlacementDriverOptions pdOpts = PlacementDriverOptionsConfigured
                .newConfigured()
                .withFake(true)
                .config();
        final RheaKVStoreOptions opts = RheaKVStoreOptionsConfigured.newConfigured()
                .withInitialServerList(serverList)
                .withStoreEngineOptions(storeOpts)
                .withPlacementDriverOptions(pdOpts)
                .config();

        RheaKVStore rheaKVStore = new DefaultRheaKVStore();
        rheaKVStore.init(opts);

        rheaKVStore.addLeaderStateListener(-1, new LeaderStateListener() {
            @Override
            public void onLeaderStart(long newTerm) {
                log.info("##############################node become leader, new Term={}", newTerm);
            }

            @Override
            public void onLeaderStop(long l) {
                leaderTerm.set(-1);
            }
        });
    }
}
