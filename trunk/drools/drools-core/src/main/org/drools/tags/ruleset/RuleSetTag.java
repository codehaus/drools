package org.drools.tags.ruleset;

import org.drools.rule.RuleSet;

import org.apache.commons.jelly.XMLOutput;

public class RuleSetTag extends RuleSetTagSupport
{
    private String name;
    private RuleSet ruleSet;

    protected RuleSetTag()
    {

    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    public void doTag(XMLOutput output) throws Exception
    {
        requiredAttribute( "name",
                           this.name );

        this.ruleSet = new RuleSet( this.name );

        invokeBody( output );
    }
}
