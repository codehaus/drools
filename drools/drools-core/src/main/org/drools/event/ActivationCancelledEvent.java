package org.drools.event;

import org.drools.WorkingMemory;
import org.drools.rule.Rule;
import org.drools.spi.Consequence;
import org.drools.spi.Tuple;

public class ActivationCancelledEvent extends WorkingMemoryEvent
{
    private final Rule rule;

    private final Tuple tuple;

    public ActivationCancelledEvent(WorkingMemory workingMemory,
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


    public Consequence getConsequence()
    {
        return this.rule.getConsequence( );
    }

    public Tuple getTuple()
    {
        return this.tuple;
    }

    public String toString()
    {
        return "[ActivationCancelled: rule=" + this.rule.getName( ) + "; tuple=" + this.tuple + "]";
    }
}