package com.example.loadserviceb;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@SpringBootApplication
public class LoadServiceBApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadServiceBApplication.class, args);


    }

    @RabbitListener(queuesToDeclare = @Queue(name="test_wzl"))
    public void listenTestMq(String message){
        System.out.println("消费了消息");
        long stime = System.nanoTime();
        for (int i =0; i<100000;i++){
            System.out.println("第"+i+"次循环");

        }
        long etime = System.nanoTime();
        System.out.printf("执行时长：%d 毫秒.", (etime - stime)/1000000);
    }

}
