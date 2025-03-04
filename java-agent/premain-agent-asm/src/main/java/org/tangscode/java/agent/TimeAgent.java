

package org.tangscode.java.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandles;
import java.security.ProtectionDomain;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.objectweb.asm.Opcodes.*;
import java.lang.instrument.Instrumentation;
import org.objectweb.asm.*;

public class TimeAgent {
    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer((loader, className, classBeingRedefined,
                             protectionDomain, classfileBuffer) -> {
            if ("org/tangscode/java/agent/test/PremainDemo".equals(className)) {
                ClassReader reader = new ClassReader(classfileBuffer);
                ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
                reader.accept(new ClassVisitor(Opcodes.ASM9, writer) {
                    @Override
                    public MethodVisitor visitMethod(int access, String name,
                                                     String descriptor, String signature,
                                                     String[] exceptions) {
                        MethodVisitor mv = super.visitMethod(access, name, descriptor,
                                signature, exceptions);
                        if ("execute".equals(name) && "()V".equals(descriptor)) {
                            return new MethodTimerAdapter(Opcodes.ASM9, access,
                                    descriptor, mv);
                        }
                        return mv;
                    }
                }, 0);
                return writer.toByteArray();
            }
            return classfileBuffer;
        });
    }

    static class MethodTimerAdapter extends LocalVariablesSorter {
        private int startTimeVar;

        public MethodTimerAdapter(int api, int access, String descriptor, MethodVisitor mv) {
            super(api, access, descriptor, mv);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            // 记录开始时间
            visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
            startTimeVar = newLocal(Type.LONG_TYPE);
            visitVarInsn(Opcodes.LSTORE, startTimeVar);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.RETURN) { // 方法正常返回时插入计时
// 计算耗时（delta = System.nanoTime() - startTime）
                visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                visitVarInsn(Opcodes.LLOAD, startTimeVar);
                visitInsn(Opcodes.LSUB);

                // 输出到控制台
                visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                visitInsn(Opcodes.DUP_X2); // 复制 out 对象到栈底
                visitInsn(Opcodes.POP);
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
            }
            super.visitInsn(opcode);
        }
    }
}


