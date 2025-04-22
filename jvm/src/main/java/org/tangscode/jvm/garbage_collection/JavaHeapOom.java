package org.tangscode.jvm.garbage_collection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2024/7/26
 */
public class JavaHeapOom {

    static class OOMObject{

    }

    // * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        // 堆溢出
        while(true) {
            list.add(new OOMObject());
        }
    }
}
