package org.drools.tags.ruleset;

import org.drools.spi.Consequence;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;

public class ConsequenceTag extends RuleSetTagSupport
{
    private Consequence consequence;

    protected ConsequenceTag()
    {
        this.consequence = null;
    }

    public void setConsequence(Consequence consequence)
    {
        this.consequence = consequence;
    }

    public Consequence getConsequence()
    {
        return this.consequence;
    }
    
    public void doTag(XMLOutput output) throws Exception
    {
        invokeBody( output );

        if ( this.consequence == null )
        {
            throw new JellyException( "Condition expected" );
        }

        getRule().setConsequence( this.consequence );
    }
}
