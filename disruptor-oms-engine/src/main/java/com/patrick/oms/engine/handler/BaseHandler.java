package com.patrick.oms.engine.handler;

import com.lmax.disruptor.EventHandler;
import com.patrick.oms.engine.bean.RbCmd;

public abstract class BaseHandler implements EventHandler<RbCmd> {
}
