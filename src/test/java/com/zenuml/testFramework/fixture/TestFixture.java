package com.zenuml.testFramework.fixture;

import com.intellij.codeInspection.emptyMethod.EmptyMethodInspection;
import com.intellij.psi.PsiMethod;
import com.intellij.testFramework.InspectionTestCase;
import com.intellij.testFramework.fixtures.*;
import com.zenuml.dsl.SequenceGeneratorV1;
import org.intellij.sequencer.generator.SequenceGenerator;
import org.intellij.sequencer.generator.SequenceParams;

import java.nio.file.Paths;
import java.util.Collections;

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
        SequenceGeneratorV1 sequenceGeneratorV1 = new SequenceGeneratorV1(new SequenceParams(Collections.emptyList()));
        PsiMethod[] aStrangeNames = myFixture.findClass("AStrangeName").getMethods();
        sequenceGeneratorV1.generate(aStrangeNames[0]);
        System.out.println(sequenceGeneratorV1.toDsl());
    }
}
