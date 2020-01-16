package com.engineer.plugin.transforms.track

import org.objectweb.asm.*

/**
 * @author rookie
 * @since 01-08-2020
 */
class TrackClassVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM6, classVisitor) {

    private var className: String? = null
    private var hack = false

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
//        println("======= $className")
        if (name?.startsWith("home/smart/fly/animations") == true) {

            interfaces?.forEach {
                if (it == "android/view/View\$OnClickListener") {
                    println("bingo , find click in class : $className")
                    hack = true
                }
            }
        }
    }


    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        var methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
        if (hack) {
//            println("name is $name, desc is $desc")
            if (name.equals("onClick") && desc.equals("(Landroid/view/View;)V")) {
                methodVisitor =
                    TrackMethodVisitor(className, Opcodes.ASM6, methodVisitor, access, name, desc)
            }
        }

        return methodVisitor
    }
}