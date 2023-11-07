package com.example.springiocdemo;

import com.example.springiocdemo.util.ApplicationContext;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//SM(com.example.springiocdemo.server)
public class SpringIocDemoApplication {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ApplicationContext applicationContext=new ApplicationContext();
        applicationContext.run();
        Object person = applicationContext.getBean("person");
        Method method = person.getClass().getMethod("printInfo");
        method.invoke(person);
    }

}
