package com.example.hystrixdemo.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

/**
 * @BelongsProject: hystrixDemo
 * @BelongsPackage: com.example.hystrixdemo.utils
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-09-21 10:50
 * @Version: 1.0
 */
@Aspect
@Component
public class MyAspect {

    Integer requestNum = 0;
    Date newTime = null;
    Date newDelayTime=null;
    Boolean isDelayTime = false;
    @AfterThrowing(pointcut = "execution(* com.example..*.*(..))",throwing = "ex")
    public Object afterThrowing(JoinPoint joinPoint,Exception ex) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method1 = methodSignature.getMethod();
            if (method1.isAnnotationPresent(MyHystrixCommand.class)) {
                MyHystrixCommand annotation = method1.getAnnotation(MyHystrixCommand.class);
                MyHystrixProperty[] myHystrixProperties = annotation.commandProperties();

                if (myHystrixProperties.length == 0) {
                    Method method2 = joinPoint.getTarget().getClass().getMethod(annotation.fallbackMethod());
                    return method2.invoke(joinPoint.getTarget());
                } else {
                    if (newDelayTime != null && new Date().compareTo(newDelayTime)>=0) {
                        isDelayTime=false;
                        newTime = null;
                        newDelayTime=null;
                    }
                    if (isDelayTime) {
                        Method method2 = joinPoint.getTarget().getClass().getMethod(annotation.fallbackMethod());
                        method2.invoke(joinPoint.getTarget());
                    } else {
                        String numValue = null;
                        String timeValue = null;
                        String errorRateValue = null;
                        String delayTimeValue = null;

                        for (MyHystrixProperty property : myHystrixProperties) {

                            if (property.name().equals("requestNum")) {
                                numValue = property.value();
                            } else if (property.name().equals("requestTime")) {
                                timeValue = property.value();
                            } else if (property.name().equals("requestErrorRate")) {
                                errorRateValue = property.value();
                            } else if (property.name().equals("requestDelayTime")) {
                                delayTimeValue = property.value();
                            }
                        }
                        requestNum++;

                        if (newTime==null){
                            // 创建Calendar对象，并设置为当前时间
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date());
                            // 将Calendar对象的毫秒数加上任意值（这里假设增加1000毫秒）
                            calendar.add(Calendar.MILLISECOND, Integer.valueOf(timeValue));
                            newTime=calendar.getTime();
                        }
                        if (new Date().compareTo(newTime) >=0){
                            if (requestNum >= Integer.valueOf(numValue)) {
                                double i = Double.valueOf(numValue) / requestNum * 100;
                                if (i >= Integer.valueOf(errorRateValue)) {
                                    isDelayTime = true;
                                    if (newDelayTime==null){
                                        // 创建Calendar对象，并设置为当前时间
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(new Date());
                                        // 将Calendar对象的毫秒数加上任意值（这里假设增加1000毫秒）
                                        calendar.add(Calendar.MILLISECOND, Integer.valueOf(delayTimeValue));
                                        newDelayTime=calendar.getTime();
                                    }
                                    requestNum = 0;
                                    Method method2 = joinPoint.getTarget().getClass().getMethod(annotation.fallbackMethod());
                                    return method2.invoke(joinPoint.getTarget());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("通过异常进入到AOP中，调用了托底方法");

        }
        return null;
    }
}
