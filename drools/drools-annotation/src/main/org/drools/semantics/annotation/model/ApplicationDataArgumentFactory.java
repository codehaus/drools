package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;

import org.drools.semantics.annotation.ApplicationData;
import org.drools.rule.Rule;

public class ApplicationDataArgumentFactory extends AnnotationArgumentFactory {

    static final String DEFAULT_IDENTIFIER = "ApplicationData$defaultIdentifier";

    public ApplicationDataArgumentFactory() {
        super(ApplicationData.class);
    }

    public Class<? extends Argument> getArgumentType() {
        return ApplicationDataArgument.class;
    }

    protected Argument doCreate(Rule rule, Class< ? > parameterClass,
                                      Annotation annotation) {
        String parameterId = ((ApplicationData) annotation).value( );
        return new ApplicationDataArgument( parameterId, parameterClass );
    }
}
