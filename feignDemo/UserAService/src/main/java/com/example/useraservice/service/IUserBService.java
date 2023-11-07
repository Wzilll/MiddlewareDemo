package com.example.useraservice.service;

import com.example.openfeigndemo.annotation.MyFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @BelongsProject: UserAService
 * @BelongsPackage: com.example.useraservice.service
 * @Author:Administrator
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-26 08:41
 * @Version: 1.0
 */
@MyFeignClient(name = "B")
@FeignClient
public interface IUserBService {

    @GetMapping("/receiveMessage")
    String getServiceBInfo();

    @GetMapping("/serviceBToGetInfo")
    String getServiceBToGet(@RequestParam("name") String name);
}
