package com.client.checksum;

public class CheckSum implements ICheckSum{

    @Override
    public byte getChecksum(byte[] data) {
        byte sum = 0;
        for(byte b : data) {
            sum ^= b;
        }
        return sum;
    }
}
