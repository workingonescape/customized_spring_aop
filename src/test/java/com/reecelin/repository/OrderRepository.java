package com.reecelin.repository;

import com.reecelin.bean.Order;
import org.springframework.annotation.ioc.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Reece
 * @ClassName OrderRepository.java
 * @Description TODO
 * @createTime 2021年05月10日 16:01:00
 */
@Component
public class OrderRepository {


    private List<Order> orders;

    public OrderRepository() {
        orders = new ArrayList<>();
        Order o1 = new Order("test1", "iphone", "苹果手机", new BigDecimal("12999.00"), "上海", "12345");
        Order o2 = new Order("test2", "iphone", "苹果手机", new BigDecimal("12999.00"), "上海", "12345");
        Order o3 = new Order("test3", "iphone", "苹果手机", new BigDecimal("12999.00"), "上海", "12345");
        Order o4 = new Order("test4", "iphone", "苹果手机", new BigDecimal("12999.00"), "上海", "12345");
        Order o5 = new Order("test5", "iphone", "苹果手机", new BigDecimal("12999.00"), "上海", "12345");
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);
        orders.add(o4);
        orders.add(o5);
    }

    /**
     * @description: 查询订单
     * @param:
     * @return: void
     * @date: 2021-05-10 16:04:04
     */
    public List<Order> findAll() {
        System.out.println("进入dao层");
        return orders;
    }


    /**
    * @description: 下单
    * @param: order
    * @return: int
    * @date: 2021-05-10 16:05:47
    */
    public int order(Order order) {
        System.out.println("进入dao层");
        System.out.println("数据插入成功");
        return 1;
    }


}
