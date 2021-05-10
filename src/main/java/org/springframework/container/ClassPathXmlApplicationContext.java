package org.springframework.container;

import org.springframework.annotation.aop.Around;
import org.springframework.annotation.aop.Aspect;
import org.springframework.annotation.ioc.Autowired;
import org.springframework.annotation.ioc.Component;
import org.springframework.annotation.ioc.Controller;
import org.springframework.annotation.ioc.Service;
import org.springframework.aop.JdkDynamicProxy;
import org.springframework.xml.SpringConfigParser;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Reece
 * @version 1.0.0
 * @ClassName ClassPathXmlApplicationContext.java
 * @Description TODO
 * @createTime 2021年05月09日 10:23:00
 */
public class ClassPathXmlApplicationContext {


    //applicationContext.xml
    private String configBase;


    //存储类的全路径
    private final List<String> classPaths = new CopyOnWriteArrayList<>();


    //存储对象名与对象之间的映射 byName
    private Map<String, Object> iocNameContainer;


    //存储class与对象之间的映射 byType
    private Map<Class<?>, Object> iocContainer;


    //存储接口与对象之间的映射
    private Map<Class<?>, List<Object>> iocInterfaceContainer;


    //存储被代理类
    private Set<Class<?>> proxiedClassSet;


    public ClassPathXmlApplicationContext(String configBase) {
        this.configBase = configBase;
        init();
    }


    /**
     * @methodName：init
     * @description: 初始化方法
     * @param:
     * @return: void
     * @date: 2021-05-09 12:07:59
     */
    private void init() {
        String baePackage = SpringConfigParser.getBaePackage(configBase);
        //获取类的全路径
        findClassPath(baePackage);

        //实例化
        doInstance();

        //进行aop
        doAop();

        //依赖注入 DI
        doDI();
    }


    /**
     * @methodName：loadClasses
     * @description: 加载类
     * @param: basePackage
     * @return: void
     * @date: 2021-05-09 12:24:03
     */
    private void findClassPath(String basePackage) {
        if (basePackage == null || basePackage.isEmpty()) {
            throw new RuntimeException("basePackage is null");
        }
        // basePackage: com.reecelin
        URL url = Thread.currentThread().getContextClassLoader().getResource("");

        //url: file:/E:/Idea_WorkSpace/customized_spring_ioc/classes/
        if (url == null) {
            throw new RuntimeException("url is null");
        }
        basePackage = basePackage.replace(".", File.separator);
        // file: E:\Idea_WorkSpace\customized_spring_ioc\target\classes\com\reecelin
        File file = new File(url.toString().replace("file:/", ""), basePackage);
        findAllClasses(file);
    }


    /**
     * @description: 递归遍历每个包，找出每个类的全路径
     * @param: file
     * @return: void
     * @date: 2021-05-10 14:29:28
     */
    private void findAllClasses(File file) {
        if (file == null) {
            throw new RuntimeException("file not exists");
        }
        File[] files = file.listFiles();
        if (files == null) {
            throw new RuntimeException("error occurred when finding classPath");
        }
        for (File f : files) {
            if (!f.isDirectory()) {
                //类的全路径
                String path = getFullPath(f.getPath());
                classPaths.add(path);
            } else {
                findAllClasses(f);
            }
        }
    }


    /**
     * @description: 获取类的全路径
     * @param: path
     * @return: java.lang.String
     * @date: 2021-05-09 12:34:18
     */
    private String getFullPath(String path) {
        int index = path.indexOf("classes\\");
        path = path.substring(index + 8, path.length() - 6);
        path = path.replace(File.separator, ".");
        return path;
    }


    /**
     * @description: 实例化并保存到IOC容器中
     * @param:
     * @return: void
     * @date: 2021-05-09 13:16:16
     */
    private void doInstance() {
        if (classPaths.isEmpty()) {
            return;
        }
        try {
            //懒加载
            iocNameContainer = new ConcurrentHashMap<>();
            iocContainer = new ConcurrentHashMap<>();
            iocInterfaceContainer = new ConcurrentHashMap<>();


            //遍历
            for (String classPath : classPaths) {

                Class<?> c = Class.forName(classPath);

                //接口不用进行实例化
                if (c.isInterface()) {
                    continue;
                }

                //有注解标注的类要获取类名或者别名
                String annotationName = null;

                //对象名 用作iocNameContainer的key
                String objectName = "";

                //实例化
                Object o = c.newInstance();

                //按类型注入时使用到
                iocContainer.put(c, o);


                //获取ioc相关注解
                Component componentAnnotation = c.getAnnotation(Component.class);
                Service serviceAnnotation = c.getAnnotation(Service.class);
                Controller controllerAnnotation = c.getAnnotation(Controller.class);


                //判断类上是否有注解
                if (componentAnnotation != null || serviceAnnotation != null || controllerAnnotation != null) {


                    if (componentAnnotation != null || serviceAnnotation != null) {

                        //类实现的接口
                        Class<?>[] interfaces = c.getInterfaces();

                        //遍历实现的接口
                        for (Class<?> inter : interfaces) {
                            //获取对象集合
                            List<Object> objects = iocInterfaceContainer.get(inter);

                            if (objects == null) {
                                //初始化
                                objects = new CopyOnWriteArrayList<>();
                                //存放到list中
                                objects.add(o);
                                //接口与对象之间的映射
                                iocInterfaceContainer.put(inter, objects);
                            } else {
                                //直接存放到list中
                                objects.add(0);
                            }
                        }
                    }


                    //同一个类上三者只有一个
                    if (componentAnnotation != null) {
                        annotationName = componentAnnotation.value();
                    } else {
                        annotationName = controllerAnnotation == null ? serviceAnnotation.value() : controllerAnnotation.value();
                    }
                }

                if (annotationName == null || "".equals(annotationName)) {
                    objectName = getLowCaseName(c);
                } else {
                    objectName = annotationName;
                }

                //不能使用相同名称
                if (iocNameContainer.containsKey(objectName)) {
                    throw new RuntimeException("IOC has already exists bean name: " + objectName);
                }

                //按名字注入时使用到
                iocNameContainer.put(objectName, o);
            }
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    
    /**
    * @description: 获取首字母小写的类名
    * @param: c
    * @return: java.lang.String
    * @date: 2021-05-10 18:35:29
    */
    private String getLowCaseName(Class<?> c) {
        if (c == null) {
            return "";
        }
        String className = c.getSimpleName();
       return  String.valueOf(className.charAt(0)).toLowerCase() + className.substring(1);
        
    }


    /**
     * @description: 依赖注入
     * @param:
     * @return: void
     * @date: 2021-05-09 17:19:26
     */
    private void doDI() {
        //使用iocContainer
        Set<Class<?>> classes = iocContainer.keySet();
        //需要进行依赖注入的类
        for (Class<?> clazz : classes) {

            //被代理类的依赖注入已在doAop中完成，此处不需要进行重复依赖注入
            if (!proxiedClassSet.contains(clazz)) {
                //获取类的属性
                Field[] declaredFields = clazz.getDeclaredFields();
                //遍历属性
                for (Field field : declaredFields) {
                    doDIByClass(clazz, field);
                }
            }
        }
    }


    /**
     * @description: 依赖注入执行方法
     * @param: clazz
     * @param: field
     * @return: void
     * @date: 2021-05-10 17:23:10
     */
    private void doDIByClass(Class<?> clazz, Field field) {

        //属性上是否有Autowired注解
        if (field.isAnnotationPresent(Autowired.class)) {
            //需要被注入的属性
            Object bean = null;
            //需要进行依赖注入
            Autowired autowired = field.getAnnotation(Autowired.class);

            //按照名称进行查找
            if (!"".equals(autowired.value())) {
                //按照名称查找
                bean = getBean(autowired.value());
                if (bean == null) {
                    throw new RuntimeException("No qualifying bean of " + autowired.value() + "  found");
                }
            } else {
                //按照类型查找 这里使用的是filed属性的类型
                Class<?> filedType = field.getType();
                bean = getBean(filedType);

                //类型查找失败时还要进行接口查找
                if (bean == null) {
                    //按照接口来查找
                    List<Object> objects = getBeanByInterface(filedType);
                    if (objects == null) {
                        throw new RuntimeException("No qualifying bean of " + filedType + "  found");
                    } else if (objects.size() > 1) {
                        throw new RuntimeException("duplicate  bean of " + filedType + ", need 1, but found " + objects.size());
                    } else {
                        bean = objects.get(0);
                    }
                }
            }
            field.setAccessible(Boolean.TRUE);
            try {
                //设置属性值，也就是依赖注入真正实现的地方
                //iocContainer.get(clazz) 属性需要被注入的类
                field.set(iocContainer.get(clazz), bean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
    * @description: 根据类名或者别名获取实例
    * @param: beanName
    * @return: java.lang.Object
    * @date: 2021-05-10 18:36:35
    */
    public Object getBean(String beanName) {
        return iocNameContainer.get(beanName);
    }

    /**
    * @description: 根据类的类型获取实例
    * @param: clazz
    * @return: java.lang.Object
    * @date: 2021-05-10 18:36:41
    */
    public Object getBean(Class<?> clazz) {
        return iocContainer.get(clazz);
    }


    /**
    * @description: 根据类的类型获取实例集合
    * @param: clazz
    * @return: java.util.List<java.lang.Object>
    * @date: 2021-05-10 18:40:30
    */
    private List<Object> getBeanByInterface(Class<?> clazz) {
        return iocInterfaceContainer.get(clazz);
    }


    /**
     * @description: 扫描切面类
     * @param:
     * @return: void
     * @date: 2021-05-10 16:10:26
     */
    private void doAop() {

        Set<Class<?>> set = iocContainer.keySet();

        if (set.isEmpty()) {
            return;
        }

        for (Class<?> c : set) {

            if (!c.isAnnotationPresent(Aspect.class)) {
                continue;
            }


            Method[] methods = c.getDeclaredMethods();

            for (Method method : methods) {
                try {
                    if (!method.isAnnotationPresent(Around.class)) {
                        continue;
                    }

                    String annotationValue = method.getAnnotation(Around.class).execution();

                    //类的全路径
                    String classFullPath = annotationValue.substring(0, annotationValue.lastIndexOf("."));

                    //方法名
                    String proxiedMethodName = annotationValue.substring(annotationValue.lastIndexOf(".") + 1);

                    //要被代理的类
                    Class<?> proxiedClazz = Class.forName(classFullPath);

                    //要是被代理类中有属性需要进行依赖注入的，则在此处进行
                    Field[] declaredFields = proxiedClazz.getDeclaredFields();

                    for (Field field : declaredFields) {
                        if (field.isAnnotationPresent(Autowired.class)) {
                            doDIByClass(proxiedClazz, field);

                            //要被代理的类放入集合中，以便依赖注入时进行判断,避免重复依赖注入
                            if (proxiedClassSet == null) {
                                proxiedClassSet = new CopyOnWriteArraySet<>();
                            }
                            proxiedClassSet.add(proxiedClazz);
                        }
                    }

                    //被代理类的实例对象
                    Object proxiedObject = iocContainer.get(proxiedClazz);

                    //jdk动态代理
                    JdkDynamicProxy<Object> proxy = new JdkDynamicProxy<>(proxiedClazz, proxiedObject, proxiedMethodName, c, method);

                    //获得代理对象实例
                    Object proxyInstance = proxy.getInstance();

                    //因为此时三个container中还保存着被代理对象的实例，所以这里需要进行替换
                    iocContainer.put(proxiedClazz, proxyInstance);

                    String simpleName = proxiedClazz.getSimpleName();
                    String name = String.valueOf(simpleName.charAt(0)).toLowerCase() + simpleName.substring(1);
                    iocNameContainer.put(name, proxyInstance);


                    //被代理类实现的接口数组
                    Class<?>[] interfaces = proxiedClazz.getInterfaces();

                    //遍历数组
                    for (Class<?> inter : interfaces) {

                        List<Object> objectList = iocInterfaceContainer.get(inter);

                        if (objectList == null || objectList.isEmpty()) {
                            continue;
                        }

                        //一个接口可能有多个实现类
                        //遍历实现类集合，判断这些实现类中是否含有被代理类
                        for (int i = 0; i < objectList.size(); i++) {
                            //获取类
                            Class<?> clazz = objectList.get(i).getClass();
                            //如果含有被代理类，则进行替换
                            //将value这个对象数组中原始对象替换为代理对象实例
                            if (clazz == proxiedClazz) {
                                //按照索引下标进行替换
                                objectList.set(i, proxyInstance);
                                //只可能有一个，不然doInstance方法会先报错
                                break;
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}



