package com.example.ribbonsdk.service;

import com.example.ribbonsdk.config.test.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @BelongsProject: ribbonDemo
 * @BelongsPackage: com.example.ribbonsdk.service
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-08-28 08:20
 * @Version: 1.0
 */
@Service
public class ServiceA {
    @Autowired
    private RequestInterceptor requestInterceptor;

    public void getServiceInfo(){
        String url = "http://"+"B"+"/B/receiveMessage/";
        RestTemplate restTemplate=new RestTemplateBuilder().build();
        restTemplate.getInterceptors().add(requestInterceptor);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        if (forEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("调用B服务成功！");
        }
    }
}
