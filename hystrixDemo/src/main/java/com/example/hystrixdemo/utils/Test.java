package com.example.hystrixdemo.utils;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: hystrixDemo
 * @BelongsPackage: com.example.hystrixdemo.utils
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-09-23 09:21
 * @Version: 1.0
 */
@Service
public class Test {

    @MyHystrixCommand(fallbackMethod = "test1")
    public String test()  {
        if (true){
            int i = 3 / 0;
        }
        return "test方法";
    }

    public String test1()  {
        System.out.println("你有异常啦");
        return "调用了降级的托底方法";
    }


}
