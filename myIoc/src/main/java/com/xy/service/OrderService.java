package com.xy.service;

import com.xy.annotation.Autowired;
import com.xy.annotation.MyService;

/**
 * @author XY
 */
@MyService
public class OrderService {
    @Autowired
    private MyOrderService tmpOrderService;

    public void find(){
        System.out.println("orderService");
        tmpOrderService.getOrderName();
    }
}
