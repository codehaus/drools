package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;
import org.drools.semantics.annotation.Parameter;
import org.drools.rule.Declaration;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;

public class TupleArgumentSourceFactory extends AnnotationArgumentSourceFactory {

    static final String BASE_DEFAULT_IDENTIFIER = "Parameter$";

    public TupleArgumentSourceFactory() {
        super(Parameter.class);
    }

    public Class<? extends ArgumentSource> getParameterValueType() {
        return TupleArgumentSource.class;
    }

    protected ArgumentSource doCreate(Rule rule, Class<?> parameterClass,
                                      Annotation annotation) throws InvalidRuleException {
        String parameterId = ((Parameter) annotation).value();
        if (parameterId == null || parameterId.trim().length() == 0) {
            parameterId = getDefaultIdentifier(parameterClass);
        }
        Declaration declaration = rule.getParameterDeclaration(parameterId);
        if (declaration == null) {
            ClassObjectType objectType = new ClassObjectType(parameterClass);
            declaration = rule.addParameterDeclaration(parameterId, objectType);
        }
        return new TupleArgumentSource(declaration);
    }

    private String getDefaultIdentifier(Class<?> parameterClass) {
        return BASE_DEFAULT_IDENTIFIER + parameterClass.getName();
    }
}