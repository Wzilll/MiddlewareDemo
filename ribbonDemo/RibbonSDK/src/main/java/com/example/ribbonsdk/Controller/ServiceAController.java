package com.example.ribbonsdk.Controller;

import com.example.ribbonsdk.service.ServiceA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: ribbonDemo
 * @BelongsPackage: com.example.ribbonsdk.Controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-31 22:54
 * @Version: 1.0
 */
@RestController
@RequestMapping("/ribbonsdk")
public class ServiceAController {

    @Autowired
    private ServiceA serviceA;

    @RequestMapping(value="getInfo",method= RequestMethod.GET)
    public void getInfo(){
        serviceA.getServiceInfo();
    }
}
