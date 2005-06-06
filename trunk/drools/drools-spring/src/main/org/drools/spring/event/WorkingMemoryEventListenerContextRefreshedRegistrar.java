package org.drools.spring.event;

import org.drools.WorkingMemory;

public class WorkingMemoryEventListenerContextRefreshedRegistrar extends AbstractWorkingMemoryContextRefreshedRegistrar {

    private WorkingMemory workingMemory;

    public void setWorkingMemory(WorkingMemory workingMemory) {
        this.workingMemory = workingMemory;
    }

    public WorkingMemory getWorkingMemory() {
        return workingMemory;
    }
}
