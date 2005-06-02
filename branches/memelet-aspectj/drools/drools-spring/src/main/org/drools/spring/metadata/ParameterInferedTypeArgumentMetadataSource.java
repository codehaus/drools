package org.drools.spring.metadata;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.drools.spi.KnowledgeHelper;

public class ParameterInferedTypeArgumentMetadataSource implements ArgumentMetadataSource{

    public interface ParameterTypeArgumentMetadataFactory {
        ArgumentMetadata createMetadata(Class parameterType);
    }

    private final Map typeToMetadataMap = new HashMap();
    private ParameterTypeArgumentMetadataFactory fallbackMetadataFactory;

    public ParameterInferedTypeArgumentMetadataSource() {
        initializeDefaults();
    }

    private void initializeDefaults() {
        addArgumentMetadataFactory(KnowledgeHelper.class,
                new ParameterTypeArgumentMetadataFactory() {
                    public ArgumentMetadata createMetadata(Class parameterType) {
                        return new KnowledgeHelperArgumentMetadata();
                    }
                });

        setFallbackParameterTypeArgumentMetadataFactory(new ParameterTypeArgumentMetadataFactory() {
                    public ArgumentMetadata createMetadata(Class parameterType) {
                        return new FactArgumentMetadata(null, parameterType);
                    }
                });
    }

    public void addArgumentMetadataFactory(Class parameterType, ParameterTypeArgumentMetadataFactory factory) {
        this.typeToMetadataMap.put(parameterType, factory);
    }

    public void setArgumentMetadataFactories(Map typeToMetadataMap) {
        this.typeToMetadataMap.putAll(typeToMetadataMap);
    }

    public void setFallbackParameterTypeArgumentMetadataFactory(ParameterTypeArgumentMetadataFactory defaultFactory) {
        this.fallbackMetadataFactory = defaultFactory;
    }

    public ArgumentMetadata getArgumentMetadata(Method method, Class parameterType, int parameterIndex) {
        ParameterTypeArgumentMetadataFactory metadataFactory = (ParameterTypeArgumentMetadataFactory) typeToMetadataMap.get(parameterType);
        if (metadataFactory != null) {
            return metadataFactory.createMetadata(parameterType);
        } else {
            return fallbackMetadataFactory.createMetadata(parameterType);
        }
    }
}
