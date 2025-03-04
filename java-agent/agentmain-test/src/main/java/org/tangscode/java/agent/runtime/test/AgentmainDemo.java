package org.tangscode.java.agent.runtime.test;

import com.sun.tools.attach.VirtualMachine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/4
 */
public class AgentmainDemo {
    public static void main(String[] args) {
        try {
            Map<String, String> argsMap = resolveArguments(args);
            if (!argsMap.containsKey("pid")) {
                throw new IllegalArgumentException("please specify JVM process id");
            }
            if (!argsMap.containsKey("agent")) {
                throw new IllegalArgumentException("please specify JVM agent");
            }
            VirtualMachine vm = VirtualMachine.attach(argsMap.get("pid"));
            vm.loadAgent(argsMap.get("agent"), "haha");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> resolveArguments(String[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        Map<String, String> argsMap = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (null != args[i] && args[i].contains("=")) {
                String[] kv = args[i].split("=");
                if (kv.length == 2 && !"".equals(kv[0]) && !"".equals(kv[1])) {
                    argsMap.put(kv[0], kv[1]);
                }
            }
        }
        return argsMap;
    }
}
