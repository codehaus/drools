package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;
import org.drools.semantics.annotation.Parameter;
import org.drools.rule.Declaration;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;

public class TupleArgumentFactory extends AnnotationArgumentFactory {

    static final String BASE_DEFAULT_IDENTIFIER = "Parameter$";

    public TupleArgumentFactory() {
        super(Parameter.class);
    }

    public Class<? extends Argument> getArgumentType() {
        return TupleArgument.class;
    }

    protected Argument doCreate(Rule rule, Class parameterClass,
                                Annotation annotation) throws InvalidRuleException {
        return createArgument(rule, parameterClass, ((Parameter) annotation).value());
    }

    private static String getDefaultIdentifier(Class<?> parameterClass) {
        return BASE_DEFAULT_IDENTIFIER + parameterClass.getName();
    }

    public static Argument createArgument(Rule rule, Class parameterClass, String identifier) throws InvalidRuleException {
        if (identifier == null || identifier.trim().length() == 0) {
            identifier = getDefaultIdentifier(parameterClass);
        }
        Declaration declaration = rule.getParameterDeclaration(identifier);
        if (declaration == null) {
            // TODO This is a poor home for this responsibility
            ClassObjectType objectType = new ClassObjectType(parameterClass);
            declaration = rule.addParameterDeclaration(identifier, objectType);
        }
        return new TupleArgument(declaration);
    }
}