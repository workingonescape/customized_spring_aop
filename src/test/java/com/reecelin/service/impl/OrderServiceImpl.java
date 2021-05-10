package com.reecelin.service.impl;

import com.reecelin.bean.Order;
import com.reecelin.repository.OrderRepository;
import com.reecelin.service.OrderService;
import org.springframework.annotation.ioc.Autowired;
import org.springframework.annotation.ioc.Service;

import java.util.List;

/**
 * @author Reece
 * @ClassName OrderServiceImpl.java
 * @Description TODO
 * @createTime 2021年05月09日 12:48:00
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderRepository orderRepository;


    /**
    * @description: 根据用户id查询订单
    * @param: id 用户id
    * @return: java.util.List<com.reecelin.bean.Order>
    * @date: 2021-05-09 15:23:12
    */
    @Override
    public List<Order> findAllOrders() {
        System.out.println("进入service层");
        return orderRepository.findAll();
    }


    /*
    * @description: 下单
    * @param: order
    * @return: int
    * @date: 2021-05-10 16:00:58
    */
    @Override
    public int order(Order order) {
        return orderRepository.order(order);
    }

}
