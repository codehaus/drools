package org.drools.smf;

import org.drools.rule.RuleSet;
import org.drools.spi.Functions;
import org.drools.spi.RuleBaseContext;

public interface FunctionsFactory
{
    Functions newFunctions(RuleSet ruleSet,
                           RuleBaseContext context,
                           Configuration config) throws FactoryException;
}