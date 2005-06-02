package org.drools.spring.metadata.annotation.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.drools.spring.metadata.ArgumentMetadata;
import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.DataArgumentMetadata;
import org.drools.spring.metadata.FactArgumentMetadata;

public class AnnotationArgumentMetadataSource implements ArgumentMetadataSource{

    public ArgumentMetadata getArgumentMetadata(Method method, Class parameterType, int parameterIndex) {
        Annotation[] parameterAnnotations = method.getParameterAnnotations()[parameterIndex];
        for (int i = 0; i < parameterAnnotations.length; i++) {
            if (parameterAnnotations[i] instanceof Fact) {
                return createFactMetadata((Fact)parameterAnnotations[i], parameterType);
            } else if (parameterAnnotations[i] instanceof Data) {
                return createDataMetadata((Data)parameterAnnotations[i], parameterType);
            }
        }
        return null;
    }

    private ArgumentMetadata createFactMetadata(Fact fact, Class parameterType) {
        return new FactArgumentMetadata(
                fact.value().length() > 0 ? fact.value() : null,
                parameterType);
    }

    private ArgumentMetadata createDataMetadata(Data data, Class parameterType) {
        return new DataArgumentMetadata(data.value(), parameterType);
    }
}
