package com.ge;


import com.ge.common.URL;
import com.ge.protocol.HttpServer;
import com.ge.register.LocalRegister;
import com.ge.register.MapRemoteRegister;

public class Provider {

    public static void main(String[] args) {

        LocalRegister.regist(HelloService.class.getName(), "v1.0", HelloServiceImpl.class);

        // 注册中心注册 服务注册
        URL url = new URL("localhost", 8080);
        MapRemoteRegister.regist(HelloService.class.getName(), url);


        // Netty、Tomcat
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(), url.getPort());
    }
}
