package com.zenuml.testFramework.fixture;

import com.intellij.openapi.application.ex.PathManagerEx;
import com.intellij.psi.PsiClass;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;
import org.junit.Test;

import java.nio.file.Paths;

public class TestFixture extends UsefulTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();

        final TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory().createFixtureBuilder("ZenUML");

        JavaCodeInsightTestFixture myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.getFixture());
        try {
            final String dataPath = Paths.get("./test/data/").toAbsolutePath().toString();
            myFixture.setTestDataPath(dataPath);
            final JavaModuleFixtureBuilder builder = projectBuilder.addModule(JavaModuleFixtureBuilder.class);
            builder.setMockJdkLevel(JavaModuleFixtureBuilder.MockJdkLevel.jdk15);

            myFixture.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(myFixture.getModule());
        PsiClass psiClass = myFixture.addClass("package foo.bar; public class PublicClass { public void method1() {} }");

        System.out.println(psiClass);
        System.out.println(psiClass.getMethods().length);
    }

    @Test
    public void test() {
    }
}
