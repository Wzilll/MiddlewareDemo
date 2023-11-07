package com.ge;

/**
 * @BelongsProject: rpc-demo
 * @BelongsPackage: com.ge
 * @Author: gepengjun
 * @CreateTime: 2023-09-23  14:35
 * @Description: TODO
 * @Version: 1.0
 */
public class IRPCProviderClient {
    public static void main(String[] args) {
        new RpcServer(8082).listen();
    }
}
