package com.zenuml.dsl;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiMethod;
import org.apache.commons.lang3.StringUtils;

public class PsiToDslConverter extends JavaRecursiveElementVisitor {
    private String dsl = "";

    @Override
    public void visitMethod(PsiMethod method) {
        dsl += method.getContainingClass().getName() + "." + method.getName() + "()";
        super.visitMethod(method);
    }

    public String getDsl() {
        return dsl;
    }
}
