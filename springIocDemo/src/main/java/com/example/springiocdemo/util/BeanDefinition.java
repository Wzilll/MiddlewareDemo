package com.example.springiocdemo.util;

/**
 * @BelongsProject: springIocDemo
 * @BelongsPackage: com.example.springiocdemo.util
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-08 10:15
 * @Version: 1.0
 */

public class BeanDefinition {

    private Class beanClass;

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

}
