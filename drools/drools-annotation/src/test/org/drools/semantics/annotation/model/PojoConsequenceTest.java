package org.drools.semantics.annotation.model;

import java.lang.reflect.Method;

import org.drools.rule.Rule;
import org.drools.semantics.annotation.model.PojoConsequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

import junit.framework.TestCase;

public class PojoConsequenceTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );

    private Mock<RuleReflectMethod> newMockRuleMethod() {
        return mocks.createMock(
                RuleReflectMethod.class,
                new Class[] {Rule.class, Object.class, Method.class, Argument[].class},
                new Object[] {null, null, null, null});
    }

    public void testPojoMethodThrowsException() throws Exception {
        Mock<Tuple> mockTuple = mocks.createMock(Tuple.class);
        Mock<RuleReflectMethod> mockRuleMethod = newMockRuleMethod();
        mockRuleMethod.control.expectAndThrow(
                mockRuleMethod.object.invokeMethod(mockTuple.object),
                new RuntimeException("test"));
        RuleReflectMethod[] mockRuleMethods = new RuleReflectMethod[]{mockRuleMethod.object};

        mocks.replay();

        PojoConsequence pojoConsequence = new PojoConsequence(mockRuleMethods);
        try {
            pojoConsequence.invoke(mockTuple.object);
            fail( "Expected ConsequenceException" );
        }
        catch (ConsequenceException e) {
            // expected
        }

        mocks.verify();
    }

    public void testInvoke() throws Exception {
        Mock<Tuple> mockTuple = mocks.createMock(Tuple.class);
        Mock<RuleReflectMethod> mockRuleMethod = newMockRuleMethod();
        mockRuleMethod.control.expectAndReturn(
                mockRuleMethod.object.invokeMethod(mockTuple.object), null);
        RuleReflectMethod[] mockRuleMethods = new RuleReflectMethod[]{mockRuleMethod.object};

        mocks.replay();

        PojoConsequence pojoConsequence = new PojoConsequence(mockRuleMethods);
        pojoConsequence.invoke(mockTuple.object);

        mocks.verify();
    }

    public void testInvokeMulti() throws Exception {
        Mock<Tuple> mockTuple = mocks.createMock(Tuple.class);
        Mock<RuleReflectMethod> mockRuleMethod_1 = newMockRuleMethod();
        mockRuleMethod_1.control.expectAndReturn(
                mockRuleMethod_1.object.invokeMethod(mockTuple.object), null);
        Mock<RuleReflectMethod> mockRuleMethod_2 = newMockRuleMethod();
        mockRuleMethod_2.control.expectAndReturn(
                mockRuleMethod_2.object.invokeMethod(mockTuple.object), null);
        RuleReflectMethod[] mockRuleMethods = new RuleReflectMethod[]{
                mockRuleMethod_1.object, mockRuleMethod_2.object};

        mocks.replay();

        PojoConsequence pojoConsequence = new PojoConsequence(mockRuleMethods);
        pojoConsequence.invoke(mockTuple.object);

        mocks.verify();
    }
}
