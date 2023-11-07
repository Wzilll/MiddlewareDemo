package com.ge.service;

import com.ge.HelloService;

/**
 * @BelongsProject: rpc-demo
 * @BelongsPackage: com.ge
 * @Author: gepengjun
 * @CreateTime: 2023-09-23  14:37
 * @Description: TODO
 * @Version: 1.0
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        System.out.println(name);
        return "调用者："+name;
    }
}
