package com.example.loadserviceb.Controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: nginxDemo
 * @BelongsPackage: com.example.loadserviceb.Controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-19 11:28
 * @Version: 1.0
 */

@Component
public class test {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void test(String queueName,String message){
        rabbitTemplate.convertAndSend(queueName,message);
    }
}
