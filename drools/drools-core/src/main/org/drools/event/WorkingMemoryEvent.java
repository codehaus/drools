package org.drools.event;

import java.util.EventObject;

import org.drools.WorkingMemory;

public class WorkingMemoryEvent extends EventObject
{
    public WorkingMemoryEvent(WorkingMemory workingMemory)
    {
        super( workingMemory );
    }

    public final WorkingMemory getWorkingMemory()
    {
        return (WorkingMemory) getSource( );
    }
}
