package org.drools.event;

import org.drools.FactHandle;
import org.drools.WorkingMemory;

public class ObjectModifiedEvent extends WorkingMemoryEvent {

    private final FactHandle handle;
    private final Object oldObject;
    private final Object object;

    public ObjectModifiedEvent(WorkingMemory workingMemory, FactHandle handle, Object oldObject,
            Object object) {
        super(workingMemory);
        this.handle = handle;
        this.oldObject = oldObject;
        this.object = object;
    }

    public FactHandle getFactHandle() {
        return this.handle;
    }

    public Object getOldObject() {
        return this.oldObject;
    }

    public Object getObject() {
        return this.object;
    }

    public String toString() {
        return "[ObjectModified: handle=" + this.handle + "; old_object=" + this.oldObject
                + "; new_object=" + this.object + "]";
    }
}
