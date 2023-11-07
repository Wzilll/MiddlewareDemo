package com.ge;



import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


/**
 * @BelongsProject: rpc-demo
 * @BelongsPackage: com.ge
 * @Author: gepengjun
 * @CreateTime: 2023-09-23  10:47
 * @Description: TODO
 * @Version: 1.0
 */
public class RpcServer {

    // 暴露端口
    private  int port;

    public RpcServer(int port){
        this.port = port;
    }

    /**
     * 开启服务接口监听
     * 调用 netty 相关API实现接口监听
     */
    public void listen(){
        // Boss线程 (Selector核心)
        NioEventLoopGroup boss = new NioEventLoopGroup();
        // Work线程 (工作线程)
        NioEventLoopGroup work = new NioEventLoopGroup();

        // 1. 建立服务
        ServerBootstrap server = new ServerBootstrap();
        // 2. 注入 Boos/Worker
        server.group(boss,work)
                .channel(NioServerSocketChannel.class) // 3. 管道执行 keys 轮询的核心
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel channel) throws Exception {
                        // 5. 对流数据进行解析
                        ChannelPipeline pipeline = channel.pipeline();
                        // 6. 自定义协议解码器 (取决于自己定义的规则对象)
                        /** 入参有5个，分别解释如下
                         *  maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出TooLongFrameException。
                         *  lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
                         *  lengthFieldLength：长度字段的长度。如：长度字段是int型表示，那么这个值就是4（long型就是8）
                         *  lengthAdjustment：要添加到长度字段值的补偿值
                         *  initialBytesToStrip：从解码帧中去除的第一个字节数
                         */
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        //自定义协议编码器
                        pipeline.addLast(new LengthFieldPrepender(4));
                        // 7. 参数解析
                        //对象参数类型编码器
                        pipeline.addLast("encoder",new ObjectEncoder());
                        //对象参数类型解码器
                        pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                        // 8. 执行业务逻辑
                        pipeline.addLast(new RpcServerHandler());
                    }
                }) // 4. 子线程 执行对应的业务逻辑
                .option(ChannelOption.SO_BACKLOG,128) // 主线程最大连接数
                .childOption(ChannelOption.SO_KEEPALIVE,true); // 子线程持续

        try {
            // 服务绑定端口
            ChannelFuture future = server.bind(port).sync();
            System.out.println("RPC start success, listen port is :" +  port + " !!");
            future.channel().closeFuture().sync(); // 回调
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }




}


