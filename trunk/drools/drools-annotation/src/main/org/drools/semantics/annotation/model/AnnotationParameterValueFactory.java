package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;
import org.drools.semantics.annotation.model.ParameterValue;

import org.drools.DroolsException;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;

public abstract class AnnotationParameterValueFactory implements ParameterValueFactory {

    private final Class<? extends Annotation> annotationClass;

    protected AnnotationParameterValueFactory(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    protected abstract ParameterValue doCreate(Rule rule, Class<?> parameterClass,
                                               Annotation annotation) throws InvalidRuleException;

    protected final String getIdentifier(String specifiedIndentifier) {
        return null;
    }

    public ParameterValue create(Rule rule, Class<?> parameterClass,
                                 Annotation[] parameterAnnotations) throws DroolsException {
        Annotation annotation = getAnnotation(annotationClass, parameterAnnotations);
        if (annotation == null) {
            return null;
        }
        return doCreate(rule, parameterClass, annotation);
    }

    private Annotation getAnnotation(Class<? extends Annotation> annotationClass, Annotation[] parameterAnnotations) {
        for (Annotation annotation : parameterAnnotations) {
            if (annotationClass.isAssignableFrom(annotation.getClass())) {
                return annotation;
            }
        }
        return null;
    }
}