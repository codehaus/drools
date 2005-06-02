package org.drools.spring.factory;

import java.lang.reflect.Method;

import org.drools.spring.metadata.ArgumentMetadata;
import org.drools.spring.metadata.ArgumentMetadataSource;

class ChainedArgumentMetadataSource implements ArgumentMetadataSource {

    private final ArgumentMetadataSource[] delegates;

    public ChainedArgumentMetadataSource(ArgumentMetadataSource[] delegates) {
        if (delegates == null) {
            throw new IllegalArgumentException("delegates argument must not be null");
        }
        for (int i = 0; i < delegates.length; i++) {
            if (delegates[i] == null) {
                throw new IllegalArgumentException("delegates[" + i + "] element must not be null");
            }
        }
        this.delegates = delegates;
    }

    public ArgumentMetadata getArgumentMetadata(Method method, Class parameterType, int parameterIndex) {
        for (int i = 0; i < delegates.length; i++) {
            ArgumentMetadata metadata = delegates[i].getArgumentMetadata(method, parameterType, parameterIndex);
            if (metadata != null) {
                return metadata;
            }
        }
        return null;
    }
}
