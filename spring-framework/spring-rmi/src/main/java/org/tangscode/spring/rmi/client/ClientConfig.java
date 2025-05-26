// 文件路径：com/example/client/ClientConfig.java
package org.tangscode.spring.rmi.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.tangscode.spring.rmi.HelloService;

@Configuration
public class ClientConfig {

    @Bean
    public RmiProxyFactoryBean helloService() {
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceUrl("rmi://localhost:1099/HelloService"); // 服务地址
        factory.setServiceInterface(HelloService.class);          // 服务接口
        return factory;
    }
}