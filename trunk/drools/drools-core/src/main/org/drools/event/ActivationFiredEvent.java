package org.drools.event;

import org.drools.WorkingMemory;
import org.drools.spi.Activation;

public class ActivationFiredEvent
    extends WorkingMemoryEvent
{
    private Activation activation;

    public ActivationFiredEvent(WorkingMemory workingMemory,
                                Activation activation)
    {
        super( workingMemory );

        this.activation = activation;
    }

    public Activation getActivation()
    {
        return this.activation;
    }
}
