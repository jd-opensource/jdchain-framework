package com.jd.blockchain.maven.plugins.contract.analysis.asm;

import com.jd.blockchain.maven.plugins.contract.analysis.contract.ContractClass;
import com.jd.blockchain.maven.plugins.contract.analysis.contract.ContractMethod;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 基于ASM的Class浏览器
 *
 * @author shaozhuguang
 *
 */
public class ASMClassVisitor extends ClassVisitor {

    private ContractClass contractClass;

    public ASMClassVisitor(ContractClass contractClass) {
        super(Opcodes.ASM5);
        this.contractClass = contractClass;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor superMV = super.visitMethod(access, name, desc, signature, exceptions);
        ContractMethod method = this.contractClass.method(name);
        return new ASMMethodVisitor(superMV, method);
    }
}