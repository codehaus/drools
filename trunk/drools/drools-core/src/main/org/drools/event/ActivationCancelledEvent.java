package org.drools.event;

import org.drools.WorkingMemory;
import org.drools.spi.Consequence;
import org.drools.spi.Tuple;

public class ActivationCancelledEvent extends WorkingMemoryEvent
{
    private final Consequence consequence;

    private final Tuple tuple;

    public ActivationCancelledEvent(WorkingMemory workingMemory,
                                    Consequence consequence,
                                    Tuple tuple)
    {
        super( workingMemory );

        this.consequence = consequence;
        this.tuple = tuple;
    }

    public Consequence getConsequence()
    {
        return this.consequence;
    }

    public Tuple getTuple()
    {
        return this.tuple;
    }

    public String toString()
    {
        return "[ActivationCancelled: rule=" + this.tuple.getRule( ).getName( ) + "; tuple=" + this.tuple + "]";
    }
}