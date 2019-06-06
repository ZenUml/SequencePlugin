package com.zenuml.testFramework.fixture;

import com.intellij.JavaTestUtil;
import com.intellij.codeInspection.emptyMethod.EmptyMethodInspection;
import com.intellij.openapi.application.ex.PathManagerEx;
import com.intellij.psi.PsiClass;
import com.intellij.testFramework.InspectionTestCase;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;
import org.junit.Test;

import java.nio.file.Paths;

public class TestFixture extends InspectionTestCase {

    @Override
    protected String getTestDataPath() {
        return Paths.get("src/test/data/").toAbsolutePath().toString() + "/inspection";
    }

    private void doTest() {
        doTest(false);
    }

    private void doTest(final boolean checkRange) {
        final EmptyMethodInspection tool = new EmptyMethodInspection();
        doTest("emptyMethod/" + getTestName(true), tool, checkRange);
    }

    public void testInAnonymous() {
        doTest(true);
    }
}
