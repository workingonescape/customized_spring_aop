package org.springframework.annotation.aop;

import java.lang.annotation.*;

/**
 * @author Reece
 * @ClassName Around.java
 * @Description TODO
 * @createTime 2021年05月10日 15:43:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Around {

    String execution() default "";
}
