package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;

import org.drools.semantics.annotation.model.Argument;

import org.drools.DroolsException;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;

public abstract class AnnotationArgumentFactory implements ArgumentFactory {

    private final Class<? extends Annotation> annotationClass;

    protected AnnotationArgumentFactory(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    protected abstract Argument doCreate(Rule rule, Class<?> parameterClass,
                                         Annotation annotation) throws InvalidRuleException;

    public Argument create(Rule rule, Class<?> parameterClass,
                           Annotation[] parameterAnnotations) throws DroolsException {
        Annotation annotation = getAnnotation(annotationClass, parameterAnnotations);
        if (annotation == null) {
            return null;
        }
        return doCreate(rule, parameterClass, annotation);
    }

    private Annotation getAnnotation(Class<? extends Annotation> annotationClass, 
                                     Annotation[] parameterAnnotations) {
        for (Annotation annotation : parameterAnnotations) {
            if (annotationClass.isAssignableFrom(annotation.getClass())) {
                return annotation;
            }
        }
        return null;
    }
}