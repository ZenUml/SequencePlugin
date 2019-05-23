package org.intellij.sequencer.generator;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MethodDescriptionTest {

    @Test
    public void test() {
        ClassDescription classDescription = new ClassDescription("com.zenuml.A", new ArrayList());

        MethodDescription methodDescription = MethodDescription.createMethodDescription(classDescription, new ArrayList<>(), "", "", new ArrayList(), null);
//        assertThat(methodDescription.toString(), is(""));
    }
}