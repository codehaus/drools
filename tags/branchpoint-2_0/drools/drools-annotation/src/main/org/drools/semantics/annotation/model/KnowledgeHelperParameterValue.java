package org.drools.semantics.annotation.model;

import org.drools.rule.Rule;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;

class KnowledgeHelperParameterValue implements ParameterValue
{
    private final Rule rule;

    public KnowledgeHelperParameterValue(Rule rule)
    {
        if (rule == null)
        {
            throw new IllegalArgumentException( "Null 'rule' argument" );
        }
        this.rule = rule;
    }

    public KnowledgeHelper getValue( Tuple tuple )
    {
        return new DefaultKnowledgeHelper( rule, tuple );
    }
}
