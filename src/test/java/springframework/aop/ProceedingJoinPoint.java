package springframework.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Reece
 * @ClassName ProceedingJoinPoint.java
 * @Description 连接点
 * @createTime 2021年05月10日 15:46:00
 */
public class ProceedingJoinPoint {

    //被代理类的对象 目标对象
    private Object object;


    //目标对象要实现的方法 目标方法
    private Method method;

    //方法所需要的参数
    private Object[] args;



    public ProceedingJoinPoint(Object object, Method method, Object[] args) {
        this.object = object;
        this.method = method;
        this.args = args;
    }

    /*
    * @description: 执行目标方法
    * @param:
    * @return: java.lang.Object
    * @date: 2021-05-10 15:57:32
    */
    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(object, args);
    }
}
