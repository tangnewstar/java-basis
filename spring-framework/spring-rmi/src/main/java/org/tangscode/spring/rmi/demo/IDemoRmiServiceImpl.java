package org.tangscode.spring.rmi.demo;

public class IDemoRmiServiceImpl implements IDemoRmiService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}