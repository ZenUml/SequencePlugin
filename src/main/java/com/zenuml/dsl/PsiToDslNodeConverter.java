package com.zenuml.dsl;

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Optional;

public class PsiToDslNodeConverter extends JavaElementVisitor {

    private SequenceDiagram sequenceDiagram = new SequenceDiagram();

    public void visitElement(PsiElement psiElement) {
        psiElement.acceptChildren(this);
    }

    public void visitMethod(PsiMethod psiMethod) {
        System.out.println("Enter: visitMethod");
        String methodExpression = psiMethod.getName() + "()";
        PsiClass containingClass = psiMethod.getContainingClass();
        Optional<FunctionNode> functionNode = Optional
                .ofNullable(containingClass)
                .map(NavigationItem::getName)
                .map(className -> new FunctionNode(className, methodExpression, null));
        functionNode.ifPresent(n -> sequenceDiagram.addSub(n));
        psiMethod.acceptChildren(this);
        System.out.println("Exit: visitMethod");
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        System.out.println("Enter: visitCallExpression");

        Optional<PsiCallExpression> callExpressionOptional = Optional.ofNullable(callExpression);

        Optional<PsiCallExpression> methodCallExpression = callExpressionOptional
                .filter(c -> Optional.ofNullable(callExpression.resolveMethod()).isPresent());

        methodCallExpression
                .map(c -> new AbstractMap.SimpleImmutableEntry<>(c.getText(), c.resolveMethod().getContainingClass().getName()))
                .flatMap(cm -> {
                    String text = cm.getKey();
                    String methodExpression = removeCallee(text);
                    return Optional.of(new FunctionNode(cm.getValue(), methodExpression, null));
                })
                .ifPresent(n -> sequenceDiagram.addSub(n));

        methodCallExpression
                .map(PsiCall::resolveMethod)
                .ifPresent(m -> {
                    m.acceptChildren(this);
                    sequenceDiagram.end();
                });

        System.out.println("Exit: visitCallExpression");
    }

    @NotNull
    private String removeCallee(String expression) {
        int lastDotPos = expression.lastIndexOf(".");
        return expression.substring(lastDotPos + 1);
    }

    public SequenceDiagram rootNode() {
        return sequenceDiagram;
    }
}
