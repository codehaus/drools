package org.drools.spring.metadata;

import org.drools.rule.Rule;
import org.drools.spring.pojorule.ApplicationDataArgument;
import org.drools.spring.pojorule.Argument;

public class ApplicationDataArgumentMetadata implements ArgumentMetadata {

    private final String identifier;
    private final Class parameterClass;

    public ApplicationDataArgumentMetadata(String identifier, Class parameterClass) {
        if (identifier == null || identifier.trim().length() == 0) {
            throw new IllegalArgumentException("identifier must not be null");
        }
        if (parameterClass == null) {
            throw new IllegalArgumentException("parameterClass must not be null");
        }
        this.identifier = identifier;
        this.parameterClass = parameterClass;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Class getParameterClass() {
        return parameterClass;
    }

    public Argument createArgument(Rule rule) {
        return new ApplicationDataArgument(identifier, parameterClass);
    }
}
