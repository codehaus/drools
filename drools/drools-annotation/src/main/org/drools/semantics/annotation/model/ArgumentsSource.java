package org.drools.semantics.annotation.model;

import java.lang.reflect.Method;

import org.drools.DroolsException;
import org.drools.rule.Rule;

public interface ArgumentsSource {

    Argument[] getArguments(Rule rule, Method method) throws DroolsException;

}
