`org.apache.dubbo.remoting.transport.netty4.NettyServer`
```java
// Boss线程组只有一个，负责创建连接
protected EventLoopGroup createBossGroup() {
    return NettyEventLoopFactory.eventLoopGroup(1, "NettyServerBoss");
}
// Worker线程组
//  size：取属性iothreads和默认值（核心数+1，如果核心数超过32则为32）的较小值。
protected EventLoopGroup createWorkerGroup() {
    return NettyEventLoopFactory.eventLoopGroup(this.getUrl().getPositiveParameter("iothreads", Constants.DEFAULT_IO_THREADS), "NettyServerWorker");
}

protected void initServerBootstrap(final NettyServerHandler nettyServerHandler) {
    boolean keepalive = this.getUrl().getParameter("keep.alive", Boolean.FALSE);
    ((ServerBootstrap)((ServerBootstrap)this.bootstrap
            .group(this.bossGroup, this.workerGroup)
            // 根据shouldEpoll（是否支持epoll来选择EpollServerSocketChannel或NioServerSocketChannel）
            .channel(NettyEventLoopFactory.serverSocketChannelClass()))
            // 重用本地地址端口，如果服务器意外关闭后再次启动，可以立即绑定到之前使用的地址和端口。
            .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE))
            // Nagle算法，将小的网络包合并成较大的网络包提升网络效率，但会增加传输延迟。一般对延迟敏感的场景都会禁用Nagle算法。
            .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
            // TCP Keep-Alive，默认为false
            .childOption(ChannelOption.SO_KEEPALIVE, keepalive)
            // PooledByteBufAllocator是Netty提供的一种内存分配器实现，它可以重用ByteBuf对象的内存，提高内存的利用率和性能。
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childHandler(new ChannelInitializer<SocketChannel>() {
        protected void initChannel(SocketChannel ch) throws Exception {
            int closeTimeout = UrlUtils.getCloseTimeout(NettyServer.this.getUrl());
            NettyCodecAdapter adapter = new NettyCodecAdapter(NettyServer.this.getCodec(), NettyServer.this.getUrl(), NettyServer.this);
            // 处理https
            ch.pipeline().addLast("negotiation", new SslServerTlsHandler(NettyServer.this.getUrl()));
            ch.pipeline()
                    // 解码 读数据
                    .addLast("decoder", adapter.getDecoder())
                    // 编码 写数据
                    .addLast("encoder", adapter.getEncoder())
                    // IdleStateHandler
                    .addLast("server-idle-handler", new IdleStateHandler(0L, 0L, (long)closeTimeout, TimeUnit.MILLISECONDS))
                    // netty核心，实现了ChannelDuplexHandler，双工通信
                    .addLast("handler", nettyServerHandler);
        }
    });
}

public static Class<? extends ServerSocketChannel> serverSocketChannelClass() {
    return shouldEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
}

// 关键知识
// netty每个连接对应一个Channel，每个Channel对应一个ChannelPipeline：用来管理Channel上的ChannelHandler（责任链）
// ChannelHandler有ChannelInboundHandler和ChannelOutboundHandler两类，分别表示入站（读）和出站（写）的流程编写
// Netty提供了很多开箱即用的ChannelHandler，方便实现HTTP，WebSocket等协议

// ByteToMessageDecoder: 将接收的字节流解码为消息对象, 将解码后的消息传递给下一个Handler
// SslServerTlsHandler继承了它，可以方便处理多种，二进制文本数据
public class SslServerTlsHandler extends ByteToMessageDecoder { 
    
}
```

