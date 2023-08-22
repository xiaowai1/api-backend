package com.cyj.apibackend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName AuthCheck
 * @Description 权限校验自定义注解
 * @Author chixiaowai
 * @Date 2023/8/22 19:47
 * @Version 1.0
 */
//该注解表示我们自定义的这个注解能用到那些地方，ElementType.METHOD表示能用到方法上
@Target(ElementType.METHOD)
// 表明注解的生命周期，在运行时有效，所以可以通过反射获取
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须有某个权限
     * @return
     */
    String mustRole() default "";
}
