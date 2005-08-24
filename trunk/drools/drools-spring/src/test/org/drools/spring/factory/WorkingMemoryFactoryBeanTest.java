package org.drools.spring.factory;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */



import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.WorkingMemory;
import org.drools.event.DefaultWorkingMemoryEventListener;
import org.drools.event.WorkingMemoryEventListener;
import org.drools.spring.SpringTestSetup;

public class WorkingMemoryFactoryBeanTest extends TestCase {
        
    public static class Listener_A extends DefaultWorkingMemoryEventListener {}
    public static class Listener_B extends DefaultWorkingMemoryEventListener {}
    
    public static class Fact_A extends Object {}
    public static class Fact_B extends Object {}
    
    private static SpringTestSetup.ContextHolder contextHolder = new SpringTestSetup.ContextHolder();

    public static Test suite() {
        return new SpringTestSetup(
                new TestSuite(WorkingMemoryFactoryBeanTest.class),
                contextHolder, "org/drools/spring/factory/workingmemory.appctx.xml");
    }

    private WorkingMemoryFactoryBean workingMemoryFactoryBean;
    private WorkingMemory workingMemory_1;
    private WorkingMemory workingMemory_2;
    private WorkingMemory workingMemory_3;
    private WorkingMemoryEventListener listener_A;
    private WorkingMemoryEventListener listener_B;
    
    protected void setUp() throws Exception {
        super.setUp();
        workingMemoryFactoryBean = (WorkingMemoryFactoryBean) contextHolder.context.getBean("&workingMemory_1");
        workingMemory_1 = (WorkingMemory) contextHolder.context.getBean("workingMemory_1");
        workingMemory_2 = (WorkingMemory) contextHolder.context.getBean("workingMemory_2");
        workingMemory_3 = (WorkingMemory) contextHolder.context.getBean("workingMemory_3");
        listener_A = (WorkingMemoryEventListener) contextHolder.context.getBean("listener_A");
        listener_B = (WorkingMemoryEventListener) contextHolder.context.getBean("listener_B");
    }

    public void testGetObjectType() throws Exception {
        assertSame(WorkingMemory.class, workingMemoryFactoryBean.getObjectType());
    }

    public void testIsSingleton() throws Exception {
        assertTrue(workingMemoryFactoryBean.isSingleton());
        assertSame(
                contextHolder.context.getBean("workingMemory_1"),
                contextHolder.context.getBean("workingMemory_1"));
        assertSame(
                workingMemory_1,
                contextHolder.context.getBean("workingMemory_1"));
    }

    public void testRuleBaseNotSet() throws Exception {
        try {
            WorkingMemoryFactoryBean factory = new WorkingMemoryFactoryBean();
            factory.afterPropertiesSet();
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testSecondInstance() throws Exception {
        assertNotSame(workingMemory_2, workingMemory_1);
    }
    
    public void testAutoRegisterListeners_True() throws Exception {
        Set expectedListeners = new HashSet();
        expectedListeners.add(listener_A);
        expectedListeners.add(listener_B);
        
        List listeners = workingMemory_1.getEventListeners();
        assertEquals(2, listeners.size());
        for (Iterator iter = listeners.iterator(); iter.hasNext();) {
            WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
            assertTrue(expectedListeners.contains(listener));
        }
    }
    
    public void testAutoRegisterListeners_False() throws Exception {
        assertTrue(workingMemory_2.getEventListeners().isEmpty());
    }
    
    public void testExplicitRegisterListeners() {
        List listeners = workingMemory_3.getEventListeners();
        assertEquals(1, listeners.size());
        assertSame(listener_A, listeners.get(0));
    }

//sandbox    
//    public void testAssertFacts() throws Exception {
//        List facts = workingMemory_1.getObjects();
//        assertEquals(2, facts.size());
//        Set expectedFacts = new HashSet();
//        expectedFacts.add(contextHolder.context.getBean("fact_A"));
//        expectedFacts.add(contextHolder.context.getBean("fact_B"));
//        for (Iterator iter = facts.iterator(); iter.hasNext();) {
//            Object fact = iter.next();
//            assertTrue(expectedFacts.contains(fact));
//        }
//    }    
    
    public void FIXME_testApplicationData() throws Exception {
        WorkingMemory workingMemory = (WorkingMemory) contextHolder.context.getBean("workingMemory");
        assertNotNull(workingMemory);

        assertEquals("this is a child comment", workingMemory.getApplicationData("childComment"));
        assertEquals("this is an adult comment", workingMemory.getApplicationData("adultComment"));
    }
}

