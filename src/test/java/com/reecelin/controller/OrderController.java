package com.reecelin.controller;

import com.reecelin.bean.Order;
import com.reecelin.service.OrderService;
import org.springframework.annotation.ioc.Autowired;
import org.springframework.annotation.ioc.Controller;

import java.util.List;

/**
 * @author Reece
 * @ClassName OrderController.java
 * @Description TODO
 * @createTime 2021年05月09日 12:39:00
 */
@Controller("orderController")
public class OrderController {


    @Autowired
    private OrderService orderService;


    public void getOrders() {
        System.out.println("进入controller层");
        List<Order> orders = orderService.findAllOrders();
        for (Order o : orders) {
            System.out.println(o);

        }
    }


    public int order(Order order) {
        System.out.println("进入controller层");
        return orderService.order(order);
    }
}
