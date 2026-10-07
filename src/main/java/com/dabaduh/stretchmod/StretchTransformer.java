package com.dabaduh.stretchmod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

public class StretchTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        if (!"net.minecraft.client.renderer.EntityRenderer".equals(transformedName)) {
            return basicClass;
        }

        try {
            ClassNode node = new ClassNode();
            new ClassReader(basicClass).accept(node, 0);

            for (MethodNode method : node.methods) {

                if (!method.name.equals("setupCameraTransform")
                        && !method.name.equals("h")) {
                    continue;
                }

                InsnList list = new InsnList();

                list.add(new LdcInsnNode(1.18F));
                list.add(new LdcInsnNode(1.0F));
                list.add(new LdcInsnNode(1.0F));

                list.add(new MethodInsnNode(
                        org.objectweb.asm.Opcodes.INVOKESTATIC,
                        "org/lwjgl/opengl/GL11",
                        "glScalef",
                        "(FFF)V",
                        false
                ));

                method.instructions.insert(list);
            }

            ClassWriter writer =
                    new ClassWriter(ClassWriter.COMPUTE_MAXS);

            node.accept(writer);

            return writer.toByteArray();

        } catch (Throwable e) {
            e.printStackTrace();
            return basicClass;
        }
    }
}
