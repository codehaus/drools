package org.drools.spring.pojorule;

import java.lang.reflect.Method;

import org.drools.rule.Rule;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.PojoConsequence;
import org.drools.spring.pojorule.RuleReflectMethod;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

import junit.framework.TestCase;

public class PojoConsequenceTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer( );

    private MockControl newMockRuleMethod() {
        return mocks.createControl(
                RuleReflectMethod.class,
                new Class[] {Rule.class, Object.class, Method.class, Argument[].class},
                new Object[] {null, null, null, null});
    }

    public void testPojoMethodThrowsException() throws Exception {
        MockControl controlTuple = mocks.createControl(Tuple.class);
        Tuple mockTuple = (Tuple)controlTuple.getMock();
        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndThrow(mockRuleMethod.invokeMethod(mockTuple),
                new RuntimeException("test-exception"));
        RuleReflectMethod[] mockRuleMethods = new RuleReflectMethod[]{mockRuleMethod};

        mocks.replay();

        PojoConsequence pojoConsequence = new PojoConsequence(mockRuleMethods);
        try {
            pojoConsequence.invoke(mockTuple);
            fail( "Expected ConsequenceException" );
        }
        catch (ConsequenceException e) {
            // expected
        }

        mocks.verify();
    }

    public void testInvoke() throws Exception {
        MockControl controlTuple = mocks.createControl(Tuple.class);
        Tuple mockTuple = (Tuple)controlTuple.getMock();
        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndReturn(mockRuleMethod.invokeMethod(mockTuple), null);
        RuleReflectMethod[] mockRuleMethods = new RuleReflectMethod[]{mockRuleMethod};

        mocks.replay();

        PojoConsequence pojoConsequence = new PojoConsequence(mockRuleMethods);
        pojoConsequence.invoke(mockTuple);

        mocks.verify();
    }

    public void testInvokeMulti() throws Exception {
        MockControl controlTuple = mocks.createControl(Tuple.class);
        Tuple mockTuple = (Tuple)controlTuple.getMock();
        MockControl controlRuleMethod_1 = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod_1 = (RuleReflectMethod) controlRuleMethod_1.getMock();
        controlRuleMethod_1.expectAndReturn(mockRuleMethod_1.invokeMethod(mockTuple), null);
        MockControl controlRuleMethod_2 = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod_2 = (RuleReflectMethod) controlRuleMethod_2.getMock();
        controlRuleMethod_2.expectAndReturn(mockRuleMethod_2.invokeMethod(mockTuple), null);
        RuleReflectMethod[] mockRuleMethods = new RuleReflectMethod[]{mockRuleMethod_1, mockRuleMethod_2};

        mocks.replay();

        PojoConsequence pojoConsequence = new PojoConsequence(mockRuleMethods);
        pojoConsequence.invoke(mockTuple);

        mocks.verify();
    }
}
