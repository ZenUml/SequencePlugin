package org.intellij.sequencer.generator;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ClassDescriptionTest {

    @Test
    public void test() {
        ClassDescription classDescription = new ClassDescription("com.zenuml.A", new ArrayList());

        assertThat(classDescription.getClassName(), is("com.zenuml.A"));
        assertThat(classDescription.getClassShortName(), is("A"));
    }

}