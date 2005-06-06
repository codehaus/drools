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

public class WorkingMemoryEventListenerContextRefreshedRegistrarTest extends TestCase {

    public static class Listener_A extends DefaultWorkingMemoryEventListener { }
    public static class Listener_B extends DefaultWorkingMemoryEventListener { }

    public void testWorkingMemoryNotSet() throws Exception {
        WorkingMemoryEventListenerBeanPostProcessor listener = new WorkingMemoryEventListenerBeanPostProcessor();
        try {
            listener.afterPropertiesSet();
            fail("expected IllegalArgumentException");
        } catch (Exception e) {
            // expected
        }
    }

    public void testRegisterListeners() throws Exception {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("org/drools/spring/event/workingmemory-contextrefreshed.appctx.xml");
        WorkingMemory workingMemory = (WorkingMemory) context.getBean("workingMemory");

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
