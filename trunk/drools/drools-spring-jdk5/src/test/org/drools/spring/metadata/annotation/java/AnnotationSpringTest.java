package org.drools.spring.metadata.annotation.java;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spring.SpringTestSetup;

public class AnnotationSpringTest extends TestCase {

    private static SpringTestSetup.ContextHolder contextHolder = new SpringTestSetup.ContextHolder();

    public static Test suite() {
        return new SpringTestSetup(
                new TestSuite(AnnotationSpringTest.class),
                contextHolder, "org/drools/spring/metadata/annotation/java/for-test.appctx.xml");
    }

    public void testFromConfigFile() throws Exception {
        RuleSet ruleSet = (RuleSet) contextHolder.context.getBean("ruleSet");
        assertNotNull(ruleSet);
        assertEquals(4, ruleSet.getRules().length);

        assertRuleA(ruleSet);
        assertRuleB(ruleSet);
        assertRuleC(ruleSet);
        assertRuleD(ruleSet);
    }

    private void assertRuleA(RuleSet ruleSet) {
        Rule rule_A = ruleSet.getRule(TestRules.PojoRule_A.class.getName());
        assertNotNull(rule_A);
    }

    private void assertRuleB(RuleSet ruleSet) {
        Rule rule_B = ruleSet.getRule(TestRules.PojoRule_B.class.getName());
        assertNotNull(rule_B);
        assertEquals("my B docs", rule_B.getDocumentation());
        assertEquals(10, rule_B.getSalience());
    }

    private void assertRuleC(RuleSet ruleSet) {
        Rule rule_C = ruleSet.getRule("myC");
        assertNotNull(rule_C);
        assertEquals("my C docs", rule_C.getDocumentation());
        assertTrue(rule_C.getNoLoop());
    }

    private void assertRuleD(RuleSet ruleSet) {
        Rule rule_D = ruleSet.getRule(TestRules.PojoRule_D.class.getName());
        assertNotNull(rule_D);
    }
}
