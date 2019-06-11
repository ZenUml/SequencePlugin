package com.zenuml.converter;

import com.intellij.psi.*;
import com.zenuml.dsl.PsiToDslNodeConverter;
import com.zenuml.dsl.SequenceDiagram;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SelfMessageZenUmlTest extends ZenUmlTestCase {

    private PsiToDslNodeConverter psiToDslNodeConverter;

    public void setUp() throws Exception {
        super.setUp();
        psiToDslNodeConverter = new PsiToDslNodeConverter();
    }

    public void test_convert_to_dsl_node_selfMessage() {
        myFixture.copyDirectoryToProject("selfMessage","");
        PsiClass selfMessageClass = myFixture.findClass("selfMessage.SelfMessage");
        PsiMethod clientMethod = selfMessageClass.findMethodsByName("clientMethod", true)[0];
        clientMethod.accept(psiToDslNodeConverter);
        SequenceDiagram rootNode = psiToDslNodeConverter.rootNode();
        rootNode.toDsl();
        assertThat(rootNode.toDsl(), is("SelfMessage.clientMethod(){\n  SelfMessage.internalMethod(1);\n}"));
    }

    public void test_convert_to_dsl_node_selfMessage_nest_2_levels() {
        myFixture.copyDirectoryToProject("selfMessage","");
        PsiClass selfMessageClass = myFixture.findClass("selfMessage.SelfMessage");
        PsiMethod clientMethod = selfMessageClass.findMethodsByName("clientMethod2", true)[0];
        clientMethod.accept(psiToDslNodeConverter);
        SequenceDiagram rootNode = psiToDslNodeConverter.rootNode();
        rootNode.toDsl();
        assertThat(rootNode.toDsl(), is("SelfMessage.clientMethod2(){\n  SelfMessage.internalMethodA(i){\n    SelfMessage.internalMethodB(100, 1000){\n      SelfMessage.internalMethodC(i1, i);\n    }  }}"));
    }
}
