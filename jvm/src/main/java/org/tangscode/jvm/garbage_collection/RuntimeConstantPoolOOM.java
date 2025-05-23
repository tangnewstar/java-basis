package org.tangscode.jvm.garbage_collection;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/4/22
 */

import java.util.HashSet;
import java.util.Set;

/**
 * VM Args：-XX:PermSize=6M -XX:MaxPermSize=6M
 * JDK6 运行
 */
public class RuntimeConstantPoolOOM {
//    public static void main(String[] args) {
//// 使用Set保持着常量池引用，避免Full GC回收常量池行为
//        Set<String> set = new HashSet<String>();
//// 在short范围内足以让6MB的PermSize产生OOM了
//        short i = 0;
//        while (true) {
//            set.add(String.valueOf(i++).intern());
//        }
//    }

    public static void main(String[] args) {
        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1); // true
        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2); // false
        /*

        在JDK 6中，intern()方法会把首次遇到的字符串实例复制到永久代的字符串常量池
        中存储，返回的也是永久代里面这个字符串实例的引用，而由StringBuilder创建的字符串对象实例在
        Java堆上，所以必然不可能是同一个引用，结果将返回false。

        而JDK 7（以及部分其他虚拟机，例如JRockit）的intern()方法实现就不需要再拷贝字符串的实例
        到永久代了，既然字符串常量池已经移到Java堆中，那只需要在常量池里记录一下首次出现的实例引
        用即可，因此intern()返回的引用和由StringBuilder创建的那个字符串实例就是同一个。而对str2比较返
        回false，这是因为“java”[2]这个字符串在执行String-Builder.toString()之前就已经出现过了，字符串常量
        池中已经有它的引用，不符合intern()方法要求“首次遇到”的原则，“计算机软件”这个字符串则是首次
        出现的，因此结果返回true。
         */
    }
}
