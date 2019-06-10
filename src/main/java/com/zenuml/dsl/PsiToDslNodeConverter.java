package com.zenuml.dsl;

import com.intellij.psi.*;

public class PsiToDslNodeConverter extends JavaElementVisitor {

    private SequenceDiagram sequenceDiagram = new SequenceDiagram();

    public void visitElement(PsiElement psiElement) {
        psiElement.acceptChildren(this);
    }

    public void visitMethod(PsiMethod psiMethod) {
        PsiClass containingClass = psiMethod.getContainingClass();
        assert containingClass != null;
        String className = containingClass.getName();
        String methodName = psiMethod.getName();
        FunctionNode functionNode = new FunctionNode(className, methodName + "()", null);
        sequenceDiagram.addSub(functionNode);
        psiMethod.acceptChildren(this);
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        PsiMethod psiMethod = callExpression.resolveMethod();
        if (psiMethod == null) return;

        PsiClass containingClass = psiMethod.getContainingClass();
        assert containingClass != null;
        String className = containingClass.getName();

        FunctionNode functionNode = new FunctionNode(className, callExpression.getText(), null);
        sequenceDiagram.addSub(functionNode);
        psiMethod.acceptChildren(this);
        sequenceDiagram.end();
    }

    public SequenceDiagram rootNode() {
        return sequenceDiagram;
    }
}
