package org.drools.smf;

import org.drools.rule.Rule;

public interface RuleFactory
{
    Rule newRule(Configuration config)
        throws FactoryException;
}
