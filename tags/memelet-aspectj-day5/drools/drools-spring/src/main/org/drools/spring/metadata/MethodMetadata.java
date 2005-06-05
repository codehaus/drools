package org.drools.spring.metadata;

public class MethodMetadata {

    public static final int CONDITION = 0;
    public static final int CONSEQUENCE = 1;

    private final int methodType;

    public MethodMetadata(int methodType) {
        this.methodType = methodType;
    }

    public int getMethodType() {
        return methodType;
    }
}
