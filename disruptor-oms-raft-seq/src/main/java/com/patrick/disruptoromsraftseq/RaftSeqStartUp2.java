package com.patrick.disruptoromsraftseq;

import com.client.codec.BodyCodec;

public class RaftSeqStartUp2 {
    public static void main(String[] args) throws Exception {
        String configName = "raft-seq2.properties";
        new RaftSeqConfig(configName,new BodyCodec()).startup();
    }
}
