package org.tangscode.java.agent;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/5
 */

import java.util.concurrent.ConcurrentHashMap;

public class SimpleTracer {
    private static final ConcurrentHashMap<Long, Long> START_TIMES = new ConcurrentHashMap<>();

    public static void start() {
        final long threadId = Thread.currentThread().getId();
        START_TIMES.put(threadId, System.nanoTime());
    }

    public static void end(String methodName) {
        final long threadId = Thread.currentThread().getId();
        final long end = System.nanoTime();
        final Long start = START_TIMES.get(threadId);

        if (start != null) {
            long costMs = (end - start) / 1_000_000;
            System.out.printf("[SimpleTracer] %s -> %dms\n", methodName, costMs);
            START_TIMES.remove(threadId);
        }
    }
}


