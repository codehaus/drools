package org.drools.event;

import org.drools.WorkingMemory;

import java.util.EventObject;

public class WorkingMemoryEvent
    extends EventObject
{
    public WorkingMemoryEvent(WorkingMemory workingMemory)
    {
        super( workingMemory );
    }

    public WorkingMemory getWorkingMemory()
    {
        return (WorkingMemory) getSource();
    }
}
