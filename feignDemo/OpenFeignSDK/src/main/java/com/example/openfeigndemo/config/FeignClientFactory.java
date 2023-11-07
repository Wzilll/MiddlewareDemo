package com.example.openfeigndemo.config;

import com.example.openfeigndemo.annotation.MyFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Proxy;

/**
 * @BelongsProject: OpenFeignSDK
 * @BelongsPackage: com.example.openfeigndemo.config
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-26 09:34
 * @Version: 1.0
 */
@Service
public class FeignClientFactory {

    @Value("${server.serviceIp}")
    private String serviceIp;


    public  <T> T create(Class<T> feignClientClass) {

        MyFeignClient myFeignClient = feignClientClass.getAnnotation(MyFeignClient.class);
        String serviceName = myFeignClient.name(); //请求服务的服务名  controller上的服务名
        String baseUrl="http://"+serviceIp+"/"+serviceName;
        return (T)Proxy.newProxyInstance(feignClientClass.getClassLoader(),
                new Class[]{feignClientClass},
                new FeignInvocationHandler(baseUrl));
    }

}
