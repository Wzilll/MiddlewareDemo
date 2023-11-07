package com.ge;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: rpc-demo
 * @BelongsPackage: com.ge
 * @Author: gepengjun
 * @CreateTime: 2023-09-23  10:46
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class MyInvokerProtocol implements Serializable {

    private String className;//类名
    private String methodName;//函数名称
    private Class<?>[] parames;//形参列表
    private Object[] values;//实参列表

}
