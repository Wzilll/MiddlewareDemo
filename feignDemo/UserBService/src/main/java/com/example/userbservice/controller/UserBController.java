package com.example.userbservice.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @BelongsProject: UserBService
 * @BelongsPackage: com.example.userbservice.controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-26 15:44
 * @Version: 1.0
 */
@RestController
@RequestMapping("/serviceB")
public class UserBController {
    @RequestMapping(value="serviceBInfo",method= RequestMethod.GET)
    public String userB(){

        System.out.println("欢迎访问B服务，我是B服务的userB方法");
        return "访问B服务的userB方法成功喽！";
    }

    @RequestMapping(value="serviceBToGetInfo",method= RequestMethod.GET)
    public String userBToGet(String name){

        System.out.println("欢迎访问B服务，我是B服务的userBToGet方法");
        return "访问B服务的userBToGet方法成功喽,你是："+name+"吧！";
    }

    @RequestMapping(value="serviceBToPostInfo",method= RequestMethod.POST)
    public String userBToPost(@RequestParam List<String> nameList){
        System.out.println("欢迎访问B服务，我是B服务的userBToPost方法");
        return "访问B服务的userBToPost方法成功喽,你是输入的内容是："+nameList+"吧！";
    }
}
