package com.example.usera.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @BelongsProject: nginxDemo
 * @BelongsPackage: com.example.usera.Controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-12 11:40
 * @Version: 1.0
 */
@RestController
@RequestMapping("/userA")
public class UserAController {


    @Autowired
    private Environment environment;

    @GetMapping(value = {"/getResource"})
    public void getResource(String key) {
        String serviceIp = environment.getProperty("server.service");
        String url = "http://" + serviceIp + "/agent/getResourceInfo?resourceName="+key;
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        if (forEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("成功获取资源！资源为" + forEntity.getBody());
            String urlPath = forEntity.getBody();
            try {
                Runtime.getRuntime().exec("cmd /c start " + urlPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
