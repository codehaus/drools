package org.drools.spring.event;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.drools.WorkingMemory;
import org.drools.event.DefaultWorkingMemoryEventListener;
import org.drools.event.WorkingMemoryEventListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class WorkingMemoryEventListenerProcessorTestCase extends TestCase {

    public static class Listener_A extends DefaultWorkingMemoryEventListener { }
    public static class Listener_B extends DefaultWorkingMemoryEventListener { }

    protected abstract String getContextFilename();
    protected abstract String getWorkingMemoryBeanName();

    public void testRegisterListeners() throws Exception {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(getContextFilename());
        WorkingMemory workingMemory = (WorkingMemory) context.getBean(getWorkingMemoryBeanName());

        Set expectedListenerClasses = new HashSet();
        expectedListenerClasses.add(Listener_A.class);
        expectedListenerClasses.add(Listener_B.class);

        List listeners = workingMemory.getEventListeners();
        assertEquals(2, listeners.size());
        for (Iterator iter = listeners.iterator(); iter.hasNext();) {
            WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
            assertTrue(expectedListenerClasses.contains(listener.getClass()));
        }
    }
}
