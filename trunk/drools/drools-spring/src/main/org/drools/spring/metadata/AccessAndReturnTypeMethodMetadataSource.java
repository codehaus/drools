package org.drools.spring.metadata;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A method will be considered a condition if:
 *  - method is public
 *  - method returns boolean
 *  - method has at least a one parameter
 *
 * A method will be considered a consequence if:
 *  - method is public
 *  - method returns void
 */
public class AccessAndReturnTypeMethodMetadataSource implements MethodMetadataSource {

    private static final MethodMetadata CONDITION_METADATA = new MethodMetadata(MethodMetadata.CONDITION);
    private static final MethodMetadata CONSEQUENCE_METADATA = new MethodMetadata(MethodMetadata.CONSEQUENCE);

    public MethodMetadata getMethodMetadata(Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        if (isReturnTypeBoolean(method) && hasParameters(method)) {
            return CONDITION_METADATA;
        } else if (isReturnTypeVoid(method)) {
            return CONSEQUENCE_METADATA;
        } else {
            return null;
        }
    }

    private boolean isReturnTypeBoolean(Method method) {
        return boolean.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnTypeVoid(Method method) {
        return void.class.isAssignableFrom(method.getReturnType());
    }

    private boolean hasParameters(Method method) {
        return method.getParameterTypes().length > 0;
    }
}
