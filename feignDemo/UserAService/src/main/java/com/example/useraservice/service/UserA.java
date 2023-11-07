package com.example.useraservice.service;

import com.example.hystrixdemo.utils.MyHystrixCommand;
import com.example.hystrixdemo.utils.MyHystrixProperty;
import com.example.openfeigndemo.config.FeignConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: UserAService
 * @BelongsPackage: com.example.useraservice.service
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-26 08:40
 * @Version: 1.0
 */
@Service
public class UserA {

    @Autowired
    private FeignConfig feignConfig;

    @MyHystrixCommand(fallbackMethod = "errorMsg",commandProperties = {
            @MyHystrixProperty(name = "requestNum", value = "3"), //请求次数
            @MyHystrixProperty(name = "requestTime", value = "10000"), //请求的单位时间
            @MyHystrixProperty(name = "requestErrorRate", value = "50"),  //错误率
            @MyHystrixProperty(name = "requestDelayTime", value = "20000")   //熔断持续时间
    })
    public String userA(){
        System.out.println("进入A服务的方法了,去访问B服务。");
        IUserBService iUserBService=(IUserBService)feignConfig.getAgentObject();
        String returnContext = iUserBService.getServiceBInfo();
        System.out.println("B服务返回的内容是："+returnContext);
        return "A服务调用B服务";
    }

    public String errorMsg()  {
        System.out.println("你有异常啦");
        return "你有异常了";
    }

    public void userAToParam(String name){
        IUserBService iUserBService=(IUserBService)feignConfig.getAgentObject();
        String returnContext = iUserBService.getServiceBToGet(name);
        System.out.println("B服务返回的内容是："+returnContext);
    }
}
