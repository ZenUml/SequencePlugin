package org.intellij.sequencer.generator;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.search.searches.DefinitionsScopedSearch;
import com.intellij.util.containers.Stack;
import org.intellij.sequencer.diagram.Info;
import org.intellij.sequencer.generator.filters.ImplementClassFilter;
import org.intellij.sequencer.util.PsiUtil;

import java.util.ArrayList;
import java.util.List;

public class SequenceGenerator extends JavaElementVisitor {
    private final Stack<PsiCallExpression> _exprStack = new Stack<PsiCallExpression>();
    private final Stack<CallStack> _callStack = new Stack<CallStack>();
    private static final Logger LOGGER = Logger.getInstance(SequenceGenerator.class.getName());

    private final ImplementationFinder implementationFinder = new ImplementationFinder();
    private CallStack topStack;
    private CallStack currentStack;
    private int depth;
    private SequenceParams params;
    private boolean smartInterface;

    public SequenceGenerator(SequenceParams params) {
        this.params = params;
        smartInterface = params.isSmartInterface();
    }

    public CallStack generate(PsiMethod psiMethod) {
        PsiClass containingClass = psiMethod.getContainingClass();
        if (containingClass == null) {
            containingClass = (PsiClass) psiMethod.getParent().getContext();
        }

        followImplementation(psiMethod, containingClass);
        return topStack;
    }

    private void followImplementation(PsiMethod psiMethod, PsiClass containingClass) {
        // follow implementation
        if (PsiUtil.isAbstract(containingClass)) {
            followAbstractClass(psiMethod);
        } else {
            resolveVariableInitializer(psiMethod, containingClass);
        }
    }

    private void resolveVariableInitializer(PsiMethod psiMethod, PsiClass containingClass) {
        // resolve variable initializer
        if (smartInterface && !PsiUtil.isExternal(containingClass)) {
            containingClass.accept(implementationFinder);
        }
        psiMethod.accept(this);
    }

    private void followAbstractClass(PsiMethod psiMethod) {
        psiMethod.accept(this);
        PsiElement[] psiElements = DefinitionsScopedSearch.search(psiMethod).toArray(PsiElement.EMPTY_ARRAY);
        if (psiElements.length == 1) {
            if (psiElements[0] instanceof PsiMethod) {
                methodAccept((PsiMethod) psiElements[0]);
            }
        } else {
            for (PsiElement psiElement : psiElements) {
                if (psiElement instanceof PsiMethod) {
                    PsiMethod psiMethod1 = (PsiMethod) psiElement;
                    if (!alreadyInStack(psiMethod1)) {
                        if (!smartInterface && params.getInterfaceImplFilter().allow(psiMethod1)) {
                            methodAccept(psiMethod1);
                        }
                    }

                }
            }
        }
    }

    private boolean alreadyInStack(PsiMethod psiMethod) {
        MethodDescription method = createMethod(psiMethod);
        return currentStack.isRecursive(method);
    }

    private void methodAccept(PsiMethod psiMethod) {
        if (params.getMethodFilter().allow(psiMethod)) {
            PsiClass containingClass = psiMethod.getContainingClass();
            boolean containingClassIsNotExternal = containingClass != null && !PsiUtil.isExternal(containingClass);
            if (smartInterface && containingClassIsNotExternal) {
                containingClass.accept(implementationFinder);
            }
            psiMethod.accept(this);
        }
    }

    public void visitElement(PsiElement psiElement) {
        psiElement.acceptChildren(this);
    }

    public void visitMethod(PsiMethod psiMethod) {
        MethodDescription method = createMethod(psiMethod);
        if (topStack == null) {
            topStack = new CallStack(method);
            currentStack = topStack;
        } else {
            currentStack = currentStack.methodCall(method);
        }
        super.visitMethod(psiMethod);
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        if (PsiUtil.isPipeline(callExpression)) {
            _exprStack.push(callExpression);
            _callStack.push(currentStack);

            callExpression.getFirstChild().acceptChildren(this);

            if (!_exprStack.isEmpty()) {
                CallStack old = currentStack;
                PsiCallExpression pop = _exprStack.pop();
                currentStack = _callStack.pop();
                findAbstractImplFilter(pop, pop.resolveMethod());
                methodCall(pop.resolveMethod());
                currentStack = old;
            }
            super.visitCallExpression(callExpression);
        } else if (PsiUtil.isComplexCall(callExpression)) {
            _exprStack.push(callExpression);
            _callStack.push(currentStack);
            super.visitCallExpression(callExpression);
            if (!_exprStack.isEmpty()) {
                CallStack old = currentStack;
                PsiCallExpression pop = _exprStack.pop();
                currentStack = _callStack.pop();
                findAbstractImplFilter(pop, pop.resolveMethod());
                methodCall(pop.resolveMethod());
                currentStack = old;
            }
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
     * @param callExpression
     * @param psiMethod
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
        if (psiMethod != null) {
            if (params.getMethodFilter().allow(psiMethod)) {
                if (depth < params.getMaxDepth() - 1) {
                    CallStack oldStack = currentStack;
                    depth++;
                    LOGGER.debug("+ depth = " + depth + " method = " + psiMethod.getName());
                    generate(psiMethod);
                    depth--;
                    LOGGER.debug("- depth = " + depth + " method = " + psiMethod.getName());
                    currentStack = oldStack;
                } else {
                    currentStack.methodCall(createMethod(psiMethod));
                }
            }
        }
    }

    private MethodDescription createMethod(PsiMethod psiMethod) {
        PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
        List<String> argNames = new ArrayList<>();
        List<String> argTypes = new ArrayList<>();
        for (PsiParameter parameter : parameters) {
            argNames.add(parameter.getName());
            PsiType psiType = parameter.getType();
            argTypes.add(psiType.getCanonicalText());
        }
        PsiClass containingClass = psiMethod.getContainingClass();
        if (containingClass == null) {
            containingClass = (PsiClass) psiMethod.getParent().getContext();
        }
        List attributes = createAttributes(psiMethod.getModifierList(), PsiUtil.isExternal(containingClass));
        if (psiMethod.isConstructor())
            return MethodDescription.createConstructorDescription(
                    createClassDescription(containingClass),
                    attributes, argNames, argTypes);
        return MethodDescription.createMethodDescription(
                createClassDescription(containingClass),
                attributes, psiMethod.getName(), psiMethod.getReturnType().getCanonicalText(),
                argNames, argTypes);
    }

    private ClassDescription createClassDescription(PsiClass psiClass) {
        return new ClassDescription(psiClass.getQualifiedName(),
                createAttributes(psiClass.getModifierList(), PsiUtil.isExternal(psiClass)));
    }

    private List<String> createAttributes(PsiModifierList psiModifierList, boolean external) {
        if (psiModifierList == null)
            return new ArrayList<>();
        List attributes = new ArrayList();
        for (int i = 0; i < Info.RECOGNIZED_METHOD_ATTRIBUTES.length; i++) {
            String attribute = Info.RECOGNIZED_METHOD_ATTRIBUTES[i];
            if (psiModifierList.hasModifierProperty(attribute))
                attributes.add(attribute);
        }
        if (external)
            attributes.add(Info.EXTERNAL_ATTRIBUTE);
        if (PsiUtil.isInterface(psiModifierList))
            attributes.add(Info.INTERFACE_ATTRIBUTE);
        return attributes;
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
        if (smartInterface && re instanceof PsiNewExpression) {
            String face = expression.getType().getCanonicalText();
            String impl = expression.getRExpression().getType().getCanonicalText();

            params.getInterfaceImplFilter().put(face, new ImplementClassFilter(impl));

        }
        super.visitAssignmentExpression(expression);
    }

    @Override
    public void visitLambdaExpression(PsiLambdaExpression expression) {
        MethodDescription method = createMethod(expression);
        if (topStack == null) {
            topStack = new CallStack(method);
            currentStack = topStack;
        } else {
            if (!true && currentStack.isRecursive(method))
                return;
            currentStack = currentStack.methodCall(method);
        }
        super.visitLambdaExpression(expression);
    }

    private MethodDescription createMethod(PsiLambdaExpression expression) {
        PsiParameter[] parameters = expression.getParameterList().getParameters();
        List<String> argNames = new ArrayList<>();
        List argTypes = new ArrayList();
        for (int i = 0; i < parameters.length; i++) {
            PsiParameter parameter = parameters[i];
            argNames.add(parameter.getName());
            PsiType psiType = parameter.getType();
            argTypes.add(psiType == null ? null : psiType.getCanonicalText());
        }
        String returnType;
        PsiType functionalInterfaceType = expression.getFunctionalInterfaceType();
        if (functionalInterfaceType == null) {
            returnType = null;
        } else {
            returnType = functionalInterfaceType.getCanonicalText();
        }

        PsiMethod psiMethod = PsiUtil.findEncolsedPsiMethod(expression);
        PsiClass containingClass = psiMethod.getContainingClass();
        if (containingClass == null) {
            containingClass = (PsiClass) psiMethod.getParent().getContext();
        }

        return MethodDescription.createLambdaDescription(
                createClassDescription(containingClass), argNames, argTypes, returnType);
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
}
