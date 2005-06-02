package org.drools.spring.metadata;

import java.lang.reflect.Method;

public interface MethodMetadataSource {

    MethodMetadata getMethodMetadata(Method method);

}
