package org.drools.event;

import org.drools.FactHandle;
import org.drools.WorkingMemory;

public class ObjectRetractedEvent extends WorkingMemoryEvent
{
    private final FactHandle handle;

    public ObjectRetractedEvent(WorkingMemory workingMemory, FactHandle handle)
    {
        super( workingMemory );
        this.handle = handle;
    }

    public FactHandle getFactHandle()
    {
        return this.handle;
    }

    public String toString()
    {
        return "[ObjectRetracted: handle=" + this.handle + "]";
    }
}