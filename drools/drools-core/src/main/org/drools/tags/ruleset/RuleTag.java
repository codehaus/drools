package org.drools.tags.ruleset;

import org.drools.rule.Rule;

import org.apache.commons.jelly.XMLOutput;

public class RuleTag extends RuleSetTagSupport
{
    private String name;
    private Rule rule;

    protected RuleTag()
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

    public Rule getRule()
    {
        return this.rule;
    }

    public void doTag(XMLOutput output) throws Exception
    {
        requiredAttribute( "name",
                           this.name );

        this.rule = new Rule( this.name );

        invokeBody( output );

        getRuleSet().addRule( this.rule );
    }
}
