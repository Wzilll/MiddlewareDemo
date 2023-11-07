package com.ge;

/**
 * @BelongsProject: rpc-demo
 * @BelongsPackage: com.ge
 * @Author: gepengjun
 * @CreateTime: 2023-09-23  10:44
 * @Description: TODO
 * @Version: 1.0
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    // 注册中心 (容器)
    public static ConcurrentHashMap<String,Object> context = new ConcurrentHashMap<String,Object>();
    // 类信息集合
    private List<String> classNames = new ArrayList<String>();


    // 构造器初始化
    public RpcServerHandler(){
        // 包扫描信息
        scannerClass("com.ge.service");
        // 注册容器
        doRegistry();
    }


    // 递归包扫描
    private void scannerClass(String packageName) {
        // 获取类加载器
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            //如果是一个文件夹，继续递归
            if(file.isDirectory()){
                scannerClass(packageName + "." + file.getName());
            }else{
                classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    // 注册
    private void doRegistry() {
        if(classNames.isEmpty()){return;}
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> anInterface = clazz.getInterfaces()[0]; // 因为此demo默认实现一个接口  所以此处写死获取当前第一个接口信息
                context.put(anInterface.getName(),clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 请求到达  执行执行的业务逻辑
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        MyInvokerProtocol request = (MyInvokerProtocol) msg; // netty 会按照我们自定义的策略进行转换
        // 判断当前调用服务在容器中是否真正存在
        if(context.containsKey(request.getClassName())){
            // 确实存在执行对应的业务逻辑
            Object clazz = context.get(request.getClassName());
            // 获取真正执行的
            Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParames());
            result = method.invoke(clazz, request.getValues());
        }
        if(result != null){
            ctx.write(result);
        }
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}

