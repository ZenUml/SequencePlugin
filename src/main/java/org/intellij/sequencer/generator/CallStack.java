package org.intellij.sequencer.generator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallStack {
    private MethodDescription _method;
    private CallStack _parent;
    private List<CallStack> _calls = new ArrayList<>();

    CallStack(MethodDescription method) {
        _method = method;
    }

    private CallStack(MethodDescription method, CallStack parent) {
        _method = method;
        _parent = parent;
    }

    CallStack methodCall(MethodDescription method) {
        CallStack callStack = new CallStack(method, this);
        _calls.add(callStack);
        return callStack;
    }

    boolean isRecursive(MethodDescription method) {
        CallStack current = this;
        while(current != null) {
            if(current._method.equals(method))
                return true;
            current = current._parent;
        }
        return false;
    }

    public String generateSequence() {
        StringBuffer buffer = new StringBuffer();
        generate(buffer);
        return buffer.toString();
    }

    public MethodDescription getMethod() {
        return _method;
    }

    private void generate(StringBuffer buffer) {
        buffer.append('(').append(_method.toJson()).append(' ');
        for(Iterator iterator = _calls.iterator(); iterator.hasNext();) {
            CallStack callStack = (CallStack)iterator.next();
            callStack.generate(buffer);
            if(iterator.hasNext()) {
                buffer.append(' ');
            }
        }
        buffer.append(')');
    }

    public String generateText() {
        StringBuffer buffer = new StringBuffer();
        int deep = 0;
        generateFormatStr(buffer, deep);
        return buffer.toString();
    }

    private void generateFormatStr(StringBuffer buffer, int deep) {
        for (int i = 0; i< deep; i ++) {
            buffer.append("    ");
        }
        buffer.append(_method.toJson()).append('\n');
        for (Object call : _calls) {
            CallStack callStack = (CallStack) call;
            callStack.generateFormatStr(buffer, deep + 1);
        }
    }

}
