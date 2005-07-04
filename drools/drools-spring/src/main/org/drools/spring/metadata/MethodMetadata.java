package org.drools.spring.metadata;

public class MethodMetadata {

    public static final int METHOD_CONDITION = 0;
    public static final int METHOD_CONSEQUENCE = 1;
    public static final int OBJECT_CONDITION = 3;

    private final int methodType;

    public MethodMetadata(int methodType) {
        this.methodType = methodType;
    }

    public int getMethodType() {
        return methodType;
    }
}
