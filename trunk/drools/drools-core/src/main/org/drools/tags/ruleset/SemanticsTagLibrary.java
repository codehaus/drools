package org.drools.tags.ruleset;

import org.drools.smf.SemanticModule;

import org.apache.commons.jelly.impl.DynamicTagLibrary;

public class SemanticsTagLibrary extends DynamicTagLibrary
{
    private SemanticModule module;

    public SemanticsTagLibrary(SemanticModule module)
    {
        super( module.getUri() );
        this.module = module;
    }

    protected SemanticModule getSemanticModule()
    {
        return this.module;
    }
}
