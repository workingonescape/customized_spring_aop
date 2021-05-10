package org.springframework.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Reece
 * @ClassName JDKDynamicProxy.java
 * @Description JDK动态代理生成代理对象
 * @createTime 2021年05月10日 16:36:00
 */
public class JdkDynamicProxy<T> {

    //被代理类
    private Class<?> proxiedClass;

    //被代理类的实例对象
    private Object proxiedObject;


    //被代理类要被进行增强的方法名
    private String proxiedmethodName;


    //代理类
    private Class<?> proxyClass;

    //代理对象要执行的增强方法
    private Method proxyMethod;


    public JdkDynamicProxy(Class<?> proxiedClass, Object proxiedObject, String proxiedmethodName, Class<?> proxyClass, Method proxyMethod) {
        this.proxiedClass = proxiedClass;
        this.proxiedObject = proxiedObject;
        this.proxiedmethodName = proxiedmethodName;
        this.proxyClass = proxyClass;
        this.proxyMethod = proxyMethod;
    }


    public Object getInstance(){

        // proxiedClass: com.reecelin.service.impl.OrderServiceImpl.class
        //proxiedObject: com.reecelin.service.impl.OrderServiceImpl
        //proxiedmethodName: com.reecelin.service.impl.OrderServiceImpl.order方法
        //proxyClass: com.reecelin.aop.LogAspect
        //proxyMethod: com.reecelin.aop.LogAspect.around方法

        return Proxy.newProxyInstance(proxiedClass.getClassLoader(), proxiedClass.getInterfaces(), (proxy, method, args) -> {

            //被代理对象的方法名与method一致，说明当前调用的方法就是需要进行增强的方法
            if (method.getName().equals(proxiedmethodName)) {

                ProceedingJoinPoint proceedingJoinPoint = new ProceedingJoinPoint(proxiedObject, method, args);

                return proxyMethod.invoke(proxyClass.newInstance(), proceedingJoinPoint);
            }

            //不需要进行代理的话 就正常执行方法
            return method.invoke(proxiedObject,args);
        });
    }


}
