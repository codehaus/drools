package org.drools.tags.ruleset;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.MissingAttributeException;

public abstract class RuleSetTagSupport extends TagSupport
{
    protected RuleSetTagSupport()
    {

    }

    protected RuleSet getRuleSet() throws JellyException
    {
        RuleSetTag ruleSetTag = (RuleSetTag) findAncestorWithClass( RuleSetTag.class );

        if ( ruleSetTag == null )
        {
            throw new JellyException( "No rule-set available" );
        }

        return ruleSetTag.getRuleSet();
    }

    protected Rule getRule() throws JellyException
    {
        RuleTag ruleTag = (RuleTag) findAncestorWithClass( RuleSetTag.class );

        if ( ruleTag == null )
        {
            throw new JellyException( "No rule available" );
        }

        return ruleTag.getRule();
    }

    protected void requiredAttribute(String name,
                                     String value) throws MissingAttributeException
    {
        if ( value == null
             ||
             value.trim().equals( "" ) )
        {
            throw new MissingAttributeException( name );
        }
    }
}
