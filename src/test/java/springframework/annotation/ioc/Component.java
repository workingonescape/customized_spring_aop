package springframework.annotation.ioc;

import java.lang.annotation.*;

/**
 * @author Reece
 * @ClassName Ioc.java
 * @Description 元注解 用于标识
 * @createTime 2021年05月10日 11:35:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Component {

    String value() default "";
}
