package com.example.ribbonsdk.config.test;


import com.example.client.Controller.SDKController;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * @BelongsProject: ribbonDemo
 * @BelongsPackage: com.example.ribbonsdk.config
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-31 22:47
 * @Version: 1.0
 */
@Component
public class RequestInterceptor implements ClientHttpRequestInterceptor, ApplicationContextAware {

    public static ApplicationContext applicationContext;

    int index = 0;

    // 目前是写死的，应该放到注册中心中去，动态的添加注册服务和权重
    public Map<String,Integer> serverList = new HashMap<>(){{
        put("localhost:9002",7); // 权重值为7
        put("localhost:9005",3); // 权重值为3
    }};


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.applicationContext == null) {
            this.applicationContext = applicationContext;
        }
    }

    /**
     * @Author:Wuzilong
     * @Description: 手动注入AnnotationConfigApplicationContext用于判断
     * @CreateTime: 2023/6/19 17:36
     * @param:
     * @return:
     **/
    @Bean
    public AnnotationConfigApplicationContext annotationConfigApplicationContext() {
        return new AnnotationConfigApplicationContext();
    }


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        System.out.println("拦截器拦截进来了，拦截的地址是："+request.getURI());
        RestTemplate restTemplate = new RestTemplate();
        //获取服务名
        String serveName = request.getURI().getAuthority();
        String newAuthority = null;
        Environment environment = applicationContext.getBean(Environment.class);
        String loadBalanceName = environment.getProperty("ribbon.loadBalanceName");
        if (loadBalanceName.equals("polling")){
             newAuthority = this.polling(serveName);
            System.out.println("采用的是负载均衡策略————轮询");
        }else if (loadBalanceName.equals("weight")){
            newAuthority = this.weight();
            System.out.println("采用的是负载均衡策略————权重");
        }
        
        String newHost= newAuthority.split(":")[0];
        String newPort= newAuthority.split(":")[1];
        URI newUri = UriComponentsBuilder.fromUri(request.getURI())
                .host(newHost)
                .port(newPort)
                .build()
                .toUri();

        RequestEntity tRequestEntity = new RequestEntity(HttpMethod.GET, newUri);
        ResponseEntity<String> exchange = restTemplate.exchange(tRequestEntity, String.class);
        System.out.println("请求的服务是"+exchange.getBody());


        // 创建一个ClientHttpResponse对象，并将实际的响应内容传递给它
        ClientHttpResponse response = new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() {
                return exchange.getStatusCode();
            }

            @Override
            public int getRawStatusCode() {
                return exchange.getStatusCodeValue();
            }

            @Override
            public String getStatusText() {
                return exchange.getBody();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(exchange.getBody().getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                return exchange.getHeaders();
            }
        };
        return response;
    }



    //轮询获取服务的IP地址
    public  String polling(String serverName){
        List<String> pollingList = applicationContext.getBean(SDKController.class).getList(serverName);
        String ipContext = pollingList.get(index);
        index=(index+1)%pollingList.size();
        return ipContext;
    }



    //权重获取服务的IP地址
    public String weight() {
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
