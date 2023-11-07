package com.example.loadserviceb.Controller;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: nginxDemo
 * @BelongsPackage: com.example.loadservicea.Controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-12 12:30
 * @Version: 1.0
 */
@RestController
@RequestMapping("/loadService")
public class LoadServiceController {

    public Map<String,String> resourceInfo =new HashMap<>(){{
        put("百度","www.baidu.com");
        put("csdn","https://blog.csdn.net/weixin_45490198?spm=1010.2135.3001.5343");
    }};

    @GetMapping(value = {"/getResourceInfo"})
    public String getResourceInfo(String resourceName) {
        String resource = resourceInfo.get(resourceName);
        if (StringUtils.isEmpty(resource)){
            return "暂时还没亲请求的资源，抱歉！";
        }else
        {
            System.out.println("您请求的地址是："+resource);
            return resource;
        }
    }

    @Autowired
    private test springMqProduceTest;

    @GetMapping("testMq/{message}")
    public String testMq(@PathVariable("message") String message){
        springMqProduceTest.test("testMq",message);
        return "已发送:"+message;
    }


}
