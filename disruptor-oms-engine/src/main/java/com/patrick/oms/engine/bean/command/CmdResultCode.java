package com.patrick.oms.engine.bean.command;

import lombok.Getter;

@Getter
public enum CmdResultCode {
    SUCCESS(1),

    INVALID_ORDER_ID(-1),
    INVALID_ORDER_PRICE(-2),
    DUPLICATE_ORDER_ID(-3),
    UNKNOWN_MATCH_CMD(-4),
    INVALID_ORDER_BOOK_ID(-5),

    RISK_INVALID_USER(-100),
    RISK_INVALID_CODE(-101),
    RISK_INVALID_BALANCE(-102);
//    RISK_INVALID_USER(-103),
//    RISK_INVALID_USER(-104),
//    RISK_INVALID_USER(-2),
//    RISK_INVALID_USER(-2),
//
    public final int code;

    CmdResultCode(int code) {
        this.code = code;
    }


}
