package org.drools.spring.factory;

import java.lang.reflect.Method;

import org.drools.spring.metadata.MethodMetadata;
import org.drools.spring.metadata.MethodMetadataSource;

class ChainedMethodMetadataSource implements MethodMetadataSource {

    private final MethodMetadataSource[] delegates;


    public ChainedMethodMetadataSource(MethodMetadataSource[] delegates) {
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


    public MethodMetadata getMethodMetadata(Method method) {
        for (int i = 0; i < delegates.length; i++) {
            MethodMetadata metadata = delegates[i].getMethodMetadata(method);
            if (metadata != null) {
                return metadata;
            }
        }
        return null;
    }
}
