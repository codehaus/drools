package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;

import org.drools.DroolsException;
import org.drools.rule.Rule;

public interface ParameterValueFactory
{
    Class<? extends ParameterValue> getParameterValueType();

    ParameterValue create ( Rule rule, Class< ? > parameterClass,
                            Annotation[] parameterAnnotations) throws DroolsException;
}