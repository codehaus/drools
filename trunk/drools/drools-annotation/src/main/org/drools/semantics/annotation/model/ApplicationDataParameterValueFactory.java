package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;

import org.drools.semantics.annotation.DroolsApplicationData;
import org.drools.rule.Rule;

class ApplicationDataParameterValueFactory extends AnnotationParameterValueFactory
{
    public ApplicationDataParameterValueFactory() {
        super(DroolsApplicationData.class);
    }

    public Class<? extends ParameterValue> getParameterValueType() {
        return ApplicationDataParameterValue.class;
    }

    @Override
    public ParameterValue doCreate ( Rule rule, Class< ? > parameterClass,
                                     Annotation annotation) {
        String parameterId = ((DroolsApplicationData) annotation).value( );
        return new ApplicationDataParameterValue( parameterId, parameterClass );
    }
}
