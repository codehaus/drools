package org.drools.semantics.annotation.model;

import java.util.List;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.KnowledgeHelper;
import org.drools.semantics.annotation.*;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.Tuple;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.DroolsApplicationData;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.model.AnnonatedPojoRuleBuilder.InvalidReturnTypeException;
import org.drools.semantics.annotation.model.AnnonatedPojoRuleBuilder.InvalidParameterException;
import org.drools.semantics.annotation.model.AnnonatedPojoRuleBuilder.MissingConsequenceMethodException;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

import junit.framework.TestCase;

public class AnnonatedPojoRuleBuilderTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );
    private Rule rule = new Rule("test");

    private AnnonatedPojoRuleBuilder builder = new AnnonatedPojoRuleBuilder();

    //---- Condition

    public void testConditionInvalidReturnType() throws Exception {
        class Pojo {
            @DroolsCondition
            public void condition() {}
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected InvalidReturnTypeException");
        } catch (InvalidReturnTypeException e) {
            // expected
        }
    }

    public void testConditionUnannoatedParameter() throws Exception {
        class Pojo {
            @DroolsCondition
            public boolean condition(String parameter,
                                     @DroolsParameter("object") Object object) {
                return true;
            }
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected InvalidParameterException");
        } catch (InvalidParameterException e) {
            // expected
        }
    }

    public void testConditionIllegalKnowledgeHelperParameter() throws Exception {
        class Pojo {
            @DroolsCondition
            public boolean condition(KnowledgeHelper knowledgeHelper) {
                return true;
            }

            @DroolsConsequence
            public void consequence() {}
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected InvalidParameterException");
        } catch (InvalidParameterException e) {
            // expected
        }
    }

    public void testConditionParameterAnnotation() throws Exception {
        class Pojo {
            @DroolsCondition
            public boolean condition(@DroolsParameter("p1") @Deprecated String p) {
                return false;
            }
            @DroolsConsequence
            public void consequence() {}
        }
        Pojo pojo = new Pojo();

        builder.buildRule(rule, pojo);

        List<Declaration> declarations = getRuleParameterDeclarations(rule);
        assertEquals(1, declarations.size());
    }

    //---- Consequence

    public void testConsequenceNoMethod() throws Exception {
        class Pojo {
            public void consequence() {
                // did not annotate!
            }
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected MissingConsequenceMethodException");
        } catch (MissingConsequenceMethodException e) {
            // expected
        }
    }

    public void testConsequenceInvalidReturnType() throws Exception {
        class Pojo {
            @DroolsConsequence
            public int consequence() {
                return 1;
            }
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected DroolsException");
        } catch (InvalidReturnTypeException e) {
            // expected
        }
    }

    public void testConsequenceUnannoatedParameter() throws Exception {
        class Pojo {
            @DroolsConsequence
            public void consequence(String parameter,
                                    @DroolsParameter("object") Object object) {
            }
        }
        Pojo pojo = new Pojo();

        try {
            builder.buildRule(rule, pojo);
            fail("Expected InvalidParameterException");
        } catch (InvalidParameterException e) {
            // expected
        }
    }

    public void testConsequenceMultipleKnowledgeHelperParameters() throws Exception {
        class Pojo {
            @DroolsConsequence
            public void consequence(KnowledgeHelper kh1, KnowledgeHelper kn2) {}
        }
        Pojo pojo = new Pojo();


        try {
            builder.buildRule(rule, pojo);
            fail("Expected InvalidParameterException");
        } catch (InvalidParameterException e) {
            // expected
        }
    }

    public void testConsequenceParameterAnnotation() throws Exception {
        class Pojo1 {
            @DroolsConsequence
            public void consequence(@Deprecated @DroolsApplicationData("a2") String p) {}
        }
        Pojo1 pojo1 = new Pojo1();

        builder.buildRule(rule, pojo1);

        assertNotNull(rule.getConsequence());
    }

    public void testMultipleConsequenceMethods() throws Exception {
        class Pojo {
            public int consequenceOneCallCount;
            public int consequenceTwoCallCount;

            @DroolsConsequence
            public void consequenceOne() {
                consequenceOneCallCount++;
            }

            @DroolsConsequence
            public void consequenceTwo() {
                consequenceTwoCallCount++;
            }
        }
        Pojo pojo = new Pojo();
        Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );

        Rule returnedRule = builder.buildRule(rule, pojo);
        Consequence compositeConsequence = returnedRule.getConsequence();

        compositeConsequence.invoke(mockTuple.object);

        assertEquals(1, pojo.consequenceOneCallCount);
        assertEquals(1, pojo.consequenceTwoCallCount);
    }

    //---- ---- ----

    public void testBuild() throws Exception {
        class Pojo {
            @DroolsCondition
            public boolean conditionOne(
                    @DroolsParameter("p1") String p1) {
                return true;
            }

            @DroolsCondition
            public boolean conditionTwo(
                    @DroolsParameter("p1") String p1,
                    @DroolsParameter("p2") Integer p2,
                    @DroolsApplicationData("a1") Object a1) {
                return true;
            }

            @DroolsConsequence
            public void consequence(
                    KnowledgeHelper knowledgeHelper,
                    @DroolsParameter("p1") String p1,
                    @DroolsApplicationData("a1") Object a1) {}
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
