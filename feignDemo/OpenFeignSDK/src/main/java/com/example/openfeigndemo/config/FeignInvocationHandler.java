package com.example.openfeigndemo.config;

import com.example.ribbonsdk.config.test.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @BelongsProject: OpenFeignSDK
 * @BelongsPackage: com.example.openfeigndemo.config
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-26 09:32
 * @Version: 1.0
 */

public class FeignInvocationHandler implements InvocationHandler {


    private  String  baseUrl;

    private RestTemplate restTemplate =new RestTemplate();

    @Autowired
    private AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext();


    public FeignInvocationHandler(String baseUrl){
        this.baseUrl=baseUrl;
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String url = baseUrl + method.getAnnotation(GetMapping.class).value()[0]; // 获取请求URL
        // 获取方法参数上的注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if(args!=null){
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof PathVariable) {
                        PathVariable pathVariable = (PathVariable) annotation;
                        url = url.replace("{" + pathVariable.value() + "}", args[i].toString());
                    } else if (annotation instanceof RequestParam) {
                        RequestParam requestParam = (RequestParam) annotation;
                        String paramName = requestParam.value();
                        String paramValue = args[i].toString();
                        url = url + "?" + paramName + "=" + paramValue;
                    }
                }
            }
        }
        //给请求添加拦截器。
        restTemplate.getInterceptors().add(new RequestInterceptor());
        // 使用 RestTemplate 发送 HTTP 请求
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        if (responseEntity.getStatusCode()== HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Failed to invoke API: " + responseEntity.getStatusCode());
        }
    }
}
