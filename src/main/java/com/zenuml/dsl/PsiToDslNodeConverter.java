package com.zenuml.dsl;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.search.searches.DefinitionsScopedSearch;
import org.intellij.sequencer.generator.SequenceParams;
import org.intellij.sequencer.generator.filters.ImplementClassFilter;
import org.intellij.sequencer.util.PsiUtil;

public class PsiToDslNodeConverter extends JavaElementVisitor {

    private final ImplementationFinder implementationFinder = new ImplementationFinder();
    private int depth;
    private SequenceParams params;
    private SequenceDiagram sequenceDiagram = new SequenceDiagram();

    public PsiToDslNodeConverter() {
        this.params = new SequenceParams();
    }

    public void generate(PsiMethod psiMethod) {
        PsiClass containingClass = psiMethod.getContainingClass();
        if (containingClass == null) {
            containingClass = (PsiClass) psiMethod.getParent().getContext();
        }

        // follow implementation
        if (PsiUtil.isAbstract(containingClass)) {
            psiMethod.accept(this);
            PsiElement[] psiElements = DefinitionsScopedSearch.search(psiMethod).toArray(PsiElement.EMPTY_ARRAY);
            if (psiElements.length == 1) {
                methodAccept(psiElements[0]);
            } else {
                for (PsiElement psiElement : psiElements) {
                    if (psiElement instanceof PsiMethod) {
                        if (!params.isSmartInterface() && params.getInterfaceImplFilter().allow((PsiMethod) psiElement))
                            methodAccept(psiElement);
                    }
                }
            }
        } else {
            // resolve variable initializer
            if (params.isSmartInterface() && !PsiUtil.isExternal(containingClass))
                containingClass.accept(implementationFinder);
            psiMethod.accept(this);
        }
        sequenceDiagram.end();
    }

    private void methodAccept(PsiElement psiElement) {
        if (psiElement instanceof PsiMethod) {
            PsiMethod method = (PsiMethod) psiElement;
            if (params.getMethodFilter().allow(method)) {
                PsiClass containingClass = (method).getContainingClass();
                if (params.isSmartInterface() && containingClass != null && !PsiUtil.isExternal(containingClass))
                    containingClass.accept(implementationFinder);
                method.accept(this);
            }
        }
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
        if (PsiUtil.isPipeline(callExpression)) {
            callExpression.getFirstChild().acceptChildren(this);

            super.visitCallExpression(callExpression);
        } else if (PsiUtil.isComplexCall(callExpression)) {
            super.visitCallExpression(callExpression);
        } else {
            PsiMethod psiMethod = callExpression.resolveMethod();
            findAbstractImplFilter(callExpression, psiMethod);
            methodCall(psiMethod);
            super.visitCallExpression(callExpression);
        }
    }

    /**
     * If the psiMethod's containing class is Interface or abstract, then try to find it's implement class.
     *
     */
    private void findAbstractImplFilter(PsiCallExpression callExpression, PsiMethod psiMethod) {
        try {
            PsiClass containingClass = psiMethod.getContainingClass();
            if (PsiUtil.isAbstract(containingClass)) {
                String type = containingClass.getQualifiedName();
                String impl = ((PsiMethodCallExpressionImpl) callExpression).getMethodExpression().getQualifierExpression().getType().getCanonicalText();
                if (!impl.startsWith(type))
                    params.getInterfaceImplFilter().put(type, new ImplementClassFilter(impl));
            }
        } catch (Exception e) {
            //ignore
        }
    }

    private void methodCall(PsiMethod psiMethod) {
        if (psiMethod == null)
            return;
        if (params.getMethodFilter().allow(psiMethod)) {
            if (depth < 5 - 1) {
                depth++;
                generate(psiMethod);
                depth--;
            }
        }
    }

    @Override
    public void visitLocalVariable(PsiLocalVariable variable) {
        PsiJavaCodeReferenceElement referenceElement = variable.getTypeElement().getInnermostComponentReferenceElement();
        if (referenceElement != null) {
            PsiClass psiClass = (PsiClass) referenceElement.resolve();

            if (PsiUtil.isAbstract(psiClass)) {
                String type = variable.getType().getCanonicalText();
                PsiExpression initializer = variable.getInitializer();
                if (initializer instanceof PsiNewExpression) {
                    String impl = initializer.getType().getCanonicalText();
                    if (!type.equals(impl)) {
                        params.getInterfaceImplFilter().put(type, new ImplementClassFilter(impl));
                    }
                }
            }

        }

        super.visitLocalVariable(variable);
    }

    @Override
    public void visitAssignmentExpression(PsiAssignmentExpression expression) {
        PsiExpression re = expression.getRExpression();
        if (params.isSmartInterface() && re instanceof PsiNewExpression) {
            String face = expression.getType().getCanonicalText();
            String impl = expression.getRExpression().getType().getCanonicalText();

            params.getInterfaceImplFilter().put(face, new ImplementClassFilter(impl));

        }
        super.visitAssignmentExpression(expression);
    }


    @Override
    public void visitInstanceOfExpression(PsiInstanceOfExpression expression) {
        super.visitInstanceOfExpression(expression);
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
        public void visitField(PsiField field) {
            PsiTypeElement typeElement = field.getTypeElement();
            if (typeElement != null) {
                PsiJavaCodeReferenceElement referenceElement = typeElement.getInnermostComponentReferenceElement();
                if (referenceElement != null) {
                    PsiClass psiClass = (PsiClass) referenceElement.resolve();
                    if (PsiUtil.isAbstract(psiClass)) {
                        String type = field.getType().getCanonicalText();
                        PsiExpression initializer = field.getInitializer();
                        if (initializer != null && initializer instanceof PsiNewExpression) {
                            String impl = initializer.getType().getCanonicalText();
                            if (!type.equals(impl)) {
                                params.getInterfaceImplFilter().put(type, new ImplementClassFilter(impl));
                            }
                        }
                    }

                }
            }

            super.visitField(field);
        }

        @Override
        public void visitMethod(PsiMethod method) {
            // only constructor
            PsiClass containingClass = method.getContainingClass();
            if (containingClass != null && method.getName().equals(containingClass.getName())) {
                super.visitMethod(method);
            }
        }

        @Override
        public void visitAssignmentExpression(PsiAssignmentExpression expression) {
            PsiExpression re = expression.getRExpression();
            if (re instanceof PsiNewExpression) {
                String face = expression.getType().getCanonicalText();
                String impl = expression.getRExpression().getType().getCanonicalText();

                params.getInterfaceImplFilter().put(face, new ImplementClassFilter(impl));

            }
            super.visitAssignmentExpression(expression);
        }

        public void visitElement(PsiElement psiElement) {
            psiElement.acceptChildren(this);
        }

    }


    public SequenceDiagram rootNode() {
        return sequenceDiagram;
    }
}
