package org.drools.semantics.annotation.model;

import org.drools.rule.Rule;
import org.drools.semantics.annotation.Drools;
import org.drools.spi.Tuple;

class DroolsParameterValue implements ParameterValue
{
    private final Rule rule;

    public DroolsParameterValue(Rule rule)
    {
        if (rule == null)
        {
            throw new IllegalArgumentException( "Null 'rule' argument" );
        }
        this.rule = rule;
    }

    public Drools getValue( Tuple tuple )
    {
        return new KnowledgeHelperDrools( rule, tuple );
    }
}
