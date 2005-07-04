package org.drools.spring.metadata.annotation.java;

import java.lang.reflect.Method;

import org.drools.spring.metadata.MethodMetadata;
import org.drools.spring.metadata.MethodMetadataSource;

public class AnnotationMethodMetadataSource implements MethodMetadataSource {

    private static final MethodMetadata METHOD_CONDITION_METADATA = new MethodMetadata(MethodMetadata.METHOD_CONDITION);
    private static final MethodMetadata OBJECT_CONDITION_METADATA = new MethodMetadata(MethodMetadata.OBJECT_CONDITION);
    private static final MethodMetadata METHOD_CONSEQUENCE_METADATA = new MethodMetadata(MethodMetadata.METHOD_CONSEQUENCE);

    public MethodMetadata getMethodMetadata(Method method) {
        if (method.isAnnotationPresent(Condition.class)) {
            return METHOD_CONDITION_METADATA;
        } else if (method.isAnnotationPresent(ObjectCondition.class)) {
                return OBJECT_CONDITION_METADATA;
        } else if (method.isAnnotationPresent(Consequence.class)) {
            return METHOD_CONSEQUENCE_METADATA;
        } else {
            return null;
        }
    }
}
