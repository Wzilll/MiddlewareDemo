package com.example.agent.Controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @BelongsProject: nginxDemo
 * @BelongsPackage: com.example.agent.Controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-12 11:51
 * @Version: 1.0
 */
@RestController
@RequestMapping("/agent")
public class AgentController {


    public List<String> serviceIp =new ArrayList<>(){{
        add("localhost:8004");
        add("localhost:8005");
    }};

    // 创建服务器列表
   public Map<String,Integer> serverList = new HashMap<>(){{
        put("localhost:8004",7); // 权重值为7
        put("localhost:8005",3); // 权重值为3
    }};

   public Map<String,String> resourceInfo=new HashMap<>();


    int index = 0;

    /**
    * @Author:Wuzilong
    * @Description: 通过不同的策略获取ip地址请求资源
    * @CreateTime: 2023/7/12 12:27
    * @param:
    * @return:  请求获取的资源
    **/
    @GetMapping(value = {"/getResourceInfo"})
    public String getResourceInfo(String resourceName){
        String resource = resourceInfo.get(resourceName);
        if (StringUtils.isEmpty(resource)){
            String urlIp = this.chooseServer();
            System.out.println("获取到的ip地址为"+urlIp);
            String url = "http://" + urlIp + "/loadService/getResourceInfo?resourceName="+resourceName;
            RestTemplate restTemplate = new RestTemplateBuilder().build();
            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            if (forEntity.getStatusCode() == HttpStatus.OK) {
                System.out.println("成功获取资源！资源为" + forEntity.getBody());
                resourceInfo.put(resourceName, forEntity.getBody());
                return forEntity.getBody();
            }else{
                return "没有请求到资源";
            }
        }else{
            System.out.println("从nginx缓存中获取的资源");
            return resource;
        }

    }



    //轮询获取服务的IP地址
    public  String polling(){
        String ipContext = serviceIp.get(index);
        index=(index+1)%serviceIp.size();
        return ipContext;
    }


    public String chooseServer() {
        int totalWeight = serverList.values().stream().mapToInt(Integer::intValue).sum();
        int randomWeight = new Random().nextInt(totalWeight); // 生成一个随机权重值
        int cumulativeWeight = 0; // 累计权重值
        for (Map.Entry<String,Integer> server : serverList.entrySet()) {
            cumulativeWeight += server.getValue();
            if (randomWeight < cumulativeWeight) {
                return server.getKey();
            }
        }
        return null; // 没有找到合适的服务器
    }
}
