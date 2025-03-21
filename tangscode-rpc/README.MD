
```mermaid
timeline
    title Java RPC框架发展里程碑
    section 萌芽期(2000s)
        2001 : RMI(Java原生远程调用)
        2004 : Hessian(二进制协议)
        2006 : XML-RPC(WebService)
    section 发展期(2010s)
        2011 : Dubbo(阿里开源)
        2012 : Thrift(Facebook跨语言方案)
        2015 : gRPC(Google推出)
    section 微服务时代
        2016 : Spring Cloud Feign
        2018 : Dubbo 2.7(适配云原生)
    section 云原生时代
        2020 : Dubbo 3.0(应用级服务发现)
        2022 : gRPC 1.47(支持Java 17)
```

```mermaid
graph LR
    A[项目需求] --> B{是否需要跨语言?}
    B -->|是| C[gRPC/Thrift]
    B -->|否| D{是否需要强治理?}
    D -->|是| E[Dubbo]
    D -->|否| F{是否需要快速集成?}
    F -->|是| G[Spring Cloud OpenFeign]
    F -->|否| H[Hessian等轻量方案]

```

```mermaid
graph LR
  A[请求线程] --> B{对象池}
  B -->|获取| C[复用ByteBuffer]
  C --> D[网络写入]
  D --> E[归还对象池]

```

主流框架能力矩阵

| 框架          | 协议支持            | 性能(QPS) | 跨语言 | 服务治理         | 典型应用场景       |
|-------------|-------------------|---------|------|--------------|----------------|
| Dubbo       | Dubbo/Triple/gRPC | 35万+   | 部分   | 完整(限流/熔断)  | 电商交易系统       |
| gRPC        | HTTP/2 + Protobuf | 50万+   | 全支持 | 依赖sidecar    | 物联网数据采集     |
| Thrift      | Thrift Binary     | 28万+   | 全支持 | 无原生支持       | 跨语言数据交换     |
| Spring Cloud| HTTP/JSON         | 8万+    | 有限   | Hystrix集成    | 传统微服务改造     |
| Hessian     | Hessian2 Binary   | 15万+   | 部分   | 需扩展实现       | 遗留系统集成       |

| 场景                   | Dubbo 3.2 | gRPC 1.54 | 优势幅度 |
|------------------------|-----------|-----------|----------|
| 1MB订单对象序列化      | 4.2ms     | 6.8ms     | +38%     |
| 并发10万连接内存       | 2.1GB     | 4.3GB     | +51%     |
| 长连接保活开销         | 0.3% CPU  | 1.2% CPU  | 4倍优势  |

#### Netty VS. Tomcat
| 维度           | Tomcat (NIO模式)         | Netty (Reactor模型)       |
|----------------|--------------------------|---------------------------|
| 线程类型       | 阻塞式请求处理线程池     | 非阻塞EventLoop线程组     |
| 默认线程数     | 200 (maxThreads)         | CPU核心数 × 2             |
| 线程增长策略   | 按需创建直至maxThreads   | 固定大小                  |
| 单线程承载量   | 约1-2k QPS/线程          | 约5-10k QPS/线程          |

性能对比数据 某压测案例（4核8G环境）：

| 指标         | Tomcat 200线程 | Netty 8线程 |
|--------------|----------------|-------------|
| 最大连接数   | 5,000          | 50,000      |
| 吞吐量(QPS)  | 12,000         | 68,000      |
| 内存占用     | 1.2GB          | 480MB       |
| 95\%延迟     | 45ms           | 18ms        |

Netty理想线程数 = CPU可用核心数 × (1 + 平均I/O等待时间比例)
例如：4核CPU，I/O密集型应用（等待时间占比50%）
推荐线程数 = 4 × (1 + 0.5) = 6

Netty以更少线程实现更高性能的核心原因在于其非阻塞式事件驱动架构与高效的线程模型设计
#### 线程模型本质差异
| 特性           | Tomcat线程模型（阻塞式）                          | Netty线程模型（非阻塞式）                      |
|----------------|--------------------------------------------------|-----------------------------------------------|
| 线程工作方式   | 每个线程全程独占式处理请求（连接→读→处理→写）    | 线程仅触发事件回调，I/O操作完全异步            |
| 线程阻塞点     | 在读取请求体、业务逻辑执行、网络响应时都会阻塞    | 仅在执行同步代码时占用线程（约95\%时间线程可复用） |
| 内存消耗       | 每个线程需要独立的栈内存（通常1MB/线程）         | 共享的EventLoop线程，内存占用与连接数无关     |
| 上下文切换成本 | 高（200线程在高并发时频繁切换）                  | 极低（16线程可承载数万连接）                  |

> 案例：处理10k并发请求时，Tomcat需要约200个线程（约200MB栈内存），而Netty仅需8个EventLoop线程（约8MB内存）。

事件驱动机制解析
Netty的Reactor模式三层处理流水线：

```mermaid
graph TD;
    A[Boss EventLoop <1 thread>] --> |Accept| B[Worker EventLoop < N thread,  N = CPU * 2> ];
    B -->|Read| C[ChannelHandler 业务逻辑处理 ];
```

连接接入阶段：Boss线程仅处理TCP握手，耗时约3μs（微秒级）
I/O就绪阶段：Worker线程通过epoll检测就绪的Channel（零拷贝）
业务处理阶段：用户代码在ChannelHandler中执行，若包含阻塞操作会破坏模型

```mermaid
sequenceDiagram
    participant Client as 客户端
    participant NettyWorker as Netty Worker线程
    participant DubboDispatcher as Dubbo分发器(dispatcher)
    participant DubboBizPool as Dubbo业务线程池

    Client ->> NettyWorker: 发送请求（I/O线程处理解码）
    NettyWorker ->> DubboDispatcher: 将请求对象传递给分发器
    alt dispatcher=all
        DubboDispatcher ->> DubboBizPool: 提交到业务线程池
    else dispatcher=direct
        DubboDispatcher ->> NettyWorker: 直接在I/O线程执行（风险！）
    end
    DubboBizPool -->> Client: 返回响应（业务线程处理编码）

```