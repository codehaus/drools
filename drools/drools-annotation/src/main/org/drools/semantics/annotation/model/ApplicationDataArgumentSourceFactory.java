package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;

import org.drools.semantics.annotation.DroolsApplicationData;
import org.drools.rule.Rule;

public class ApplicationDataArgumentSourceFactory extends AnnotationArgumentSourceFactory {

    static final String DEFAULT_IDENTIFIER = "ApplicationData$defaultIdentifier";

    public ApplicationDataArgumentSourceFactory() {
        super(DroolsApplicationData.class);
    }

    public Class<? extends ArgumentSource> getParameterValueType() {
        return ApplicationDataArgumentSource.class;
    }

    protected ArgumentSource doCreate(Rule rule, Class< ? > parameterClass,
                                      Annotation annotation) {
        String parameterId = ((DroolsApplicationData) annotation).value( );
        return new ApplicationDataArgumentSource( parameterId, parameterClass );
    }
}
