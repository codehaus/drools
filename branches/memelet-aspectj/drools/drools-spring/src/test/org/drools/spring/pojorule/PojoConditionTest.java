package org.drools.spring.pojorule;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.ConditionException;
import org.drools.spi.ObjectType;
import org.drools.spi.Tuple;
import org.drools.spring.pojorule.ApplicationDataArgument;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.FactArgument;
import org.drools.spring.pojorule.PojoCondition;
import org.drools.spring.pojorule.RuleReflectMethod;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class PojoConditionTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private MockControl newMockRuleMethod() {
        return mocks.createControl(RuleReflectMethod.class, new Class[] { Rule.class, Object.class,
                Method.class, Argument[].class }, new Object[] { null, null, null, null });
    }

    public void testGetRequiredTupleMembers() throws Exception {
        Rule rule = new Rule("for-test.declaration-factory");
        Argument[] parameterValues = new Argument[3];
        MockControl controlObjectType_1 = mocks.createControl(ObjectType.class);
        MockControl controlObjectType_2 = mocks.createControl(ObjectType.class);
        Declaration expectedDeclaration_1 = rule.addParameterDeclaration(
                "id-1", (ObjectType) controlObjectType_1.getMock());
        Declaration expectedDeclaration_2 = rule.addParameterDeclaration(
                "id-2", (ObjectType) controlObjectType_2.getMock());

        parameterValues[0] = new ApplicationDataArgument("name", Object.class);
        parameterValues[1] = new FactArgument(expectedDeclaration_1);
        parameterValues[2] = new FactArgument(expectedDeclaration_2);

        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndReturn(mockRuleMethod.getArguments(), parameterValues);

        mocks.replay();

        PojoCondition pojoCondition = new PojoCondition(mockRuleMethod);

        Declaration[] requiredTupleMembers = pojoCondition.getRequiredTupleMembers();

        mocks.verify();
        assertEquals(2, requiredTupleMembers.length);
        assertSame(expectedDeclaration_1, requiredTupleMembers[0]);
        assertSame(expectedDeclaration_2, requiredTupleMembers[1]);
    }

    public void testPojoMethodThrowsException() throws Exception {
        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndReturn(mockRuleMethod.getArguments(), new Argument[] {});
        controlRuleMethod.expectAndThrow(mockRuleMethod.invokeMethod(null), new RuntimeException("test"));

        mocks.replay();

        PojoCondition pojoCondition = new PojoCondition(mockRuleMethod);
        try {
            pojoCondition.isAllowed(null);
            fail("Expected ConditionException");
        } catch (ConditionException e) {
            // expected
        }

        mocks.verify();
    }

    public void testIsAllowed() throws Exception {
        MockControl controlTuple = mocks.createControl(Tuple.class);
        Tuple mockTuple = (Tuple)controlTuple.getMock();
        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndReturn(mockRuleMethod.getArguments(), new Argument[] {});
        controlRuleMethod.expectAndReturn(mockRuleMethod.invokeMethod(mockTuple), Boolean.TRUE);

        mocks.replay();

        PojoCondition pojoCondition = new PojoCondition(mockRuleMethod);
        pojoCondition.isAllowed(mockTuple);

        mocks.verify();
    }
}
