package com.patrick.netty.common.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TranslatorData implements Serializable {
    private static final long serialVersionUID = 321885185447780182L;

    private String id;
    private String name;
    private String message;


}
