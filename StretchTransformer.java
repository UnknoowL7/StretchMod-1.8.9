package com.dabaduh.stretchmod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class StretchTransformer implements IClassTransformer {

    private static final String TARGET =
            "net.minecraft.client.renderer.EntityRenderer";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        if (!TARGET.equals(transformedName)) {
            return basicClass;
        }

        try {
            ClassNode classNode = new ClassNode();

            ClassReader reader = new ClassReader(basicClass);
            reader.accept(classNode, 0);

            for (MethodNode method : classNode.methods) {

                if (!method.name.equals("setupCameraTransform")
                        && !method.name.equals("a")) {
                    continue;
                }

                for (AbstractInsnNode insn : method.instructions.toArray()) {

                    if (insn.getOpcode() == Opcodes.INVOKESTATIC) {

                        MethodInsnNode call = (MethodInsnNode) insn;

                        if (call.owner.equals(
                                "net/minecraft/util/Project")
                                && call.name.equals("gluPerspective")) {

                            InsnList stretch = new InsnList();

                            stretch.add(new LdcInsnNode(1.25F));

                            stretch.add(new InsnNode(Opcodes.FCONST_1));

                            stretch.add(new InsnNode(Opcodes.FCONST_1));

                            stretch.add(new MethodInsnNode(
                                    Opcodes.INVOKESTATIC,
                                    "org/lwjgl/opengl/GL11",
                                    "glScalef",
                                    "(FFF)V",
                                    false
                            ));

                            method.instructions.insert(insn, stretch);
                        }
                    }
                }
            }

            ClassWriter writer =
                    new ClassWriter(ClassWriter.COMPUTE_MAXS);

            classNode.accept(writer);

            return writer.toByteArray();

        } catch (Throwable t) {
            t.printStackTrace();
            return basicClass;
        }
    }
}
