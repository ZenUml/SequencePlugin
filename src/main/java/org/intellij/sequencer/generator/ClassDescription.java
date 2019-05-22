package org.intellij.sequencer.generator;

import org.intellij.sequencer.Constants;

import java.util.Collections;
import java.util.List;

public class ClassDescription {
    private String _className;
    private List _attributes;

    ClassDescription(String className, List attributes) {
        _className = className != null ? className : Constants.ANONYMOUS_CLASS_NAME;
        _attributes = attributes;
    }

    String getClassShortName() {
        return _className.substring(_className.lastIndexOf('.') + 1);
    }

    public String getClassName() {
        return _className;
    }

    public List getAttributes() { return Collections.unmodifiableList(_attributes); }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (Object o : _attributes) {
            String attribute = (String) o;
            buffer.append('|').append(attribute);
        }
        buffer.append("|@").append(_className);
        return buffer.toString();
    }

    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof ClassDescription)) return false;

        final ClassDescription classDescription = (ClassDescription)o;

        return _className.equals(classDescription._className);

    }

    public int hashCode() {
        return _className.hashCode();
    }

}
