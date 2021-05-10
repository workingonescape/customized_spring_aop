package com.reecelin.test;

import com.reecelin.bean.Order;
import com.reecelin.controller.OrderController;
import org.springframework.container.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

/**
 * @author Reece
 * @ClassName TestSpring.java
 * @Description TODO
 * @createTime 2021年05月10日 18:45:00
 */
public class TestSpring {


    public static void main(String[] args) {
        
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        OrderController controller = (OrderController) context.getBean(OrderController.class);

        controller.getOrders();

        System.out.println("-------------------");
        Order order = new Order("test", "pc", "笔记本电脑", new BigDecimal("12999.00"), "上海", "12345");


        controller.order(order);
    }
}
