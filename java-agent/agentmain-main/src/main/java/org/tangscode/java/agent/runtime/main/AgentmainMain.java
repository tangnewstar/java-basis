package org.tangscode.java.agent.runtime.main;

import java.io.IOException;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/4
 */
public class AgentmainMain {
    public static void main(String[] args) {
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
