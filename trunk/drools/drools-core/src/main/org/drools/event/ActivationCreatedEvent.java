package org.drools.event;

import org.drools.WorkingMemory;
import org.drools.spi.Consequence;
import org.drools.spi.Tuple;

public class ActivationCreatedEvent extends WorkingMemoryEvent
{
    private WorkingMemory workingMemory;

    private Consequence   consequence;

    private Tuple         tuple;

    public ActivationCreatedEvent(WorkingMemory workingMemory,
                                  Consequence consequence,
                                  Tuple tuple)
    {
        super( workingMemory );

        this.workingMemory = workingMemory;
        this.consequence = consequence;
        this.tuple = tuple;
    }

    public WorkingMemory getWorkingMemory()
    {
        return this.workingMemory;
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
        return "[ActivationCreated: rule=" + this.tuple.getRule( ).getName( )
               + "; tuple=" + this.tuple + "]";
    }
}