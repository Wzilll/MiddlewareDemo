package com.ge;

/**
 * @BelongsProject: rpc-demo
 * @BelongsPackage: com.ge
 * @Author: gepengjun
 * @CreateTime: 2023-09-23  10:48
 * @Description: TODO
 * @Version: 1.0
 */
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * @author : pankun
 * @Version : 1.0
 **/
public class RpcClient implements InvocationHandler {

    private Class<?> clazz;
    public RpcClient(Class<?> clazz){
        this.clazz = clazz;
    }

    // 动态代理执行相应业务逻辑
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(Object.class.equals(method.getDeclaringClass())){
            // 如果当前就是一个实现类
            return method.invoke(proxy,args);
        }else{
            return rpcInvoke(method,args);
        }
    }

    private Object rpcInvoke(Method method, Object[] args) {
        MyInvokerProtocol request = new MyInvokerProtocol();
        request.setClassName(this.clazz.getName()); // 类名称
        request.setMethodName(method.getName()); // 方法名称
        request.setParames(method.getParameterTypes()); // 入参列表
        request.setValues(args); // 实参列表

        // TCP 远程调用
        final RpcClientHandler consumerHandler = new RpcClientHandler();

        NioEventLoopGroup work = new NioEventLoopGroup();

        Bootstrap server = new Bootstrap();
        server.group(work)
                .channel(NioSocketChannel.class)// 客户端管道
                .option(ChannelOption.TCP_NODELAY, true) // 开启
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        //自定义协议编码器
                        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                        //对象参数类型编码器
                        pipeline.addLast("encoder", new ObjectEncoder());
                        //对象参数类型解码器
                        pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                        pipeline.addLast("handler",consumerHandler);
                    }
                });
        ChannelFuture future = null;
        try {
            future = server.connect("localhost", 8082).sync();
            future.channel().writeAndFlush(request).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            work.shutdownGracefully();
        }
        return consumerHandler.getResponse();
    }
}


