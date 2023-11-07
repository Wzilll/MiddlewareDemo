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
public class Fruit {

    private Person person;



    public void setPerson(Person person) {
        this.person = person;
    }

    public void printInfo() {
        System.out.println("Fruit: I am a fruit.");
    }
}
