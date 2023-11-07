package com.example.openfeigndemo.annotation;

public @interface MyEnableFeignClients {
    //用于标注扫描的带有MyFeignClient注解的包路径
    String[] basePackages() default {};
}
