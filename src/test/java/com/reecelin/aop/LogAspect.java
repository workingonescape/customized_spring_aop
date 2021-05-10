package com.reecelin.aop;

import org.springframework.annotation.aop.Around;
import org.springframework.annotation.aop.Aspect;
import org.springframework.aop.ProceedingJoinPoint;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Reece
 * @ClassName LogAspect.java
 * @Description 切面类
 * @createTime 2021年05月10日 15:45:00
 */
@Aspect
public class LogAspect {



    @Around(execution = "com.reecelin.service.impl.OrderServiceImpl.order")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            System.out.println("前置通知");

           Object result = joinPoint.proceed();

            System.out.println("后置通知");

            return result;
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
