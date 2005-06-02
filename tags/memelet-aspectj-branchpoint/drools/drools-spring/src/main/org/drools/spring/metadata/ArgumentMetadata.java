package org.drools.spring.metadata;

import org.drools.DroolsException;
import org.drools.rule.Rule;
import org.drools.spring.pojorule.Argument;

public interface ArgumentMetadata {

    Class getParameterClass();

    Argument createArgument(Rule rule) throws DroolsException;

}
