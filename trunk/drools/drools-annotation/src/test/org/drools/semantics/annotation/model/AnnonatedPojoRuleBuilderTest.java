package org.drools.semantics.annotation.model;

import java.util.List;

import org.drools.DroolsException;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.semantics.annotation.*;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;

import junit.framework.TestCase;

public class AnnonatedPojoRuleBuilderTest extends TestCase 
{
    private Rule rule = new Rule("test");
    
    private AnnonatedPojoRuleBuilder builder = new AnnonatedPojoRuleBuilder();

    public void testInvalidConsequenceReturnType() throws Exception {
        class Pojo {
            @Drools.Consequence
            public int consequence() {
                return 1;
            }
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }

    public void testInvalidConditionReturnType() throws Exception {
        class Pojo {
            @Drools.Condition
            public void condition() {}
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }
    
    public void testMultipleConsequences() throws Exception {
        class Pojo {
            @Drools.Consequence
            public void consequenceOne() {}

            @Drools.Consequence
            public void consequenceTwo() {}
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }
    
    public void testMissingConsequence() throws Exception {
        class Pojo {
            public void consequence() {
                // did not annotate!
            }
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }
    
    public void testConflictingConsequenceParameterAnnotation() throws Exception {
        class Pojo1 {
            @Drools.Consequence
            public void consequence(@Drools.Parameter("p1") @Drools.ApplicationData("a2") String p) {}
        }
        Pojo1 pojo1 = new Pojo1();

        try {
            builder.buildRule(rule, pojo1);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }

        class Pojo2 {
            @Drools.Consequence
            public void consequence(@Drools.ApplicationData("a1") @Drools.Parameter("p1") String p) {}
        }
        Pojo2 pojo2 = new Pojo2();

        try {
            builder.buildRule(rule, pojo2);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }

    public void testNonConflictingConsequenceParameterAnnotation() throws Exception {
        class Pojo1 {
            @Drools.Consequence
            public void consequence(@Deprecated @Drools.ApplicationData("a2") String p) {}
        }
        Pojo1 pojo1 = new Pojo1();

        builder.buildRule(rule, pojo1);

        assertNotNull(rule.getConsequence());
    }

    public void testConflictingConditionParameterAnnotation() throws Exception {
        class Pojo {
            @Drools.Condition
            public boolean condition(@Drools.Parameter("p1") @Drools.ApplicationData("p2") String p) {
                return false;
            }
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }

    public void testNonConflictingConditionParameterAnnotation() throws Exception {
        class Pojo {
            @Drools.Condition
            public boolean condition(@Drools.Parameter("p1") @Deprecated String p) {
                return false;
            }
            @Drools.Consequence
            public void consequence() {}
        }
        Pojo pojo = new Pojo();
        
        builder.buildRule(rule, pojo);
        
        List<Declaration> declarations = getRuleParameterDeclarations(rule);
        assertEquals(1, declarations.size());
    }

    public void testUnannoatedConsequenceParameter() throws Exception {
        class Pojo {
            @Drools.Consequence
            public void consequence(String parameter,
                                    @Drools.Parameter("object") Object object) {
            }
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }
    
    public void testUnannoatedConditionParameter() throws Exception {
        class Pojo {
            @Drools.Condition
            public boolean condition(String parameter,
                                     @Drools.Parameter("object") Object object) {
                return true;
            }
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }

    public void testMultipleConsequenceDroolsParameters() throws Exception {
        class Pojo {
            @Drools.Consequence
            public void consequence(DroolsContext drools1, DroolsContext drools2) {}
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }

    public void testConditionDroolsParameter() throws Exception {
        class Pojo {
            @Drools.Condition
            public boolean condition(DroolsContext drools) {
                return true;
            }

            @Drools.Consequence
            public void consequence() {}
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (DroolsException e) {
            // expected
        }
    }

    public void testBuild() throws Exception {
        class Pojo {
            @Drools.Condition
            public boolean conditionOne(
                    @Drools.Parameter("p1") String p1) {
                return true;
            }
            
            @Drools.Condition
            public boolean conditionTwo(
                    @Drools.Parameter("p1") String p1,
                    @Drools.Parameter("p2") Integer p2,
                    @Drools.ApplicationData("a1") Object a1) {
                return true;
            }
            
            @Drools.Consequence
            public void consequence(
                    DroolsContext drools,
                    @Drools.Parameter("p1") String p1,
                    @Drools.ApplicationData("a1") Object a1) {}
        }
        Pojo pojo = new Pojo();
        
        Rule returnedRule = builder.buildRule(rule, pojo);
        
        assertSame(returnedRule, rule);
        
        List<Declaration> declarations = getRuleParameterDeclarations(rule);
        assertEquals(2, declarations.size());
        assertEquals("p1", declarations.get(0).getIdentifier());
        assertEquals("p2", declarations.get(1).getIdentifier());
        
        List<Condition> conditions = getRuleConditions(rule);
        assertEquals(2, conditions.size());
        assertTrue(conditions.get(0) instanceof PojoCondition);
        assertTrue(conditions.get(1) instanceof PojoCondition);
        
        Consequence consequence = rule.getConsequence();
        assertTrue(consequence instanceof PojoConsequence);

        // We really can't test anymore unless we create MethodElementFactory
        // interface,
        // mock it out and inject the mock into the populator, capture all the
        // arguments,
        // write comparators, etc. That would certainly improve the unit-test,
        // but it would
        // also be complex, and not really worth the effort considering all this
        // behavior
        // will be covered in the functional tests. But we'll see. If defects
        // are found
        // then we'll know I was wrong.
    }

    @SuppressWarnings("unchecked")
    private static List<Condition> getRuleConditions(Rule rule) {
        return rule.getConditions();
    }

    @SuppressWarnings("unchecked")
    private static List<Declaration> getRuleParameterDeclarations(Rule rule) {
        return rule.getParameterDeclarations();
    }
}
