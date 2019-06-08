package com.zenuml.converter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.zenuml.dsl.DslNode;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SelfMessageZenUmlTest extends ZenUmlTestCase {
    public void testSelfMessage() {
        myFixture.copyDirectoryToProject(getTestName(true),"");
        System.out.println(myFixture.findClass("SelfMessage").getName());
        System.out.println(myFixture.findClass("SelfMessage").getMethods()[0]);
        System.out.println(myFixture.findClass("SelfMessage").getMethods()[1]);
    }

    public void test_convert_to_dsl_node() {
        myFixture.copyDirectoryToProject("selfMessage","");
        PsiToDslNodeConverter psiToDslNodeConverter = new PsiToDslNodeConverter();
        PsiClass selfMessageClass = myFixture.findClass("SelfMessage");
        PsiMethod internalMethod = selfMessageClass.findMethodsByName("selfMethod", true)[0];
        internalMethod.accept(psiToDslNodeConverter);

        DslNode rootNode = psiToDslNodeConverter.rootNode();
        StringBuffer stringBuffer = new StringBuffer();
        rootNode.toDsl(stringBuffer);
        assertThat(stringBuffer.toString(), is("SelfMessage.selfMethod{\n}"));
    }
}
