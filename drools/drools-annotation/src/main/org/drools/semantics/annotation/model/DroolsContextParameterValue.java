package org.drools.semantics.annotation.model;

import org.drools.rule.Rule;
import org.drools.semantics.annotation.DroolsContext;
import org.drools.spi.Tuple;

class DroolsContextParameterValue implements ParameterValue
{
    private final Rule rule;

    public DroolsContextParameterValue(Rule rule)
    {
        if (rule == null)
        {
            throw new IllegalArgumentException( "Null 'rule' argument" );
        }
        this.rule = rule;
    }

    public DroolsContext getValue( Tuple tuple )
    {
        return new KnowledgeHelperDroolsContext( rule, tuple );
    }
}
