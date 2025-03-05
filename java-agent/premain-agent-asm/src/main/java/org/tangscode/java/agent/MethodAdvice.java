package org.tangscode.java.agent;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/5
 */
import net.bytebuddy.asm.Advice;

public class MethodAdvice {

    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin String methodName) {
        SimpleTracer.start();
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Origin String methodName) {
        SimpleTracer.end(methodName);
    }
}

