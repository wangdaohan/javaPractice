package com.patrick.disruptoromsraftseq;

import com.alipay.sofa.jraft.rhea.options.PlacementDriverOptions;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import com.alipay.sofa.jraft.rhea.options.StoreEngineOptions;
import com.alipay.sofa.jraft.rhea.options.configured.MemoryDBOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.PlacementDriverOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.RheaKVStoreOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.StoreEngineOptionsConfigured;
import com.alipay.sofa.jraft.rhea.storage.StorageType;
import com.alipay.sofa.jraft.util.Endpoint;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.listener.ChannelListener;
import com.alipay.sofa.rpc.transport.AbstractChannel;
import com.client.codec.IBodyCodec;
import com.client.fetch.IFetchService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.patrick.disruptoromsraftseq.bean.FetchTask;
import com.patrick.disruptoromsraftseq.bean.Node;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;

/**
 * 排队机配置 - 启动入口
 */
@Log4j2
@ToString
@RequiredArgsConstructor
public class RaftSeqConfig {
    private String dataPath;
    private String serverUrl;
    private String serverList;

    @NonNull
    private String fileName;

    @Getter
    private Node node;

    public void startup() throws Exception {
        //1.读取配置文件
        initConfig();

        //2.初始化 和启动集群
        startRaftSeqDbCluster();

        //TODO 3.启动下游广播

        //TODO 4.初始化网关连接
        startupFetch();

    }

    /////////////////////抓取网关消息逻辑////////////////////////////
    private String fetchurls;
    @ToString.Exclude
    @Getter
    private Map<String, IFetchService> fetchServiceMap = Maps.newConcurrentMap();

    @NonNull
    @ToString.Exclude
    @Getter
    private IBodyCodec codec;


    @RequiredArgsConstructor
    private class FetchChannelListener implements ChannelListener {
        @NonNull
        private ConsumerConfig<IFetchService>  config;

        @Override
        public void onConnected(AbstractChannel channel) {
            String remoteAddr = channel.remoteAddress().toString();
            log.info("connect to gateway : {}",remoteAddr);
            fetchServiceMap.put(remoteAddr,config.refer());//为什么put要执行2次 - 2次, 因为ChannelListener的onConnected只会在断开重新连接上时才会调用
        }

        @Override
        public void onDisconnected(AbstractChannel channel) {
            String remoteAddr = channel.remoteAddress().toString();
            log.info("disconnect from gateway : {}",remoteAddr);
            fetchServiceMap.remove(remoteAddr);
        }
    }
    //1.从哪些网关抓取
    //2。通信方式
    private void startupFetch() {
        //1.建立所有网关的连接
        String[] urls = fetchurls.split(";");
        for(String url : urls) {
            ConsumerConfig<IFetchService> consumerConfig = new ConsumerConfig<IFetchService>()
                    .setInterfaceId(IFetchService.class.getName())//通信接口
                    .setProtocol("bolt")//RPC通信协议
                    .setTimeout(5000)//超时时间
                    .setDirectUrl(url);//直连地址
            consumerConfig.setOnConnect(Lists.newArrayList(new FetchChannelListener(consumerConfig)));
            fetchServiceMap.put(url,consumerConfig.refer()); //为什么put要执行2次 - 1次
        }

        //2.定时抓取数据的任务
        new Timer().schedule(new FetchTask(this),5000,1000);
    }

    ////////////启动KV Store////////////////////////
    private void startRaftSeqDbCluster(){
        String[] serverUrlSplit = serverUrl.split(":");
        log.info("##############################server ip:{}, port:{}", serverUrlSplit[0], serverUrlSplit[1]);
        final StoreEngineOptions storeOpts = StoreEngineOptionsConfigured.newConfigured()
                .withStorageType(StorageType.Memory)
                .withMemoryDBOptions(MemoryDBOptionsConfigured.newConfigured().config())
                .withRaftDataPath(dataPath)
                .withServerAddress(new Endpoint(serverUrlSplit[0], Integer.parseInt(serverUrlSplit[1])))
                .config();
        /**
         * PlacementDriverOptions是给当有多个Raft集群时使用
         *
         * 我们的程序只有一个Raft集群（集群里有多个Node), 所以用withFake(true),即PlacementDriverOptions不生效
         */
        final PlacementDriverOptions pdOpts = PlacementDriverOptionsConfigured.newConfigured()
                .withFake(true)
                .config();
        final RheaKVStoreOptions opts = RheaKVStoreOptionsConfigured.newConfigured()
                .withInitialServerList(serverList)
                .withStoreEngineOptions(storeOpts)
                .withPlacementDriverOptions(pdOpts)
                .config();
        node = new Node(opts);
        node.start();
        Runtime.getRuntime().addShutdownHook(new Thread(node::stop)); //将node stop方法加入到系统的shutdownhook中
        log.info("started jRaft Seq node success on port:{}", serverUrlSplit[1]);
    }


    private void initConfig() throws IOException {
        Properties properties = new Properties();
        properties.load(RaftSeqConfig.class.getClassLoader().getResourceAsStream(fileName));
        dataPath = properties.getProperty("datapath");
        serverUrl = properties.getProperty("serverurl");
        serverList = properties.getProperty("serverlist");
        fetchurls = properties.getProperty("fetchurls");
        log.info("read config:{}", this);
    }
}
