package com.zenuml.dsl;

import com.intellij.psi.JavaRecursiveElementWalkingVisitor;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;

import java.util.Arrays;

public class PsiToDslNodeConverter extends JavaRecursiveElementWalkingVisitor {

    private SequenceDiagram sequenceDiagram = new SequenceDiagram();

    @Override
    public void visitMethod(PsiMethod method) {
        System.out.println("Enter: visitMethod:" + method);
        super.visitMethod(method);
        System.out.println("Exit: visitMethod");
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        System.out.println("Enter: visitMethodCallExpression:" + expression);
        super.visitMethodCallExpression(expression);
        System.out.println("Exit: visitMethodCallExpression");
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        System.out.println("Enter: visitIfStatement:" + statement);
        Arrays.stream(statement.getChildren())
                .forEach(c -> System.out.println(c.getClass() + ":" + c.getText()));
        super.visitIfStatement(statement);
        System.out.println("Exit: visitIfStatement:" + statement);
    }

    public SequenceDiagram rootNode() {
        return sequenceDiagram;
    }
}
