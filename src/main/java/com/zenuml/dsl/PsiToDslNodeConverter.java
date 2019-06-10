package com.zenuml.dsl;

import com.intellij.psi.*;
import org.intellij.sequencer.generator.SequenceParams;
import org.intellij.sequencer.generator.filters.InterfaceImplFilter;
import org.intellij.sequencer.util.PsiUtil;

public class PsiToDslNodeConverter extends JavaElementVisitor {

    private final ImplementationFinder implementationFinder = new ImplementationFinder();
    private int depth;
    private SequenceParams params;
    private InterfaceImplFilter implFilter;

    private SequenceDiagram sequenceDiagram = new SequenceDiagram();

    public PsiToDslNodeConverter() {
        this.params = new SequenceParams();
        implFilter = params.getInterfaceImplFilter();
    }


    public void visitElement(PsiElement psiElement) {
        psiElement.acceptChildren(this);
    }

    public void visitMethod(PsiMethod psiMethod) {
        assert psiMethod.getContainingClass() != null;
        sequenceDiagram.addSub(new FunctionNode(psiMethod.getContainingClass().getName(), psiMethod.getName(), null));
        super.visitMethod(psiMethod);
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        if (depth < 5) {
            depth++;
            if (!PsiUtil.isComplexCall(callExpression)) {
                PsiMethod psiMethod = callExpression.resolveMethod();
                if (psiMethod == null) return;
                PsiClass containingClass = psiMethod.getContainingClass();
                assert containingClass != null;
                String qualifiedClassName = containingClass.getQualifiedName();
                // follow implementation
                if (!PsiUtil.isAbstract(containingClass)) {
                    // resolve variable initializer
                    if (params.isSmartInterface() && !PsiUtil.isExternal(containingClass))
                        containingClass.accept(implementationFinder);
                    psiMethod.accept(this);
                }
                sequenceDiagram.end();
            }
            super.visitCallExpression(callExpression);
            depth--;
        }
    }

    @Override
    public void visitLocalVariable(PsiLocalVariable variable) {
        PsiJavaCodeReferenceElement referenceElement = variable.getTypeElement().getInnermostComponentReferenceElement();

        super.visitLocalVariable(variable);
    }

    private class ImplementationFinder extends JavaElementVisitor {

        @Override
        public void visitClass(PsiClass aClass) {
            for (PsiClass psiClass : aClass.getSupers()) {
                if (!PsiUtil.isExternal(psiClass))
                    psiClass.accept(this);
            }

            if (!PsiUtil.isAbstract(aClass) && !PsiUtil.isExternal(aClass)) {
                super.visitClass(aClass);
            }
        }

        @Override
        public void visitMethod(PsiMethod method) {
            // only constructor
            PsiClass containingClass = method.getContainingClass();
            if (containingClass != null && method.getName().equals(containingClass.getName())) {
                super.visitMethod(method);
            }
        }


        public void visitElement(PsiElement psiElement) {
            psiElement.acceptChildren(this);
        }

    }


    public SequenceDiagram rootNode() {
        return sequenceDiagram;
    }
}
