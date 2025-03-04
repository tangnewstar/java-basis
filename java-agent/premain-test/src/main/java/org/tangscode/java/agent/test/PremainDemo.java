package org.tangscode.java.agent.test;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/4
 */
// com/example/PremainDemo.java

public class PremainDemo {
    public void execute() throws Exception {
        Thread.sleep(1000); // 模拟业务逻辑
    }

    public static void main(String[] args) throws Exception {
        new PremainDemo().execute();
    }
}

