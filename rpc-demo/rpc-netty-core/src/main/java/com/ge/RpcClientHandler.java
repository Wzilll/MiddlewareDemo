package com.ge;



import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @BelongsProject: rpc-demo
 * @BelongsPackage: PACKAGE_NAME
 * @Author: gepengjun
 * @CreateTime: 2023-09-23  14:30
 * @Description: TODO
 * @Version: 1.0
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    private Object response;

    public Object getResponse() {
        return response;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}


