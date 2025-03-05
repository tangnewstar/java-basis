package org.tangscode.java.agent;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/5
 */
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class MethodTraceAgent {
    public static void premain(String args, Instrumentation inst) {
        String tracePackageName = null == args || args.isEmpty() ? resolveMainPackage() : args;
        if (tracePackageName.isEmpty()) {
            throw new IllegalArgumentException("package not found or you have to specify");
        }
        System.out.println("Monitor Package: [" + tracePackageName + "]");
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith(tracePackageName)) // 监控指定包
                //DynamicType.Builder<?> var1, TypeDescription var2, @MaybeNull ClassLoader var3, @MaybeNull JavaModule var4, ProtectionDomain var5
                .transform((builder, type, classLoader, module, domain) ->
                        builder.visit(Advice.to(MethodAdvice.class)
                                .on(ElementMatchers.isMethod())))
                .installOn(inst);
    }

    private static String resolveMainPackage() {
        String command = System.getProperty("sun.java.command", "");
        String mainClassName = command.split(" ")[0];
        return mainClassName.substring(0, mainClassName.lastIndexOf("."));
    }


}

