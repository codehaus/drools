package org.drools.spring.event;

public class WorkingMemoryEventListenerProcessorDefaultWorkingMemoryTest extends WorkingMemoryEventListenerProcessorTestCase {

    protected String getWorkingMemoryBeanName() {
        return "workingMemory";
    }

    protected String getContextFilename() {
        return "org/drools/spring/event/default-workingmemory.appctx.xml";
    }
}
