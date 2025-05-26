package org.tangscode.spring.rmi.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RMIServerSourceCode {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("rmi/RMISourceCode.xml");
    }
}
