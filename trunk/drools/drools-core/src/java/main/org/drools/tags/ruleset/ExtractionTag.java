package org.drools.tags.ruleset;

import org.drools.rule.Declaration;
import org.drools.rule.FactExtraction;
import org.drools.spi.FactExtractor;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;

public class ExtractionTag extends RuleSetTagSupport
{
    private String target;
    private FactExtractor factExtractor;

    protected ExtractionTag()
    {
        this.target        = null;
        this.factExtractor = null;
    }

    protected void setFactExtractor(FactExtractor factExtractor)
    {
        this.factExtractor = factExtractor;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getTarget()
    {
        return this.target;
    }

    public void doTag(XMLOutput output) throws Exception
    {
        requiredAttribute( "target",
                           this.target );

        Declaration decl = getRule().getDeclaration( this.target );

        if ( decl == null )
        {
            throw new JellyException( "Unknown declaration: " + this.target );
        }

        invokeBody( output );

        if ( this.factExtractor == null )
        {
            throw new JellyException( "Fact extractor expected" );
        }

        FactExtraction extraction = new FactExtraction( decl,
                                                        this.factExtractor );

        getRule().addFactExtraction( extraction );
    }
}
