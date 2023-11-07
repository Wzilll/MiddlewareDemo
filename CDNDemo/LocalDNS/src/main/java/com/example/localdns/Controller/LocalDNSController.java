package com.example.localdns.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: LocalDNS
 * @BelongsPackage: com.example.localdns.Controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-06-30 10:49
 * @Version: 1.0
 */
@RestController
@RequestMapping("/localDNS")
public class LocalDNSController {


        @Value("${server.cdn}")
        private String cdnServer;

        private Map<String, String> localDNS = new HashMap<>(){{
            put("www.wzl.com","localhost:9099");
        }};

        /**
        * @Author:Wuzilong
        * @Description: 将服务添加到CDN中，将域名进行加工起到域名加速的作用。并更新本地dns
        * @CreateTime: 2023/6/30 20:34
        * @param:
        * @return:
        **/
        @GetMapping("/addCDNServer")
        public void addCDNServer(String accelerateName) {
           String url="http://"+cdnServer+"/cdnServer/addDomainName?domainName="+accelerateName;
            RestTemplate restTemplate=new RestTemplateBuilder().build();
            ResponseEntity<Map<String,String>> forEntity = restTemplate.exchange(url, HttpMethod.GET, null,new ParameterizedTypeReference<Map<String, String>>() {});
            if (forEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, String> body = forEntity.getBody();
                localDNS.putAll(body);
                localDNS.put(accelerateName, String.valueOf(body.keySet()).replaceAll("\\[|\\]", ""));
                System.out.println("已经将加速后的域名添加到本地DNS"+body);
            }
        }


        /**
        * @Author:Wuzilong
        * @Description: 获取域名的ip地址，通过cdn加速的域名获取之后还是一个域名那么需要再次解析域名直到得到ip地址为止
        * @CreateTime: 2023/7/1 10:27
        * @param:  域名
        * @return:  ip地址
        **/
        public String getIpAddress(String domainName){
            Boolean isRun=true;
            String ipAddress = localDNS.get(domainName);
            if (!ipAddress.contains(":")){

                ipAddress=this.getIpAddress(ipAddress);
                isRun=false;
            }
            if (isRun){
                String url="http://"+ipAddress+"/cdnServer/getNearbyIpAddress";
                RestTemplate restTemplate=new RestTemplateBuilder().build();
                ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
                if (forEntity.getStatusCode() == HttpStatus.OK) {
                    System.out.println("获取到了距离最近的ip地址，为："+forEntity.getBody());
                    ipAddress=forEntity.getBody();

                }
            }
            return ipAddress;
        }
    }

