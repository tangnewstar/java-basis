// 文件路径：com/example/server/ServerApp.java
package org.tangscode.spring.rmi.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(ServerConfig.class);
        System.out.println("RMI Server started on port 1099");
        // 保持应用运行
        try { Thread.currentThread().join(); } 
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}