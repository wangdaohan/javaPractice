package com.client.bean.msg;

import com.client.bean.order.OrderCmd;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CmdPack implements Serializable {

    private long packNo; //根据包号，确认是是否有丢号 或乱序的情况

    private List<OrderCmd> orderCmds;

}
