package com.zenuml.converter;

import com.zenuml.testFramework.fixture.ZenUmlTestCase;

public class SelfMessageZenUmlTest extends ZenUmlTestCase {
    public void testSelfMessage() {
        myFixture.copyDirectoryToProject(getTestName(true),"");
        System.out.println(myFixture.findClass("SelfMessage").getName());
        System.out.println(myFixture.findClass("SelfMessage").getMethods()[0]);
        System.out.println(myFixture.findClass("SelfMessage").getMethods()[1]);
    }
}
