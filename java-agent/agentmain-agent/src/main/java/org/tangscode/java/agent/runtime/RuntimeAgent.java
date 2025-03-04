package org.tangscode.java.agent.runtime;

import java.lang.instrument.Instrumentation;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/4
 */
public class RuntimeAgent {
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("----------哈哈，我是RuntimeAgent");
        System.out.println("----------RuntimeAgent agentArgs = " + agentArgs);
    }
}
