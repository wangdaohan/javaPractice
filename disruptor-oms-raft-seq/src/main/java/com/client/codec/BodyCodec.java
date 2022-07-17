package com.client.codec;

import com.alipay.remoting.serialization.SerializerManager;

public class BodyCodec implements IBodyCodec{
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        //1。jdk序列化 -不适合网络传输 //2。JSON - 不安全 //3。自定义算法（Hessian2)
        return SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return SerializerManager.getSerializer(SerializerManager.Hessian2).deserialize(bytes, clazz.getName());
    }
}
