package org.drools.smf;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.Functions;

public interface FunctionsFactory
{
    Functions newFunctions(RuleSet ruleSet,
                           RuleBaseContext context,
                           Configuration config) throws FactoryException;
}