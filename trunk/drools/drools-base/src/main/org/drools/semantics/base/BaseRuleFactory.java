package org.drools.semantics.base;

import org.drools.rule.Rule;
import org.drools.smf.RuleFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.MissingAttributeException;

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
