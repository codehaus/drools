package org.drools.event;

import org.drools.*;
import org.drools.reteoo.*;

public aspect WorkingMemoryChangeNotification {

    private WorkingMemoryEventSupport WorkingMemory.eventSupport = new WorkingMemoryEventSupport(this);

    public void WorkingMemory.addListener(WorkingMemoryEventListener listener) {
        eventSupport.addEventListener(listener);
    }

    public void WorkingMemory.removeListener(WorkingMemoryEventListener listener) {
        eventSupport.removeEventListener(listener);
    }

    //---- ---- ----

    pointcut assertObject(WorkingMemory workingMemory, Object object)
    : execution(FactHandle WorkingMemory+.assertObject(Object))
        && this(workingMemory) && args(object);

    pointcut retractObject(WorkingMemory workingMemory, FactHandle handle)
    : execution(Object WorkingMemory+.retractObject(FactHandle))
        && this(workingMemory) && args(handle);

    pointcut modifyObject(WorkingMemory workingMemory, FactHandle handle, Object object)
    : execution(Object WorkingMemory+.modifyObject(FactHandle, Object))
        && this(workingMemory) && args(handle, object);


    after(WorkingMemory workingMemory, Object object) returning (FactHandle handle)
    : assertObject(workingMemory, object) {
        workingMemory.eventSupport.fireObjectAsserted(handle, object);
    }

    after(WorkingMemory workingMemory, FactHandle handle) returning (Object object)
    : retractObject(workingMemory, handle) {
        workingMemory.eventSupport.fireObjectRetracted(handle, object);
    }

    after(WorkingMemory workingMemory, FactHandle handle, Object object) returning (Object prevObject)
    : modifyObject(workingMemory, handle, object) {
        workingMemory.eventSupport.fireObjectModified(handle, prevObject, object);
    }



}
