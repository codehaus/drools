package org.drools.spring.metadata;

import org.drools.DroolsException;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.FactArgument;

public final class FactArgumentMetadata implements ArgumentMetadata {

    static final String BASE_DEFAULT_IDENTIFIER = "Fact$";

    private String identifier;
    private final Class parameterClass;

    public FactArgumentMetadata(String identifier, Class parameterClass) {
        if (parameterClass == null) {
            throw new IllegalArgumentException("parameterClass must not be null");
        }
        if (identifier == null || identifier.trim().length() == 0) {
            this.identifier = getDefaultIdentifier(parameterClass);
        } else {
            this.identifier = identifier;
        }
        this.parameterClass = parameterClass;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Class getParameterClass() {
        return parameterClass;
    }

    public Argument createArgument(Rule rule) throws DroolsException {
        Declaration declaration = rule.getParameterDeclaration(identifier);
        if (declaration == null) {
            ClassObjectType objectType = new ClassObjectType(parameterClass);
            declaration = rule.addParameterDeclaration(identifier, objectType);
        }
        return new FactArgument(declaration);
    }

    static String getDefaultIdentifier(Class parameterClass) {
        return BASE_DEFAULT_IDENTIFIER + parameterClass.getName();
    }
}
