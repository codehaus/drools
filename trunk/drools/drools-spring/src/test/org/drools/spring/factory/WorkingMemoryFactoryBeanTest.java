package org.drools.spring.factory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.WorkingMemory;
import org.drools.spring.SpringTestSetup;

public class WorkingMemoryFactoryBeanTest extends TestCase {

    private static SpringTestSetup.ContextHolder contextHolder = new SpringTestSetup.ContextHolder();

    public static Test suite() {
        return new SpringTestSetup(
                new TestSuite(WorkingMemoryFactoryBeanTest.class),
                contextHolder, "org/drools/spring/factory/for-test.appctx.xml");
    }

    private WorkingMemoryFactoryBean factory = new WorkingMemoryFactoryBean();

    public void testGetObjectType() throws Exception {
        assertSame(WorkingMemory.class, factory.getObjectType());
    }

    public void testIsSingleton() throws Exception {
        assertTrue(factory.isSingleton());
        assertSame(
                contextHolder.context.getBean("workingMemory"),
                contextHolder.context.getBean("workingMemory"));
    }

    public void testRuleBaseNotSet() throws Exception {
        try {
            factory.afterPropertiesSet();
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void FIXME_testApplicationData() throws Exception {
        WorkingMemory workingMemory = (WorkingMemory) contextHolder.context.getBean("workingMemory");
        assertNotNull(workingMemory);

        assertEquals("this is a child comment", workingMemory.getApplicationData("childComment"));
        assertEquals("this is an adult comment", workingMemory.getApplicationData("adultComment"));
    }
}
