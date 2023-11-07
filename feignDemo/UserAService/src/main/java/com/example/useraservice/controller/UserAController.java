package com.example.useraservice.controller;

import com.example.useraservice.service.UserA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: UserAService
 * @BelongsPackage: com.example.useraservice.controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-26 15:44
 * @Version: 1.0
 */
@RestController
@RequestMapping("/serviceA")
public class UserAController {
    @Autowired
    private UserA userA;
    @RequestMapping(value="serviceAInfo",method= RequestMethod.GET)
    public void getServiceAInfo(){
        try{
            userA.userA();
        }catch (Exception e){
            System.out.println("通过异常进入到AOP中，已调用了托底方法");
        }

    }


    @RequestMapping(value="serviceAInfoToParam",method= RequestMethod.GET)
    public void getServiceAInfoParam(String name){
        userA.userAToParam(name);
    }
}
