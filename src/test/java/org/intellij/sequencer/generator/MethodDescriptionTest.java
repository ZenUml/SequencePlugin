package org.intellij.sequencer.generator;

import org.junit.Test;

import java.util.ArrayList;

public class MethodDescriptionTest {

    @Test
    public void test() {
        ClassDescription classDescription = new ClassDescription("com.zenuml.A", new ArrayList());

        MethodDescription methodDescription = MethodDescription.createMethodDescription(classDescription, new ArrayList<>(), "", "", new ArrayList(), null);
//        assertThat(methodDescription.toString(), is(""));
    }
}