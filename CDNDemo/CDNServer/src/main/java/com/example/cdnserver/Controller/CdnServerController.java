package com.example.cdnserver.Controller;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: CDNServer
 * @BelongsPackage: com.example.cdnserver.Controller
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-06-30 20:56
 * @Version: 1.0
 */
@RestController
@RequestMapping("/cdnServer")
public class CdnServerController {

    @Value("${server.port}")
    private String serverPort;

    private List<String> cityIPs=new ArrayList<>(){{
        //需要改成各个地方服务器的ip地址
        add("152.136.111.77");
        add("81.70.2.8");
        add("43.143.230.23");
        add("39.105.96.62");
    }};


    // 地球半径（单位：千米）
    private static final double EARTH_RADIUS = 6371;
    /**
    * @Author:Wuzilong
    * @Description: 返回通过cdn加工之后的域名和cdn 调度中心的ip地址
    * @CreateTime: 2023/7/1 9:52
    * @param:  需简要加速的域名
    * @return:  返回通过cdn加工之后的域名和cdn 调度中心的ip地址
    **/
    @GetMapping("/addDomainName")
    public Map<String,String> addDomainName(String domainName) throws UnknownHostException {
        System.out.println("请求加速的域名为"+domainName);
        Map<String,String> CDNInfo=new HashMap<>();
        domainName=domainName+".cdn";
        CDNInfo.put(domainName, InetAddress.getLocalHost().getHostAddress()+":"+serverPort);
        System.out.println("加速后的域名为"+domainName);
        return CDNInfo;
    }


    @GetMapping("/getNearbyIpAddress")
    public String getNearbyIpAddress() throws IOException, GeoIp2Exception {
        // 创建一个指向GeoIP2数据库文件的File对象
        File database = new File("C:\\Users\\Administrator\\Desktop\\city\\GeoLite2-City_20230704\\GeoLite2-City.mmdb");

        // 使用数据库文件创建一个DatabaseReader对象，用于查询城市的地理位置信息
        DatabaseReader reader = new DatabaseReader.Builder(database).build();
        //获取请求方的ip地址
        String remoteAddr="222.222.111.239";

        // 查询单独的IP地址的地理位置信息
        InetAddress ip = InetAddress.getByName(remoteAddr);
        CityResponse response = reader.city(ip);
        // 获取城市的经纬度坐标
        Location location1 = response.getLocation();
        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        String nearestIp="";
        double minDistance =Double.MAX_VALUE;
        // 遍历各个城市的IP地址，计算距离并更新最小距离和对应的城市
        for (String cityIP : cityIPs) {
            // 初始化最小距离和对应的城市
            InetAddress cityIpAddress = InetAddress.getByName(cityIP);
            CityResponse cityResponse = reader.city(cityIpAddress);
            Location location2 = cityResponse.getLocation();
            double lat2 = Math.toRadians(location2.getLatitude());
            double lon2 = Math.toRadians(location2.getLongitude());

            double dlon = lon2 - lon1;
            double dlat = lat2 - lat1;

            double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                            Math.sin(dlon / 2) * Math.sin(dlon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            double distance = EARTH_RADIUS * c;

            //System.out.println("和"+cityResponse.getCity().getName()+"的距离是：" + cityIP+",距离是："+distance);

            if (distance < minDistance) {
                minDistance = distance;
                nearestIp = cityIP;
            }

        }
        System.out.println("距离最短的城市对应的IP是：" + nearestIp);
        return nearestIp+":8004";
    }
}
