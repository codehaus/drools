package org.drools.tags.ruleset;

import org.drools.smf.SemanticModule;
import org.drools.io.SemanticsLoader;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.MissingAttributeException;

public class SemanticsTag extends TagSupport
{
    private String module;

    protected SemanticsTag()
    {
        this.module = null;
    }

    public void setModule(String module)
    {
        this.module = module;
    }

    public String getModule()
    {
        return this.module;
    }

    protected SemanticModule getSemanticModule() throws Exception
    {
        SemanticsLoader loader = new SemanticsLoader();

        return loader.load( getModule() );
    }

    public void doTag(XMLOutput output) throws Exception
    {
        if ( this.module == null
             ||
             this.module.trim().equals( "" ) )
        {
            throw new MissingAttributeException( "module" );
        }

        SemanticModule semanticModule = getSemanticModule();

        getContext().registerTagLibrary( semanticModule.getUri(),
                                         new SemanticsTagLibrary( semanticModule ) );
    }
}
