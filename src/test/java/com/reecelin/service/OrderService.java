package com.reecelin.service;

import com.reecelin.bean.Order;

import java.util.List;

/**
 * @author Reece
 * @ClassName OrderService.java
 * @Description TODO
 * @createTime 2021年05月09日 12:39:00
 */
public interface OrderService {


    List<Order> findAllOrders();


    int order(Order order);
}
