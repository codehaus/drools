package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;

import org.drools.rule.Rule;
import org.drools.spi.KnowledgeHelper;

class KnowledgeHelperParameterValueFactory implements ParameterValueFactory {

    public Class<? extends ParameterValue> getParameterValueType() {
        return KnowledgeHelperParameterValue.class;
    }

    public ParameterValue create(Rule rule, Class< ? > parameterClass,
                                  Annotation[] parameterAnnotations) {
        if (parameterClass != KnowledgeHelper.class) {
            return null;
        }
        return new KnowledgeHelperParameterValue(rule);
    }
}