package com.patrick.oms.engine.bean;

import com.alipay.sofa.jraft.rhea.client.DefaultRheaKVStore;
import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.options.PlacementDriverOptions;
import com.alipay.sofa.jraft.rhea.options.RegionRouteTableOptions;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import com.alipay.sofa.jraft.rhea.options.configured.*;
import com.alipay.sofa.jraft.rhea.util.Lists;
import com.client.bean.msg.CmdPack;
import com.client.bus.IBusSender;
import com.client.bus.MQTTBusSender;
import com.client.checksum.ICheckSum;
import com.client.codec.IBodyCodec;
import com.client.codec.IMsgCodec;
import com.client.hq.MatchData;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.patrick.oms.engine.core.EngineApi;
import com.patrick.oms.engine.bean.orderbook.GOrderBookImpl;
import com.patrick.oms.engine.bean.orderbook.IOrderBook;
import com.patrick.oms.engine.core.EngineCore;
import com.patrick.oms.engine.db.DbQuery;
import com.patrick.oms.engine.handler.BaseHandler;
import com.patrick.oms.engine.handler.match.StockMatchHandler;
import com.patrick.oms.engine.handler.pub.L1PubHandler;
import com.patrick.oms.engine.handler.risk.ExistRiskHandler;
import io.netty.util.collection.IntObjectHashMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbutils.QueryRunner;
import org.eclipse.collections.impl.map.mutable.primitive.ShortObjectHashMap;

import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.util.*;

@Log4j2
@ToString
@Data
@RequiredArgsConstructor
public class EngineConfig {
    //配置文件 - 变量////////////
    private short id;
    private String orderRecvIp;
    private int orderRecvPort;
    private String seqUrlList;
    private String pubIP;
    private int pubPort;

    //////////其它成员变量
    @NonNull
    private String configFileName;
    @NonNull
    private IBodyCodec bodyCodec;
    @NonNull
    private ICheckSum checkSum;
    @NonNull
    private IMsgCodec msgCodec;

    private Vertx vertx = Vertx.vertx();

    public void startup() throws Exception {
        //1.读取配置文件
        initConfig();

        //2.数据库连接:数据库查询功能等
        initDB();

        //3.启动撮合核心
        startEngine();

        //4.建立总线连接，初始化数据的发送 - 将撮合结果进行发布
        initPub();

        //5.初始化排队机的连接 - 本质是连接KV Store,刚好KV Store在排队机
        startSeqConn();
    }

    @Getter
    private IBusSender busSender;
    private void initPub() {
        busSender = new MQTTBusSender(pubIP, pubPort, msgCodec, vertx);
        busSender.startup();
    }

    private void startEngine() throws Exception {
        //1.前置风控处理器
        final BaseHandler riskHandler = new ExistRiskHandler(db.queryAllBalance().keySet(), db.queryAllStockCode());
        //2.撮合处理器(订单簿）撮合/提供行情查询
        IntObjectHashMap<IOrderBook> orderBookMap = new IntObjectHashMap<>();
        db.queryAllStockCode().forEach(code -> orderBookMap.put(code, new GOrderBookImpl(code)));
        final BaseHandler matchHandler = new StockMatchHandler(orderBookMap);

        //3.发布处理器
        ShortObjectHashMap<List<MatchData>> matcherEventMap = new ShortObjectHashMap<>();
        for(short id : db.queryAllMemberIds()) {
            matcherEventMap.put(id, Lists.newArrayList());
        }
        final BaseHandler pubHandler = new L1PubHandler(matcherEventMap, this);

        engineApi = new EngineCore(
                riskHandler,
                matchHandler,
                pubHandler
        ).getApi();


    }

    ////////////////////////////////数据库查询///////////////////////////////////////////
    @Getter
    private DbQuery db;

    private void initDB() {
        QueryRunner runner = new QueryRunner(new ComboPooledDataSource());
        db = new DbQuery(runner);
    }

    @Getter
    private EngineApi engineApi;

    @ToString.Exclude
    private final RheaKVStore orderKvStore = new DefaultRheaKVStore();

    private void startSeqConn() throws Exception{
        ////////////////////1. 连接排队机的缓存 kvCache//////////////////////////////
        final List<RegionRouteTableOptions> routeTableOptions = MultiRegionRouteTableOptionsConfigured
                .newConfigured()
                .withInitialServerList(-1L, seqUrlList)
                .config();
        final PlacementDriverOptions pdOpts = PlacementDriverOptionsConfigured.newConfigured()
                .withFake(true)
                .withRegionRouteTableOptionsList(routeTableOptions)
                .config();
        final RheaKVStoreOptions opts = RheaKVStoreOptionsConfigured.newConfigured()
                .withInitialServerList(seqUrlList)
                .withPlacementDriverOptions(pdOpts)
                .config();
        orderKvStore.init(opts);

        //////////////////////////////////////////////
        ////////////////////2. 监听排队机的UDP消息//////////////////////////////
        //接收排队机过来的委托订单数据流
        //先接收并放一个本地缓存（BlockingQueue)，然后由一个专门的线程在监听这个缓存，并消费
        //委托指令处理器
        CmdPackQueue cmdPackQueue = CmdPackQueue.getInstance();
        cmdPackQueue.init(orderKvStore, bodyCodec, engineApi);

        //接收委托指令
        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
        //组播，设ip为0.0.0.0, 这样就允许多个socket都能接收同一个数据
        //组播先是监听在0.0.0.0，监听成功后再监听一个详细的组播IP地址
        socket.listen(orderRecvPort, "0.0.0.0", asyncRes -> {
            if(asyncRes.succeeded()) {
                try{
                    // 1.要监听的组播IP地址
                    // 2.网卡名字，用哪块网卡去监听UDP的包
                    // 3.source: 发组播消息的源地址IP
                    // 4: 异步处理器
                    socket.listenMulticastGroup(orderRecvIp, mainInterface().getName(), null, asyncRes2 -> {
                        log.info("listen succeed {}", asyncRes2.succeeded());
                    });
                } catch(Exception e){
                    log.error(e);
                }

                //添加消息处理器
                socket.handler(packet -> {
                    Buffer udpData = packet.data();
                    if (udpData.length() > 0) {
                        try {
                            CmdPack cmdPack = bodyCodec.deserialize(udpData.getBytes(), CmdPack.class);
                            //存到本地缓存中CmdPackQueue中，等CmdPackQueue里面的线程去消费
                            CmdPackQueue.getInstance().cache(cmdPack);
                        } catch (Exception e) {
                            log.error("decode failed,", e);
                        }
                    } else {
                        log.error("recv empty udp Packet from client：{}",packet.sender().toString());
                    }
                });

            } else {
                log.error("Listen failed,",asyncRes.cause());
            }
        });
    }

    private void initConfig() throws Exception{
        Properties properties = new Properties();
        properties.load(EngineConfig.class.getClassLoader().getResourceAsStream(configFileName));

        id = Short.parseShort(properties.getProperty("id"));
        orderRecvIp = properties.getProperty("orderrecvip");
        orderRecvPort = Integer.parseInt(properties.getProperty("orderrecvport"));
        seqUrlList = properties.getProperty("sequrllist");
        pubIP = properties.getProperty("pubip");
        pubPort = Integer.parseInt(properties.getProperty("pubport"));
        log.info("read config:{}", this);
    }

    private static NetworkInterface mainInterface() throws Exception {
        //什么样的网卡才能满足接收UDP数据
        //1。 不能是Loopback网卡
        //2。 支持接收multicast
        //3. 不能是虚拟机的网卡
        //4。有IPV4地址的网卡
        final ArrayList<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        final NetworkInterface networkInterface = interfaces.stream().filter(t -> {
           try{
               final boolean isLoopback = t.isLoopback();
               final boolean supportMulticast = t.supportsMulticast();
               final boolean isVirtualBox = t.getDisplayName().contains("VirtualBox") || t.getDisplayName().contains("Host-only");
               final boolean hasIPV4 = t.getInterfaceAddresses().stream().anyMatch(ia -> ia.getAddress() instanceof Inet4Address);
               return !isLoopback && supportMulticast && !isVirtualBox && hasIPV4;
           } catch (Exception e) {
               log.error("find network interface error", e);
           }
           return false;
        }).sorted(Comparator.comparing(NetworkInterface::getName)).findFirst().orElse(null);
        return networkInterface;
    }
}
