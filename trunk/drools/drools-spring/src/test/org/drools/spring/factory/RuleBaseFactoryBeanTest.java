package org.drools.spring.factory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.RuleBase;
import org.drools.conflict.CompositeConflictResolver;
import org.drools.rule.RuleSet;
import org.drools.spring.SpringTestSetup;

public class RuleBaseFactoryBeanTest extends TestCase {

    private static SpringTestSetup.ContextHolder contextHolder = new SpringTestSetup.ContextHolder();

    public static Test suite() {
        return new SpringTestSetup(
                new TestSuite(RuleBaseFactoryBeanTest.class),
                contextHolder, "org/drools/spring/factory/rulebase.appctx.xml");
    }

    private RuleBaseFactoryBean ruleBaseFactoryBean;
    private RuleBase ruleBase;
    private RuleBase autoDetectRuleBase;
    
    protected void setUp() throws Exception {
        super.setUp();
        ruleBaseFactoryBean = (RuleBaseFactoryBean) contextHolder.context.getBean("&ruleBase");
        ruleBase = (RuleBase) contextHolder.context.getBean("ruleBase");
        autoDetectRuleBase = (RuleBase) contextHolder.context.getBean("ruleBase.autoDetect");
    }

    public void testGetObjectType() throws Exception {
        assertSame(RuleBase.class, ruleBaseFactoryBean.getObjectType());
    }

    public void testIsSingleton() throws Exception {
        assertTrue(ruleBaseFactoryBean.isSingleton());
        assertSame(ruleBaseFactoryBean.getObject(), ruleBaseFactoryBean.getObject());
    }

    public void testRuleSets() throws Exception {
        assertNotNull(ruleBase);
        List ruleSets = ruleBase.getRuleSets();
        assertEquals(2, ruleSets.size());
        Set ruleSetNames = new HashSet();
        ruleSetNames.add(((RuleSet)ruleSets.get(0)).getName());
        ruleSetNames.add(((RuleSet)ruleSets.get(1)).getName());
        assertTrue(ruleSetNames.contains("ruleSet.IOCRules.NAME"));
        assertTrue(ruleSetNames.contains("ruleSet.HardCodedRules"));
    }
    
    public void testAutoDetectRuleSets() throws Exception {
        assertNotNull(autoDetectRuleBase);
        List ruleSets = autoDetectRuleBase.getRuleSets();
        assertEquals(2, ruleSets.size());
        Set ruleSetNames = new HashSet();
        ruleSetNames.add(((RuleSet)ruleSets.get(0)).getName());
        ruleSetNames.add(((RuleSet)ruleSets.get(1)).getName());
        assertTrue(ruleSetNames.contains("ruleSet.IOCRules.NAME"));
        assertTrue(ruleSetNames.contains("ruleSet.HardCodedRules"));
    }
    
    public void testConflictResolver() throws Exception {
        assertTrue(ruleBase.getConflictResolver() instanceof CompositeConflictResolver);
    }

    public void testFactHandle() throws Exception {
        assertTrue(ruleBase.getFactHandleFactory() instanceof NullFactHandleFactory);
    }
}
