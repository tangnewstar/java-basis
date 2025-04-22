package org.tangscode.jvm.garbage_collection;

/**
 * @author tangxinxing
 * @version 1.0
 * @description 对象优先在Eden区分配，Eden没有空闲空间，会触发MinorGC。将对象移到Survivor（如果空间够），不然按分配担保机制进入老年代。
 * @date 2024/9/23
 */
public class EdenSpace {
    private static final int _1MB = 1024 * 1024;

    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * */
    public static void main(String[] args) {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        // 回收发生在这里，4M无法分配到年轻代触发youngGC
        allocation4 = new byte[4 * _1MB];
    }

    /*
[GC (Allocation Failure) [PSYoungGen: 6329K->728K(9216K)] 6329K->4832K(19456K), 0.0019595 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
Heap
 PSYoungGen      total 9216K, used 7113K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
  eden space 8192K, 77% used [0x00000000ff600000,0x00000000ffc3c710,0x00000000ffe00000)
  from space 1024K, 71% used [0x00000000ffe00000,0x00000000ffeb6030,0x00000000fff00000)
  to   space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
 ParOldGen       total 10240K, used 4104K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
  object space 10240K, 40% used [0x00000000fec00000,0x00000000ff002020,0x00000000ff600000)
 Metaspace       used 3165K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 333K, capacity 386K, committed 512K, reserved 1048576K
    * */

    /*
    根据提供的Java堆内存日志，以下是对当前状态的分析和总结：
------------------------
1. 当前堆内存状态
Young Generation（年轻代）
•  总大小：9,216 KB（其中 Eden Space 8,192 KB，两个 Survivor Space 各 1,024 KB）
•  当前使用：7,113 KB（占约77%）
•  Eden Space：已使用 77%（约6,329 KB），接近满载，这是触发垃圾回收的直接原因。
•  From Space：已使用 71%（约728 KB）。回收后，存活对象被移动到这里。
•  To Space：当前未被使用（0%）。
Old Generation（老年代）
•  总大小：10,240 KB
•  当前使用：4,104 KB（占约40%），内存占用较低。
总堆内存
•  总堆空间：19,456 KB（Young + Old）
•  当前总使用：约 11,217 KB（Young 7,113 KB + Old 4,104 KB）
•  剩余空间：约 8,239 KB，整体使用率约 57.6%（未达紧急阈值）。
------------------------
2. 垃圾回收（GC）事件分析
•  类型：Minor GC（Young GC）
•  触发原因：Allocation Failure（Eden Space 满，无法为新对象分配内存）。
•  回收过程：
•  Young Generation 从 6,329 KB 降低到 728 KB（仅存活对象被移动到 from space）。
•  整个堆内存从 6,329 KB 降到 4,832 KB（可能包括老年代的少量回收或统计方式差异，需结合上下文）。
•  耗时：0.0019595 秒（非常短暂，属于正常范围）。
------------------------
3. 问题诊断与建议
当前是否存在内存问题？
•  短期风险：无明显危机。堆内存使用率仅约57.6%，未达到触发 Full GC（老年代回收）的临界点。
•  长期观察建议：
•  若频繁出现 Allocation Failure 触发 Minor GC（如每秒多次），可能需要调整 Young 区域大小（如增大 Eden Space）。
•  若老年代（ParOldGen）持续接近满载，需排查对象生命周期或内存泄漏。
优化方向
1. 调整 Young 区域比例：
•  可尝试增大 Young 区域（例如通过 -XX:NewRatio 或 -Xmn 参数），减少 Minor GC 频率。
•  或调整 Survivor 空间比例（-XX:SurvivorRatio），让对象有更多机会晋升到老年代前被回收。
2. 监控 GC 频率：
•  使用工具（如 jstat、VisualVM 或 GC logs）长期跟踪 GC 次数、停顿时间，判断是否需要调整堆参数。
3. 检查内存泄漏：
•  若老年代持续增长，需通过内存分析工具（如 Eclipse MAT）检查长期存活对象。
------------------------
4. 元数据空间（Metaspace）
•  使用量：3,165 KB / 4,486 KB（容量预留充足，仅占总保留空间的 0.3%）。
•  结论：无 Metaspace 泄漏风险，无需调整 -XX:MetaspaceSize 等参数。
------------------------
5. 总结
•  当前状态：内存使用稳定，Young 区域因 Eden 满载触发 Minor GC 属正常现象，未达紧急程度。
•  下一步建议：
1. 监控 GC 日志，观察 Minor GC 的频率是否过高。
2. 若应用性能或内存占用持续增长，进行堆转储分析。
3. 根据实际负载调整堆参数（如 -Xms、-Xmx、-Xmn）优化性能。
如果需要进一步优化或诊断，建议提供完整的 GC 日志（包含多次 GC 事件）或详细的应用场景描述。

     */
}
