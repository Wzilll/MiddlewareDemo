package com.ge;




import java.lang.reflect.Proxy;

/**
 * @BelongsProject: rpc-demo
 * @BelongsPackage: com.ge
 * @Author: gepengjun
 * @CreateTime: 2023-09-23  14:30
 * @Description: TODO
 * @Version: 1.0
 */
public class RpcProxy {

    public static <T> T invoke(Class<T> clazz){
        Class<?> [] interfaces = clazz.isInterface() ?
                new Class[]{clazz} :
                clazz.getInterfaces();
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,new RpcClient(clazz));
        return result;
    }

}


