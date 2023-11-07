package com.example.servicea.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @BelongsProject: ribbonDemo
 * @BelongsPackage: com.example.servicea.service
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-09-01 08:57
 * @Version: 1.0
 */
@Component
public class ServiceA {

    @Autowired
    private  RestTemplate restTemplate;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public String getServiceBInfo(){
        String forObject = restTemplate.getForObject("http://ribbon/B/receiveMessage", String.class);
        return forObject;

    }
}
