package org.drools.semantics.base;

import org.drools.semantics.rule.Rule;
import org.drools.semantics.smf.RuleFactory;
import org.drools.semantics.smf.Configuration;

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

        if ( name == null
             ||
             name.trim().equals( "" ) )
        {
            throw new MissingAttributeException( "name" );
        }

        return new Rule( name.trim() );
    }
}
