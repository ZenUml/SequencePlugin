package com.zenuml.converter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
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
        assertThat(rootNode.toDsl(), is("SelfMessage.clientMethod(){\n  SelfMessage.internalMethod();\n}"));
    }
}
