`org.apache.dubbo.remoting.transport.netty4.NettyServer`
```java
// Boss线程组只有一个，负责创建连接
protected EventLoopGroup createBossGroup() {
    return NettyEventLoopFactory.eventLoopGroup(1, "NettyServerBoss");
}
// Worker线程组
//  Worker线程池默认大小：默认值（核心数+1)和32取较小值。 int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);
//  size：取属性iothreads和可用核心数+1的较小值。如果按个Provider实例的可用核心数超过32，设置该参数，否则达不到最大吞吐
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
```
**Netty的可扩展性主要来自其核心组件之一ChannelHandler，ChannelHandler能帮助我们解决拆包&粘包、协议编解码、权限校验等RPC常遇到的问题。**

> 关键知识：
> 1. netty每个连接对应一个Channel，每个Channel对应一个ChannelPipeline：用来管理Channel上的ChannelHandler（责任链）
> 2. ChannelHandler有ChannelInboundHandler和ChannelOutboundHandler两个子类，分别表示入站（读）和出站（写）的流程编写
> 3. Netty提供了很多开箱即用的ChannelHandler，方便实现HTTP，WebSocket等协议

`SslServerTlsHandler`
- ByteToMessageDecoder: 将接收的字节流解码为消息对象, 将解码后的消息传递给下一个Handler
- SslServerTlsHandler继承了它，可以方便处理多种，二进制文本数据
```java
public class SslServerTlsHandler extends ByteToMessageDecoder {
    
    /*
     * 在其实现的decode方法中，如果发现ByteBuf是支持SSL|TLS，那么会立马在当前Channel的ChannelPipeline中加入SslHandler（Netty自带的支持SSL|TLS握手的ChannelHandler），
     * 注意，是加载当前SslServerTlsHandler之后。为了能在SSL|TLS解码后能继续执行SslServerTlsHandler的逻辑，
     * 在SslHandler后面又新加了一个SslServerTlsHandler（其中sslDetected=true，说明数据包到这里已经解包完成），用于校验协议。
     * */
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // TLS 协议的数据传输基于记录层（Record Layer，介于传输层和应用层之间），每个记录层数据包的头部由 5 字节（1 字节：内容类型，2 字节：协议版本，2 字节：数据长度）
        if (byteBuf.readableBytes() >= 5) {
            // SSL|TSL 校验完成后会创建新的SslServerTlsHandler
            if (!this.sslDetected) {
                CertManager certManager = (CertManager)this.url.getOrDefaultFrameworkModel().getBeanFactory().getBean(CertManager.class);
                ProviderCert providerConnectionConfig = certManager.getProviderConnectionConfig(this.url, channelHandlerContext.channel().remoteAddress());
                ChannelPipeline p;
                if (providerConnectionConfig == null) {
                    p = channelHandlerContext.pipeline();
                    p.remove(this);
                } else if (this.isSsl(byteBuf)) {
                    SslContext sslContext = SslContexts.buildServerSslContext(providerConnectionConfig);
                    this.enableSsl(channelHandlerContext, sslContext);
                } else {
                    if (providerConnectionConfig.getAuthPolicy() == AuthPolicy.NONE) {
                        p = channelHandlerContext.pipeline();
                        p.remove(this);
                    }

                    logger.error("99-0", "", "", "TLS negotiation failed when trying to accept new connection.");
                    channelHandlerContext.close();
                }
            }
        }
    }
}
```

`NettyCodecAdapter`
- NettyCodecAdapter包含编码器和解码器
- 解码器：InternalDecoder，也是继承了ByteToMessageDecoder(将接收的字节流解码为消息对象)。
  - DubboCountCodec：用来处理
```java
private class InternalDecoder extends ByteToMessageDecoder {
        private InternalDecoder() {
        }

        protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
            // ChannelBuffer是Dubbo对通道缓冲区的抽象，NettyBackedChannelBuffer使用的是ByteBuf来读写
            ChannelBuffer message = new NettyBackedChannelBuffer(input);
            // NettyChannel是衔接Netty Channel与Dubbo内部通道抽象的桥梁 ，内部使用了Dubbo的编码器Codec2
            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), NettyCodecAdapter.this.url, NettyCodecAdapter.this.handler);

            while(true) {
                int saveReaderIndex = message.readerIndex();
                // 这里的codec默认是DubboCountCodec，支持MultiMessage解码，这里返回的msg已经是org.apache.dubbo.remoting.exchange.Request对象
                Object msg = NettyCodecAdapter.this.codec.decode(channel, message);
                if (msg == DecodeResult.NEED_MORE_INPUT) {
                    message.readerIndex(saveReaderIndex);
                } else {
                    if (saveReaderIndex == message.readerIndex()) {
                        throw new IOException("Decode without read data.");
                    }

                    if (msg != null) {
                        out.add(msg);
                    }

                    if (message.readable()) {
                        continue;
                    }
                }

                return;
            }
        }
    }
```


