package com.example.usera.Controller;

import com.example.localdns.Controller.LocalDNSController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @BelongsProject: UserA
 * @BelongsPackage: com.example.usera
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-06-29 19:48
 * @Version: 1.0
 */
@RestController
@RequestMapping("/userA")
public class UserAController {


    @Autowired
    private Environment environment;

    @Autowired
    private LocalDNSController localDNS;

    @GetMapping(value = {"/getResource"})
    public void getResource(String key) {
        String property = environment.getProperty("server.name");
        String ipPath = localDNS.getIpAddress(property);
        String url = "http://" + ipPath + "/server/getResourceInfo?resourceName="+key;
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        if (forEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("成功获取资源！资源为" + forEntity.getBody());
        }
    }

    @GetMapping(value = {"/registerCDN"})
    public void registerCDN(String domainName){
        localDNS.addCDNServer(domainName);
    }
}
