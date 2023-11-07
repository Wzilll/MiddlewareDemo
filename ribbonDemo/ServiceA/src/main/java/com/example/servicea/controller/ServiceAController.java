package com.example.servicea.controller;

import com.example.servicea.service.ServiceA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: ribbonDemo
 * @BelongsPackage: com.example.servicea.controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-09-01 09:05
 * @Version: 1.0
 */
@RestController
@RequestMapping("/serviceA")
public class ServiceAController {
    @Autowired
    private ServiceA serviceA;

    @GetMapping("/callService")
    public String callService() {
        String response = serviceA.getServiceBInfo();
        System.out.println("调用的服务地址是："+response);
        return response;
    }
}
