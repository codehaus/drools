package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;

import org.drools.DroolsException;
import org.drools.rule.Rule;

public interface ArgumentFactory
{
    Class<? extends Argument> getArgumentType();

    Argument create (Rule rule, Class<?> parameterClass,
                     Annotation[] parameterAnnotations) throws DroolsException;
}