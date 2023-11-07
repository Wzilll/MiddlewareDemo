package com.example.cdnloadbalanceslave1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: CDNLoadBalanceSlave1
 * @BelongsPackage: com.example.cdnloadbalanceslave1.Controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-06-30 10:04
 * @Version: 1.0
 */
@RestController
@RequestMapping("/server")
public class LoadBalanceSlaveController  {
    @Autowired
    private Environment environment;
    
    //用来存储想访问的资源的-缓存版本
    private Map<String,String> resourceInfo=new HashMap<>();

    @GetMapping(value = {"/getResourceInfo"})
    public String getResourceInfo(String resourceName){
        String value = resourceInfo.get(resourceName);
        if (!StringUtils.isEmpty(value)){
            return resourceInfo.get(resourceName);
        }else{
            System.out.println("缓存服务器中没有用户想要的资源！");
            String originalService = environment.getProperty("server.url");
            String url="http://"+originalService+"server/getResourceInfo?resourceName="+resourceName;
            RestTemplate restTemplate = new RestTemplateBuilder().build();
            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            if (forEntity.getStatusCode() == HttpStatus.OK) {
                System.out.println("从原服务器获取资源成功，原服务器的ip是：" + originalService);
                resourceInfo.put(resourceName, forEntity.getBody());
                System.out.println("已经将从原服务器获取的资源同步到缓存服务器中了"+resourceInfo);
                return forEntity.getBody();
            }
        }
        return "非常抱歉，没有获取到您想要的资源！";
    }
}
