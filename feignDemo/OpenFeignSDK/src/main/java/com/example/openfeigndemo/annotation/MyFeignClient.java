package com.example.openfeigndemo.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyFeignClient {
    String name();

    String url() default "";

    String path() default "";

    String fallback() default "";
}
