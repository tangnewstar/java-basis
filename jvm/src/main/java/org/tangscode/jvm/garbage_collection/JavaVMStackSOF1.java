package org.tangscode.jvm.garbage_collection;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2024/7/29
 */
public class JavaVMStackSOF1 {
    private int stackLength = 1;
    public void stackLeak() {
        // 栈帧的大小约为 128 字节
        // 128kb / 128b = 1000, 预计在1000次循环后抛出StackOverflowError
        stackLength++;
        stackLeak();
    }
    // VM Args：-Xss128k
    public static void main(String[] args) throws Throwable {
        JavaVMStackSOF1 oom = new JavaVMStackSOF1();
        try {
            oom.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length:" + oom.stackLength);
            throw e;
        }
    }
}
