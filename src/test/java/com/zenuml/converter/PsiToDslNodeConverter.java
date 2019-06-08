package com.zenuml.converter;

import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.zenuml.dsl.DslNode;
import com.zenuml.dsl.FunctionNode;

public class PsiToDslNodeConverter extends JavaElementVisitor {

    private FunctionNode root;

    public void visitMethod(PsiMethod psiMethod) {
        assert psiMethod.getContainingClass() != null;
        root = new FunctionNode(psiMethod.getContainingClass().getName(), psiMethod.getName(), null);
    }

    DslNode rootNode() {
        return root;
    }
}
