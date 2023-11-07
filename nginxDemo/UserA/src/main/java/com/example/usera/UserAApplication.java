package com.example.usera;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.openqa.selenium.WebDriver;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class UserAApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAApplication.class, args);
        // 需要定时执行的任务
        Runnable runnable = () -> {
            try {
                Runtime.getRuntime().exec("cmd /c start " + "https://blog.csdn.net/weixin_45490198/article/details/131443952");

                Runtime.getRuntime().exec("cmd /c start " + "https://blog.csdn.net/weixin_45490198/article/details/131347146?spm=1001.2014.3001.5502");

                Runtime.getRuntime().exec("cmd /c start " + "https://blog.csdn.net/weixin_45490198/article/details/125119932");

                Runtime.getRuntime().exec("cmd /c start " + "https://blog.csdn.net/weixin_45490198/article/details/131256597?spm=1001.2014.3001.5501");

                Runtime.getRuntime().exec("cmd /c start " + "https://blog.csdn.net/weixin_45490198/article/details/131530373?spm=1001.2014.3001.5502");

            } catch (Exception e) {
                System.out.println("暂不支持该运算");
            }
        };

        Runnable runnable1 = () -> {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM msedge.exe");
            } catch (Exception e) {
                System.out.println("暂不支持该运算");
            }
        };
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        //立即执行，并且每5秒执行一次
        ses.scheduleAtFixedRate(runnable, 0, 65000, TimeUnit.MILLISECONDS);
        ses.scheduleAtFixedRate(runnable1, 0, 300000, TimeUnit.MILLISECONDS);
    }

}
