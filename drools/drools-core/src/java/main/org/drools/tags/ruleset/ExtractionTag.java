package org.drools.tags.ruleset;

import org.drools.rule.Declaration;
import org.drools.rule.Extraction;
import org.drools.spi.Extractor;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;

public class ExtractionTag extends RuleSetTagSupport
{
    private String target;
    private Extractor extractor;

    protected ExtractionTag()
    {
        this.target        = null;
        this.extractor = null;
    }

    protected void setExtractor(Extractor extractor)
    {
        this.extractor = extractor;
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

        if ( this.extractor == null )
        {
            throw new JellyException( "Extractor expected" );
        }

        Extraction extraction = new Extraction( decl,
                                                this.extractor );

        getRule().addExtraction( extraction );
    }
}
