package com.example.cdnloadbalance.Controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: CDNLoadBalance
 * @BelongsPackage: com.example.cdnloadbalance.Controller
 * @Author: Wuzilong
 * @Description: 原服务器
 * @CreateTime: 2023-06-29 21:11
 * @Version: 1.0
 */
@RestController
@RequestMapping("/server")
public class OriginalServerController {

    //缓存进来的资源
    private Map<String, String> resourceData = new HashMap<>(){{
        put("wzl","武梓龙");
        put("hhh","哈哈哈");
    }};


    /**
    * @Author:Wuzilong
    * @Description: 从缓存中没有获取到数据，直接请求原服务器的数据
    * @CreateTime: 2023/7/5 19:23
    * @param:
    * @return:
    **/
    @GetMapping(value = {"/getResourceInfo"})
    public String getResourceInfo(String resourceName){
        String value = resourceData.get(resourceName);
        if (StringUtils.isEmpty(value)){
            return null;
        }
        return value;
    }


    /**
    * @Author:Wuzilong
    * @Description: 新添加进来的信息
    * @CreateTime: 2023/7/5 19:33
    * @param:  新资源的键值对信息
    * @return: 资源添加成功提示语
    **/
    @PostMapping(value = {"/setResourceInfo"})
    public void setResourceInfo(@RequestBody Map<String, String> resourceInfo){
        resourceData.putAll(resourceInfo);
        System.out.println("新资源添加成功,新资源为："+resourceInfo);
    }

}
