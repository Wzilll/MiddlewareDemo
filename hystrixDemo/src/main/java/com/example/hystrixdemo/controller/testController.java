package com.example.hystrixdemo.controller;

import com.example.hystrixdemo.utils.MyHystrixCommand;
import com.example.hystrixdemo.utils.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: hystrixDemo
 * @BelongsPackage: com.example.hystrixdemo.controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-09-23 09:28
 * @Version: 1.0
 */
@RestController
@RequestMapping("/test")
public class testController {
    @Autowired
    private Test test;



    @RequestMapping(value="hystrixTest",method= RequestMethod.POST)

    public String test(){
        try{
           return test.test();
        }catch (Exception e){
            System.out.println("通过异常进入到AOP中，调用了托底方法");
        }
        return null;
    }


}
