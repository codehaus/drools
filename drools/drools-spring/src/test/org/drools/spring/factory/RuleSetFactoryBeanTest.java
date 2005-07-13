package org.drools.spring.factory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spring.SpringTestSetup;
import org.drools.spring.factory.RuleSetFactoryBean;

public class RuleSetFactoryBeanTest extends TestCase {

    private static SpringTestSetup.ContextHolder contextHolder = new SpringTestSetup.ContextHolder();

    public static Test suite() {
        return new SpringTestSetup(
                new TestSuite(RuleSetFactoryBeanTest.class),
                contextHolder, "org/drools/spring/factory/rulesets.appctx.xml");
    }

    private RuleSetFactoryBean factory = new RuleSetFactoryBean();

    public void testGetObjectType() throws Exception {
        assertSame(RuleSet.class, factory.getObjectType());
    }

    public void testIsSingleton() throws Exception {
        assertTrue(factory.isSingleton());
        assertSame(
                contextHolder.context.getBean("ruleSet.beanRules"),
                contextHolder.context.getBean("ruleSet.beanRules"));
    }

    public void testBeanRules() throws Exception {
        RuleSet ruleSet = (RuleSet) contextHolder.context.getBean("ruleSet.beanRules");
        assertNotNull(ruleSet);
        assertEquals("ruleSet.beanRules.NAME", ruleSet.getName());
        assertEquals(2, ruleSet.getRules().length);

        Rule childRule = ruleSet.getRule("childRule");
        assertNotNull(childRule);
        assertEquals(10, childRule.getSalience());
        assertTrue(childRule.getNoLoop());
        assertEquals("childRule documentation", childRule.getDocumentation());

        Rule adultRule = ruleSet.getRule("adultRule-overrideBeanName");
        assertNotNull(adultRule);
    }

    public void testPojoRules() throws Exception {
        RuleSet ruleSet = (RuleSet) contextHolder.context.getBean("ruleSet.pojoRules");
        assertNotNull(ruleSet);
        assertEquals("ruleSet.pojoRules", ruleSet.getName());
        assertEquals(2, ruleSet.getRules().length);

        Rule childRule = ruleSet.getRule(ChildCommentRule.class.getName());
        assertNotNull(childRule);

        Rule adultRule = ruleSet.getRule(AdultCommentRule.class.getName());
        assertNotNull(adultRule);
    }
}
