package org.drools.tags.ruleset;

import org.drools.spi.Condition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;

public class ConditionTag extends RuleSetTagSupport
{
    private Condition condition;

    protected ConditionTag()
    {
        this.condition = null;
    }

    protected void setCondition(Condition condition)
    {
        this.condition = condition;
    }

    public Condition getCondition()
    {
        return this.condition;
    }

    public void doTag(XMLOutput output) throws Exception
    {
        invokeBody( output );

        if ( this.condition == null )
        {
            throw new JellyException( "Condition expected" );
        }
    }
}
