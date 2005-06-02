package org.drools.spring.factory;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.conflict.CompositeConflictResolver;
import org.drools.reteoo.FactHandleFactory;
import org.drools.rule.RuleSet;
import org.drools.spring.SpringTestSetup;

public class RuleBaseFactoryBeanTest extends TestCase {

    private static SpringTestSetup.ContextHolder contextHolder = new SpringTestSetup.ContextHolder();

    public static Test suite() {
        return new SpringTestSetup(
                new TestSuite(RuleBaseFactoryBeanTest.class),
                contextHolder, "org/drools/spring/factory/for-test.appctx.xml");
    }

    private RuleBaseFactoryBean factory = new RuleBaseFactoryBean();

    public void testGetObjectType() throws Exception {
        assertSame(RuleBase.class, factory.getObjectType());
    }

    public void testIsSingleton() throws Exception {
        assertTrue(factory.isSingleton());
        RuleBaseFactoryBean contextFactory = (RuleBaseFactoryBean) contextHolder.context.getBean("&ruleBase");
        assertSame(contextFactory.getObject(), contextFactory.getObject());
    }

    public void testNoRuleSets() throws Exception {
        try {
            factory.afterPropertiesSet();
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testRuleSetsNotInstanceOfRuleSet() throws Exception {
        factory.setRuleSets(new ArrayList() {{
            add(new RuleSet("for-test"));
            add(new Object());
        }});
        try {
            factory.afterPropertiesSet();
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
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
        RuleBase ruleBase = (RuleBase) contextHolder.context.getBean("ruleBase");
        assertNotNull(ruleBase);

        List ruleSets = ruleBase.getRuleSets();
        assertEquals(2, ruleSets.size());

        // Weak, but all we can do with the CompositeConflictResolver api.
        assertTrue(ruleBase.getConflictResolver() instanceof CompositeConflictResolver);

        assertTrue(ruleBase.getFactHandleFactory() instanceof NullFactHandleFactory);
    }
}
