package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.rule.Declaration;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;

class TupleParameterValueFactory extends AnnotationParameterValueFactory
{
    public TupleParameterValueFactory() {
        super(DroolsParameter.class);
    }

    public Class<? extends ParameterValue> getParameterValueType() {
        return TupleParameterValue.class;
    }

    @Override
    public ParameterValue doCreate ( Rule rule, Class< ? > parameterClass,
                                     Annotation annotation) throws InvalidRuleException {
        String parameterId = ((DroolsParameter) annotation).value( );
        Declaration declaration = rule.getParameterDeclaration( parameterId );
        if (declaration == null)
        {
            ClassObjectType objectType = new ClassObjectType( parameterClass );
            declaration = rule.addParameterDeclaration( parameterId, objectType );
        }
        return new TupleParameterValue( declaration );
    }
}