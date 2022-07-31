package com.patrick.oms.engine.db;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.eclipse.collections.impl.map.mutable.primitive.LongLongHashMap;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;

import java.util.List;
import java.util.Map;

import static com.client.bean.msg.MsgConstants.MULTI_FACTOR;

@Log4j2
@AllArgsConstructor
public class DbQuery {
    @NonNull
    private QueryRunner runner;

    public LongLongHashMap queryAllBalance() throws Exception {
        List<Map<String, Object>> listMap = runner.query("select uid,balance from t_user", new MapListHandler());
        if (CollectionUtils.isEmpty(listMap)) {
            throw new Exception("user data empty");
        }
        LongLongHashMap uidBalanceMap = new LongLongHashMap();
        for(Map<String, Object> map : listMap) {
            uidBalanceMap.put(
                    Long.parseLong(map.get("uid").toString()),
                    Long.parseLong(map.get("balance").toString()) * MULTI_FACTOR
            );
        }

        return uidBalanceMap;
    }

    public IntHashSet queryAllStockCode() throws Exception {
        List<Map<String, Object>> listMap = runner.query("select code from t_stock where status = 1", new MapListHandler());
        if (CollectionUtils.isEmpty(listMap)) {
            throw new Exception("stock data empty");
        }
        IntHashSet codes = new IntHashSet();
        for (Map<String, Object> map : listMap) {
            codes.add(Integer.parseInt(map.get("code").toString()));
        }
        return codes;
    }

    public short[] queryAllMemberIds() throws Exception {
        List<Map<String, Object>> listMap = runner.query("select id from t_member where status = 1", new MapListHandler());
        if (CollectionUtils.isEmpty(listMap)) {
            throw new Exception("member data empty");
        }
        short[] memberIds = new short[listMap.size()];
        int i = 0;
        for (Map<String , Object> map : listMap) {
            memberIds[i] = Short.parseShort(map.get("id").toString());
        }

        return memberIds;
    }
}
