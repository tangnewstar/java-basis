package org.tangscode.java.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/4
 */
public class ExceptionAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println(">>>>>>>>>>>>>> this is ExceptionAgent");
        System.out.println(">>>>>>>>>>>>>> ExceptionAgent agentArgs = " + agentArgs);
        throw new RuntimeException("ExceptionAgent 故意抛出异常");
    }
}
