package org.drools.event;

import org.drools.FactHandle;
import org.drools.WorkingMemory;

public class ObjectAssertedEvent extends WorkingMemoryEvent
{
    private final FactHandle handle;

    private final Object     object;

    public ObjectAssertedEvent(WorkingMemory workingMemory,
                               FactHandle handle,
                               Object object)
    {
        super( workingMemory );
        this.handle = handle;
        this.object = object;
    }

    public FactHandle getFactHandle()
    {
        return this.handle;
    }

    public Object getObject()
    {
        return this.object;
    }

    public String toString()
    {
        return "[ObjectAsserted: handle=" + this.handle + "; object="
               + this.object + "]";
    }
}