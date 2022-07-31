package com.patrick.disruptoromsraftseq.bean;

import com.alipay.sofa.common.profile.ArrayUtil;
import com.alipay.sofa.jraft.util.Bits;
import com.alipay.sofa.jraft.util.BytesUtil;
import com.client.bean.msg.CmdPack;
import com.client.bean.order.OrderCmd;
import com.client.bean.order.OrderDirection;
import com.client.fetch.IFetchService;
import com.google.common.collect.Lists;
import com.patrick.disruptoromsraftseq.RaftSeqConfig;
import io.vertx.core.buffer.Buffer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ArrayUtils;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class FetchTask extends TimerTask {

    @NonNull
    private RaftSeqConfig config;

    @Override
    public void run() {
        //遍历网关
        if (!config.getNode().isLeader()) {
            return;
        }

        Map<String, IFetchService> fetchServiceMap = config.getFetchServiceMap();
        if (MapUtils.isEmpty(fetchServiceMap)) {
            return;
        }

        //获取数据
        List<OrderCmd> cmds = collectAllOrders(fetchServiceMap);
        if (CollectionUtils.isEmpty(cmds)) {
            return;
        }

        log.info(cmds);


        //对数据进行排序
        // 排序 时间优先 价格优先 量优先
        cmds.sort((o1, o2) -> {
            //第一种写法
//            if(o1.timestamp > o2.timestamp){
//                return 1;
//            }else if(o1.timestamp < o2.timestamp){
//                return -1;
//            }else {
//                //比优势价格
//                if(o1.direction == OrderDirection.BUY){
//                    if(o1.direction == o2.direction){
//                        if(o1.price > o2.price){
//                            return -1;
//                        }else if(o1.price < o2.price){
//                            return 1;
//                        }else {
//                            //量比较
//                            if(o1.volume > o2.volume){
//                                return -1;
//                            }else if(o1.volume < o2.volume){
//                                return 1;
//                            }else {
//                                return 0;
//                            }
//                        }
//                    }else {
//                        //方向不同 不影响成交结果 顺序不变
//                        return 0;
//                    }
//                }else if(o1.direction == OrderDirection.SELL){
//                    if(o1.direction == o2.direction){
//                        if(o1.price < o2.price){
//                            return -1;
//                        }else if(o1.price > o2.price){
//                            return 1;
//                        }else {
//                            //量比较
//                            if(o1.volume > o2.volume){
//                                return -1;
//                            }else if(o1.volume < o2.volume){
//                                return 1;
//                            }else {
//                                return 0;
//                            }
//                        }
//                    }else {
//                        //方向不同 不影响成交结果 顺序不变
//                        return 0;
//                    }
//                }else {
//                    return 1;
//                }
//            }

            //第二种写法
            //时间优先 价格优先 量优先

            //第二种写法
            int res = compareTime(o1, o2);
            if (res != 0) {
                return res;
            }

            res = comparePrice(o1, o2);
            if (res != 0) {
                return res;
            }

            res = compareVolume(o1, o2);
            return res;
        });

        //TODO 存储到KV Store，广播发送CmdPack 消息到撮合引擎的核心
        try{
            //1.生成CmdPack的PackNo -
            long packNo = getPackNoFromKVStore();

            //2.CmdPack入库 - 保存到KV Store
            CmdPack cmdPack = new CmdPack(packNo, cmds);
            byte[] serialized = config.getCodec().serialize(cmdPack);
            insertToKVStore(packNo, serialized);

            //3.更新PackNo
            updatePackNoInKVStore(packNo + 1);

            //4. 发送CmdPack
            config.getMulticastSender().send(Buffer.buffer(serialized),  //要发送的数据
                                            config.getMulticastPort(),
                                            config.getMulticastIP(),
                                            null);  //handler:异步处理器： 消息一旦发出去离开网卡就会调用，而不是等下游收到才调用。 因此无法通过Handler判断下游是否成功收到消息
        } catch (Exception e){
            log.error("encode cmd packet error", e);
        }
    }


    private void insertToKVStore(long packNo, byte[] serialize) {
        byte[] key = new byte[8];
        Bits.putLong(key, 0, packNo);
        config.getNode().getRheaKVStore().put(key, serialize);
    }

    private static final byte[] PACK_NO_KEY = BytesUtil.writeUtf8("seq_pack_no");//KVStore中的key

    private long getPackNoFromKVStore(){
        final byte[] bPackNo = config.getNode().getRheaKVStore().bGet(PACK_NO_KEY);
        long packNo = 0;
        if(ArrayUtils.isNotEmpty(bPackNo)){
            packNo = Bits.getLong(bPackNo, 0);
        }
        return packNo;
    }

    private void updatePackNoInKVStore(long packNo) {
        byte[] bytes = new byte[8];
        Bits.putLong(bytes, 0, packNo);
        config.getNode().getRheaKVStore().put(PACK_NO_KEY, bytes);
    }

    private int compareVolume(OrderCmd o1, OrderCmd o2) {
        if (o1.volume > o2.volume) {
            return -1;
        } else if (o1.volume < o2.volume) {
            return 1;
        } else {
            return 0;
        }
    }

    private int comparePrice(OrderCmd o1, OrderCmd o2) {
        if (o1.direction == o2.direction) {
            if (o1.price > o2.price) {
                return o1.direction == OrderDirection.BUY ? -1 : 1;
            } else if (o1.price < o2.price) {
                return o1.direction == OrderDirection.BUY ? 1 : -1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    private int compareTime(OrderCmd o1, OrderCmd o2) {
        if (o1.timestamp > o2.timestamp) {
            return 1;
        } else if (o1.timestamp < o2.timestamp) {
            return -1;
        } else {
            return 0;
        }
    }

    private List<OrderCmd> collectAllOrders(Map<String, IFetchService> fetchServiceMap) {
        //不推荐 性能不可控 调试不方便 可读性差
//       List<OrderCmd> res =  fetchServiceMap.values().stream()
//                .map(t -> t.fetchData())
//                .filter(msg -> CollectionUtils.isEmpty(msg))
//                .flatMap(List::stream)
//                .collect(Collectors.toList());

        //推荐~~
        List<OrderCmd> msgs = Lists.newArrayList();
        fetchServiceMap.values().forEach(t -> {
            List<OrderCmd> orderCmds = t.fetchData();
            if (CollectionUtils.isNotEmpty(orderCmds)) {
                msgs.addAll(orderCmds);
            }
        });
        return msgs;
    }
}
