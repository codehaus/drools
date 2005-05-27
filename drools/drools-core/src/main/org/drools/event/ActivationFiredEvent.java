package org.drools.event;

import org.drools.WorkingMemory;
import org.drools.rule.Rule;
import org.drools.spi.Consequence;
import org.drools.spi.Tuple;

public class ActivationFiredEvent extends WorkingMemoryEvent
{
    private Rule rule;

    private Tuple tuple;

    public ActivationFiredEvent(WorkingMemory workingMemory,
                                Rule rule,
                                Tuple tuple)
    {
        super( workingMemory );

        this.rule = rule;
        this.tuple = tuple;
    }
    
    public Rule getRule( )
    {
        return this.rule;
    }

    public Consequence getConsequence( )
    {
        return this.rule.getConsequence( );
    }

    public Tuple getTuple()
    {
        return this.tuple;
    }

    public String toString()
    {
        return "[ActivationFired: rule=" + this.rule.getName( ) + "; tuple=" + this.tuple + "]";
    }
}
