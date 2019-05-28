package com.zenuml.testFramework.fixture;

import com.intellij.openapi.application.ex.PathManagerEx;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;
import org.junit.Test;

public class TestFixture extends UsefulTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();

        final TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory().createFixtureBuilder("ZenUML");

        JavaCodeInsightTestFixture myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.getFixture());
        try {
            final String dataPath = PathManagerEx.getTestDataPath() + "/codeInsight/externalAnnotations";
            myFixture.setTestDataPath(dataPath);
            final JavaModuleFixtureBuilder builder = projectBuilder.addModule(JavaModuleFixtureBuilder.class);
            builder.setMockJdkLevel(JavaModuleFixtureBuilder.MockJdkLevel.jdk15);

            myFixture.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(myFixture.getModule());

    }

    @Test
    public void test() {
    }
}
