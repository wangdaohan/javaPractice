package com.client.fetch;


import com.client.bean.order.OrderCmd;
import java.util.List;

public interface IFetchService {

    List<OrderCmd> fetchData();

}