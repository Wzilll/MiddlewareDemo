package com.example;

import com.example.openfeigndemo.annotation.MyEnableFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MyEnableFeignClients
public class UserAServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAServiceApplication.class, args);
    }

}
