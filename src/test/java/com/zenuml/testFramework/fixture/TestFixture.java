package com.zenuml.testFramework.fixture;

import com.intellij.codeInspection.emptyMethod.EmptyMethodInspection;
import com.intellij.testFramework.InspectionTestCase;
import com.intellij.testFramework.fixtures.*;

import java.nio.file.Paths;

public class TestFixture extends InspectionTestCaseZenUml {

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
        doTest();
        System.out.println(myFixture.findClass("AStrangeName").getName());
        System.out.println(myFixture.findClass("AStrangeName").getMethods().length);
    }
}
