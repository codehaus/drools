package org.drools.spring.metadata;

import java.lang.reflect.Method;

public interface ArgumentMetadataSource {

    ArgumentMetadata getArgumentMetadata(Method method, Class parameterType, int parameterIndex);

}
