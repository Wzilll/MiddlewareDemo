package com.example.openfeigndemo.config;

import com.example.openfeigndemo.annotation.MyFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: OpenFeignDemo
 * @BelongsPackage: com.example.openfeigndemo.config
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-18 22:11
 * @Version: 1.0
 */
@Component
public class FeignConfig implements ApplicationRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FeignClientFactory feignClientFactory;

    //用来存储扫描
    List<String> javaFiles = new ArrayList<>();
    @Override
    public void run(ApplicationArguments args) {
        try{
            Environment environment = applicationContext.getEnvironment();
            //要扫描的包路径
            System.out.println("ribbon"+environment.getProperty("ribbon.loadBalanceName"));
            String basePackage = environment.getProperty("spring.feign.clients.basePackage");
            String packagePath = basePackage.replace(".", "/");
            System.out.println("看看这个是不是到java的路径"+System.getProperty("user.dir"));
            String javaClassPath=System.getProperty("user.dir")+"/src/main/java/"+packagePath;
            File file = new File(javaClassPath);
            //判断是不是文件夹
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (int i=0; i<files.length;i++){
                    String fileNamePath=files[i].getAbsolutePath();
                    if (fileNamePath.endsWith(".java")){
                        javaFiles.add(basePackage+"."+files[i].getName().substring(0,files[i].getName().lastIndexOf(".")));

                    }
                }
            }
            }catch (Exception e){
            e.printStackTrace();
        }
    }


    public Object getAgentObject(){
        try{
            for (String javaFile:javaFiles){
                Class<?> interfaceClass = Class.forName(javaFile);
                if (interfaceClass.isAnnotationPresent(MyFeignClient.class)){
                   return  feignClientFactory.create(interfaceClass);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
