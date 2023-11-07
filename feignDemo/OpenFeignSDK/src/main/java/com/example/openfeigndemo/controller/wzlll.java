package com.example.openfeigndemo.controller;

import com.example.openfeigndemo.config.FeignConfig;
import com.example.openfeigndemo.config.wzl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: OpenFeignSDK
 * @BelongsPackage: com.example.openfeigndemo.controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-26 17:34
 * @Version: 1.0
 */
@RestController
@RequestMapping("/wzll")
public class wzlll {
    @Autowired
    private FeignConfig feignConfig;


    @RequestMapping(value="wzl",method= RequestMethod.GET)
    public void getServiceAInfo(){

        wzl agentObject = (wzl)feignConfig.getAgentObject();
        agentObject.getServiceBInfo();
        String serviceBToPost = agentObject.getServiceBToGet("wzl");
        System.out.println(serviceBToPost);
    }

}
