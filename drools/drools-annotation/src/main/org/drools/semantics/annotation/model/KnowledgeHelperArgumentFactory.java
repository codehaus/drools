package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;

import org.drools.rule.Rule;
import org.drools.spi.KnowledgeHelper;

public class KnowledgeHelperArgumentFactory implements ArgumentFactory {

    public Class<? extends Argument> getArgumentType() {
        return KnowledgeHelperArgument.class;
    }

    public Argument create(Rule rule, Class<?> parameterClass, Annotation[] parameterAnnotations) {
        if (parameterClass != KnowledgeHelper.class) {
            return null;
        }
        return new KnowledgeHelperArgument(rule);
    }
}