// 文件路径：com/example/client/ClientApp.java
package org.tangscode.spring.rmi.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.tangscode.spring.rmi.HelloService;

import java.rmi.RemoteException;

public class ClientApp {
    public static void main(String[] args) throws RemoteException {
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(ClientConfig.class);
        HelloService helloService = context.getBean(HelloService.class);
        String result = helloService.sayHello("World");
        System.out.println("Client received: " + result);
        context.close();
    }
}