package org.drools.tags.ruleset;

import org.apache.commons.jelly.TagLibrary;

public class RuleSetTagLibrary extends TagLibrary
{
    public RuleSetTagLibrary()
    {
        registerTag( "rule-set",
                     RuleSetTag.class );

        registerTag( "rule",
                     RuleTag.class );

        registerTag( "declaration",
                     DeclarationTag.class );

        registerTag( "parameter",
                     ParameterTag.class );

        registerTag( "extraction",
                     ExtractionTag.class );
    }

}
