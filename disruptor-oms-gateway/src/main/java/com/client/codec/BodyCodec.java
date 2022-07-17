package com.client.codec;

import com.alipay.remoting.serialization.SerializerManager;
import com.client.bean.order.CmdType;
import com.client.bean.order.OrderCmd;
import com.client.bean.order.OrderDirection;
import com.client.bean.order.OrderType;

import java.io.Serializable;

public class BodyCodec implements IBodyCodec{
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        //1。jdk序列化 -不适合网络传输 //2。JSON - 不安全 //3。自定义算法（Hessian2)
        return SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return SerializerManager.getSerializer(SerializerManager.Hessian2).deserialize(bytes,clazz.getName());
    }


    static class A implements Serializable {
        private String a;
    }
    public static void main(String[] args) throws Exception {
        //OrderCmd(type=NEW_ORDER, timestamp=1658038266046, mid=0, uid=1, code=1, direction=BUY, price=50000, volume=0, orderType=LIMIT, oid=31)
       OrderCmd a = OrderCmd.builder().type(CmdType.NEW_ORDER).timestamp(1658038266046l).mid((short)0).uid(1).code(1).direction(OrderDirection.BUY).price(50000).volume(0).orderType(OrderType.LIMIT).oid(31).build();
       byte[] serialize = new BodyCodec().serialize(a);
        OrderCmd deserial = new BodyCodec().deserialize(serialize, OrderCmd.class);

       System.out.println(deserial.price);


    }

}
