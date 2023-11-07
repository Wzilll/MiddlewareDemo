package com.ge;






/**
 * @BelongsProject: rpc-demo
 * @BelongsPackage: com.ge
 * @Author: gepengjun
 * @CreateTime: 2023-09-23  14:32
 * @Description: TODO
 * @Version: 1.0
 */
public class IRpcConsumer {

    public static void main(String[] args) {
        HelloService invoke1 = RpcProxy.invoke(HelloService.class);
        System.out.println(invoke1.sayHello("gpj"));
        System.out.println(invoke1.sayHello("你好"));
        System.out.println(invoke1.sayHello("你好"));
    }
}

