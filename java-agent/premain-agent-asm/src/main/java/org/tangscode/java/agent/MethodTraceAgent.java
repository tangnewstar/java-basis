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
        if (null == args || args.isEmpty()) {
            throw new IllegalArgumentException("please specify package to be traced");
        }
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith(args)) // 监控指定包
                //DynamicType.Builder<?> var1, TypeDescription var2, @MaybeNull ClassLoader var3, @MaybeNull JavaModule var4, ProtectionDomain var5
                .transform((builder, type, classLoader, module, domain) ->
                        builder.visit(Advice.to(MethodAdvice.class)
                                .on(ElementMatchers.isMethod())))
                .installOn(inst);
    }
}

