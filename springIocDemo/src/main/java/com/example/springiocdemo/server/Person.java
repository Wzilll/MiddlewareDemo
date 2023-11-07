package com.example.springiocdemo.server;

/**
 * @BelongsProject: springIocDemo
 * @BelongsPackage: com.example.springiocdemo.server
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-07 21:42
 * @Version: 1.0
 */
//BJ
public class Person {
    private Fruit fruit;



    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    public void printInfo() {
        System.out.println("Person: I am a person.");
    }
}
