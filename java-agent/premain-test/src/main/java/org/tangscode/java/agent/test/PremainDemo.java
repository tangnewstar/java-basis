package org.tangscode.java.agent.test;

import java.io.IOException;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/4
 */

public class PremainDemo {
    public void execute() {
        try {
            Thread.sleep(1000); // 模拟业务逻辑
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PremainDemo().execute();
    }
}

