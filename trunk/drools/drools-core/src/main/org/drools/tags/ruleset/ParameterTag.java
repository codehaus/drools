package org.drools.tags.ruleset;

import org.drools.rule.Declaration;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;

public class ParameterTag extends DeclarationTag
{
    protected ParameterTag()
    {
        // intentionally left blank
    }

    public void doTag(XMLOutput output) throws Exception
    {
        verifyAttributes();

        Declaration decl = createDeclaration( output );

        getRule().addParameterDeclaration( decl );
    }
}
