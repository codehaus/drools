package org.drools.spring.metadata.annotation.java;

import java.lang.reflect.Method;

import org.drools.spring.metadata.MethodMetadata;
import org.drools.spring.metadata.MethodMetadataSource;

public class AnnotationMethodMetadataSource implements MethodMetadataSource {

    private static final MethodMetadata CONDITION_METADATA = new MethodMetadata(MethodMetadata.CONDITION);
    private static final MethodMetadata CONSEQUENCE_METADATA = new MethodMetadata(MethodMetadata.CONSEQUENCE);

    public MethodMetadata getMethodMetadata(Method method) {
        if (method.isAnnotationPresent(Condition.class)) {
            return CONDITION_METADATA;
        } else if (method.isAnnotationPresent(Consequence.class)) {
            return CONSEQUENCE_METADATA;
        } else {
            return null;
        }
    }
}
