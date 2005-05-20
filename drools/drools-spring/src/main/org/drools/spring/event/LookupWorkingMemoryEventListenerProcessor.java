package org.drools.spring.event;

import org.drools.WorkingMemory;

public class LookupWorkingMemoryEventListenerProcessor extends AbstractWorkingMemoryEventListenerProcessor {

    protected WorkingMemory getWorkingMemory() {
        throw new UnsupportedOperationException("should be overriden by <lookup-method>");
    }

}
