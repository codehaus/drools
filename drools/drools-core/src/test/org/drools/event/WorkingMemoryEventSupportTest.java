package org.drools.event;

import junit.framework.TestCase;

public class WorkingMemoryEventSupportTest extends TestCase {

    private ForTestWorkingMemory stubWorkingMemory = new ForTestWorkingMemory();

    public void testObjectAsserted() throws Exception {
        stubWorkingMemory.addListener(null);
    }
}