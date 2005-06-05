package org.drools.event;

import org.drools.FactHandle;
import org.drools.WorkingMemory;

public class ObjectRetractedEvent extends WorkingMemoryEvent {

    private final FactHandle handle;
    private final Object oldObject;

    public ObjectRetractedEvent(WorkingMemory workingMemory, FactHandle handle, Object oldObject) {
        super(workingMemory);
        this.handle = handle;
        this.oldObject = oldObject;
    }

    public FactHandle getFactHandle() {
        return this.handle;
    }

    public Object getOldObject() {
        return this.oldObject;
    }

    public String toString() {
        return "[ObjectRetracted: handle=" + this.handle + "; old_object=" + this.oldObject + "]";
    }
}
