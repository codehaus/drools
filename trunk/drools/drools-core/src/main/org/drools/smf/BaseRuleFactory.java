package org.drools.smf;

import org.drools.rule.Rule;

public class BaseRuleFactory
    implements RuleFactory
{
    public BaseRuleFactory()
    {

    }

    public Rule newRule(Configuration config)
        throws FactoryException
    {
        String name = config.getAttribute( "name" );

        return new Rule( name );
    }
}
