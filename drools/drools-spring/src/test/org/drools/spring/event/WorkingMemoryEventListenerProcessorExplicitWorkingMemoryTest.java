package org.drools.spring.event;

public class WorkingMemoryEventListenerProcessorExplicitWorkingMemoryTest extends WorkingMemoryEventListenerProcessorTestCase {

    protected String getWorkingMemoryBeanName() {
        return "workingMemoryWithNonStandardName";
    }

    protected String getContextFilename() {
        return "org/drools/spring/event/explicit-workingmemory.appctx.xml";
    }
}
