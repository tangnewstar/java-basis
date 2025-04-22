package org.tangscode.jvm.garbage_collection;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2024/7/29
 */
public class JavaVMStackOOM {
    private void dontStop() {
        while (true) {
        }
    }
    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            });
            thread.start();
        }
    }
    // VM Args：-Xss2M （这时候不妨设大些，请在32位系统下运行）
    public static void main(String[] args) throws Throwable {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
