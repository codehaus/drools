package org.drools.tags.ruleset;

import org.drools.rule.Declaration;
import org.drools.spi.ObjectType;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.MissingAttributeException;

public class DeclarationTag extends RuleSetTagSupport
{
    private String identifier;
    private ObjectType objectType;

    protected DeclarationTag()
    {
        this.identifier = null;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    protected void setObjectType(ObjectType objectType)
    {
        this.objectType = objectType;
    }

    protected void verifyAttributes() throws MissingAttributeException
    {
        requiredAttribute( "identifier",
                           this.identifier );
    }

    protected Declaration createDeclaration(XMLOutput output) throws Exception
    {
        invokeBody( output );

        if ( this.objectType == null )
        {
            throw new JellyException( "No object type specified" );
        }

        Declaration decl = new Declaration( this.objectType,
                                            getIdentifier() );

        return decl;
    }

    public void doTag(XMLOutput output) throws Exception
    {
        verifyAttributes();

        Declaration decl = createDeclaration( output );

        getRule().addDeclaration( decl );
    }
        
}
