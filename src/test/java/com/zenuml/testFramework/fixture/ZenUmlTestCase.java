package com.zenuml.testFramework.fixture;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.nio.file.Paths;

public class ZenUmlTestCase extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return Paths.get("src/test/data/").toAbsolutePath().toString();
    }

    public void testSelfMessage() {
        myFixture.copyDirectoryToProject(getTestName(true),"");
        System.out.println(myFixture.findClass("SelfMessage").getName());
        System.out.println(myFixture.findClass("SelfMessage").getMethods().length);
    }
}
