package org.tangscode.rpc.dubbo;

import org.apache.dubbo.common.threadpool.support.limited.LimitedThreadPool;
import org.apache.dubbo.remoting.http.tomcat.TomcatHttpServer;
import org.apache.dubbo.remoting.transport.netty4.NettyServer;
import reactor.netty.http.server.HttpServer;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/5
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("dubbo 版本 3.2.8");
        System.out.println("dubbo Worker线程池默认大小：" + Math.min(Runtime.getRuntime().availableProcessors() + 1, 32));
    }
}
