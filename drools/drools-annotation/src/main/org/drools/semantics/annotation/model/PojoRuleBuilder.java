package org.drools.semantics.annotation.model;

import org.drools.rule.Rule;

public interface PojoRuleBuilder {

    void populateRule(Rule rule, Class pojoClass);
    
}
