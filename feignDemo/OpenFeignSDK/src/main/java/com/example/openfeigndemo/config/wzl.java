package com.example.openfeigndemo.config;

import com.example.openfeigndemo.annotation.MyFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @BelongsProject: OpenFeignSDK
 * @BelongsPackage: com.example.openfeigndemo.config
 * @Author:Administrator
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-26 17:35
 * @Version: 1.0
 */
@MyFeignClient(name="serviceB")
public interface wzl {

    @GetMapping("/serviceBInfo")
    String getServiceBInfo();

    @GetMapping("/serviceBToGetInfo")
    String getServiceBToGet(@RequestParam("name") String name);

}
