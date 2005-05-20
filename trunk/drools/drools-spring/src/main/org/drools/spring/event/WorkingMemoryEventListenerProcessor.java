package org.drools.spring.event;

import org.drools.WorkingMemory;
import org.springframework.beans.factory.InitializingBean;

public class WorkingMemoryEventListenerProcessor extends AbstractWorkingMemoryEventListenerProcessor
                                                 implements InitializingBean {

    private WorkingMemory workingMemory;

    public void setWorkingMemory(WorkingMemory workingMemory) {
        this.workingMemory = workingMemory;
    }

    protected WorkingMemory getWorkingMemory() {
        return workingMemory;
    }

    public void afterPropertiesSet() throws Exception {
        if (workingMemory == null) {
            throw new IllegalArgumentException("WorkingMemory not set");
        }
    }
}
