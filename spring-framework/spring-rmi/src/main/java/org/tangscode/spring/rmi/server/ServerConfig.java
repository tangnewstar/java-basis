
package org.tangscode.spring.rmi.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.tangscode.spring.rmi.HelloService;

@Configuration
@ComponentScan(basePackages = "org.tangscode.spring.rmi.server")
public class ServerConfig {

    // 导出 RMI 服务
    @Bean
    public RmiServiceExporter rmiServiceExporter(HelloService helloService) {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("HelloService");       // 服务名称
        exporter.setService(helloService);            // 服务实现
        exporter.setServiceInterface(HelloService.class); // 服务接口
        exporter.setRegistryPort(1099);               // RMI 注册表端口
        return exporter;
    }
}