package org.drools.spring.factory;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.WorkingMemory;
import org.drools.conflict.CompositeConflictResolver;
import org.drools.reteoo.FactHandleFactory;
import org.drools.spring.SpringTestSetup;

public class RuleBaseBuilderFactoryBeanTest extends TestCase {

    private static SpringTestSetup.ContextHolder contextHolder = new SpringTestSetup.ContextHolder();

    public static Test suite() {
        return new SpringTestSetup(
                new TestSuite(RuleBaseBuilderFactoryBeanTest.class),
                contextHolder, "org/drools/spring/factory/rulebasebuilder-test.appctx.xml");
    }

    private RuleBaseBuilderFactoryBean factory = new RuleBaseBuilderFactoryBean();

    public void testGetObjectType() throws Exception {
        assertSame(RuleBaseBuilder.class, factory.getObjectType());
    }

    public void testIsSingleton() throws Exception {
        assertTrue(factory.isSingleton());
        RuleBaseBuilderFactoryBean contextFactory = (RuleBaseBuilderFactoryBean) contextHolder.context.getBean("&ruleBaseBuilder");
        assertSame(contextFactory.getObject(), contextFactory.getObject());
    }

    private static class NullFactHandleFactory implements FactHandleFactory {
        public FactHandle newFactHandle() {
            return null;
        }
        public FactHandle newFactHandle(long id) {
            return null;
        }
    }

    public void testFromConfigFile() throws Exception {
        RuleBaseBuilder ruleBaseBuilder = (RuleBaseBuilder) contextHolder.context.getBean("ruleBaseBuilder");
        assertNotNull(ruleBaseBuilder);
        WorkingMemory workingMemory = (WorkingMemory) contextHolder.context.getBean("workingMemory");
        assertNotNull(workingMemory);

        RuleBase ruleBase = workingMemory.getRuleBase();
        List ruleSets = ruleBase.getRuleSets();
        assertEquals(3, ruleSets.size());

        // Weak, but all we can do with the CompositeConflictResolver api.
        assertTrue(ruleBase.getConflictResolver() instanceof CompositeConflictResolver);

        assertTrue(ruleBase.getFactHandleFactory() instanceof NullFactHandleFactory);
    }
}
