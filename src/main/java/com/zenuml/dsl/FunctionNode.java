package com.zenuml.dsl;

public class FunctionNode extends DslNode {
    private final String className;
    private final String functionName;
    private final String assignee;

    public FunctionNode(String className, String functionName) {
        this(className,functionName,null);
    }

    public FunctionNode(String className, String functionName, String assignee) {
        super();
        this.className=className;
        this.functionName=functionName;
        this.assignee = assignee;
    }

    @Override
    public void toDsl(StringBuffer output) {
        printIndent(output);
        printAssignment(output);
        printFunctionCall(output);
        if (!hasChildren() && !isRoot()) {
            output.append(";\n");
        } else {
            output.append("{\n");
            printChindren(output);
            printIndent(output);
            output.append("}\n");
        }
    }

    private void printFunctionCall(StringBuffer output) {
        output.append(className);
        output.append(".");
        output.append(functionName);
    }

    private void printAssignment(StringBuffer output) {
        if(assignee !=null){
            output.append(assignee);
            output.append("=");
        }
    }

}
