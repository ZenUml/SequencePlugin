package com.zenuml.dsl;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;

public class PsiToDslConverter extends JavaRecursiveElementVisitor {
    private String dsl = "";

    @Override
    public void visitMethod(PsiMethod method) {
        dsl += method.getContainingClass().getName() + "." + method.getName() + "()";
        // getBody return null if the method belongs to a compiled class
        if (method.getBody() != null && !method.getBody().isEmpty()) {
            dsl += "{";
            super.visitMethod(method);
            dsl += "}";
        }
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        PsiMethod method = expression.resolveMethod();
        if (method != null) {
            visitMethod(method);
            super.visitMethodCallExpression(expression);
        }
    }

    public String getDsl() {
        return dsl;
    }
}
