package org.drools.semantics.annotation.model;

import java.util.List;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.KnowledgeHelper;
import org.drools.semantics.annotation.*;
import org.drools.spi.Tuple;
import org.drools.semantics.annotation.Parameter;
import org.drools.semantics.annotation.ApplicationData;
import org.drools.semantics.annotation.Condition;
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
            @Condition
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
            @Condition
            public boolean condition(String parameter,
                                     @Parameter("object") Object object) {
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
            @Condition
            public boolean condition(KnowledgeHelper knowledgeHelper) {
                return true;
            }

            @Consequence
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
            @Condition
            public boolean condition(@Parameter("p1") @Deprecated String p) {
                return false;
            }
            @Consequence
            public void consequence() {}
        }
        Pojo pojo = new Pojo();

        builder.buildRule(rule, pojo);

        List<Declaration> declarations = rule.getParameterDeclarations();
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
            @Consequence
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
            @Consequence
            public void consequence(String parameter,
                                    @Parameter("object") Object object) {
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
            @Consequence
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
            @Consequence
            public void consequence(@Deprecated @ApplicationData("a2") String p) {}
        }
        Pojo1 pojo1 = new Pojo1();

        builder.buildRule(rule, pojo1);

        assertNotNull(rule.getConsequence());
    }

    public void testMultipleConsequenceMethods() throws Exception {
        class Pojo {
            public int consequenceOneCallCount;
            public int consequenceTwoCallCount;

            @Consequence
            public void consequenceOne() {
                consequenceOneCallCount++;
            }

            @Consequence
            public void consequenceTwo() {
                consequenceTwoCallCount++;
            }
        }
        Pojo pojo = new Pojo();
        Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );

        Rule returnedRule = builder.buildRule(rule, pojo);
        org.drools.spi.Consequence compositeConsequence = returnedRule.getConsequence();

        compositeConsequence.invoke(mockTuple.object);

        assertEquals(1, pojo.consequenceOneCallCount);
        assertEquals(1, pojo.consequenceTwoCallCount);
    }

    //---- ---- ----

    public void testBuild() throws Exception {
        class Pojo {
            @Condition
            public boolean conditionOne(
                    @Parameter("p1") String p1) {
                return true;
            }

            @Condition
            public boolean conditionTwo(
                    @Parameter("p1") String p1,
                    @Parameter("p2") Integer p2,
                    @ApplicationData("a1") Object a1) {
                return true;
            }

            @Consequence
            public void consequence(
                    KnowledgeHelper knowledgeHelper,
                    @Parameter("p1") String p1,
                    @ApplicationData("a1") Object a1) {}
        }
        Pojo pojo = new Pojo();

        Rule returnedRule = builder.buildRule(rule, pojo);

        assertSame(returnedRule, rule);

        List<Declaration> declarations = rule.getParameterDeclarations();
        assertEquals(2, declarations.size());
        assertEquals("p1", declarations.get(0).getIdentifier());
        assertEquals("p2", declarations.get(1).getIdentifier());

        List conditions = rule.getConditions();
        assertEquals(2, conditions.size());
        assertTrue(conditions.get(0) instanceof PojoCondition);
        assertTrue(conditions.get(1) instanceof PojoCondition);

        assertTrue(rule.getConsequence() instanceof PojoConsequence);

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
}
