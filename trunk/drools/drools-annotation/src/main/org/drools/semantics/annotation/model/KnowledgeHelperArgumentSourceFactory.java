package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;

import org.drools.rule.Rule;
import org.drools.spi.KnowledgeHelper;

public class KnowledgeHelperArgumentSourceFactory implements ArgumentSourceFactory {

    public Class<? extends ArgumentSource> getParameterValueType() {
        return KnowledgeHelperArgumentSource.class;
    }

    public ArgumentSource create(Rule rule, Class< ? > parameterClass,
                                  Annotation[] parameterAnnotations) {
        if (parameterClass != KnowledgeHelper.class) {
            return null;
        }
        return new KnowledgeHelperArgumentSource(rule);
    }
}