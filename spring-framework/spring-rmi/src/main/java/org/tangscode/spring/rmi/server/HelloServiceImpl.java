package org.tangscode.spring.rmi.server;

import org.springframework.stereotype.Service;
import org.tangscode.spring.rmi.HelloService;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}