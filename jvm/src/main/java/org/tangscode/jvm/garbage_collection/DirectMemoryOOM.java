package org.tangscode.jvm.garbage_collection;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author tangxinxing
 * @version 1.0
 * @description 演示直接内存异常
 * @date 2025/2/18
 */
public class DirectMemoryOOM {

    private static int _1M = 1024 * 1024;
    public static void main(String[] args) throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1M);
        }
    }
}
