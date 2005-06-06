package org.drools.spring.event;

import org.drools.WorkingMemory;

public class LookupWorkingMemoryEventListenerBeanFactoryPostProcessor extends AbstractWorkingMemoryEventListenerBeanFactoryPostProcessor {

    protected WorkingMemory getWorkingMemory() {
        throw new UnsupportedOperationException("should be overriden by <lookup-method>");
    }

}
