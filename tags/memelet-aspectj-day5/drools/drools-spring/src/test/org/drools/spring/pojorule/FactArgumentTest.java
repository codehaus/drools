package org.drools.spring.pojorule;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Tuple;
import org.drools.spring.pojorule.FactArgument;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

import junit.framework.TestCase;

public class FactArgumentTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private static class ValueClass extends Object {}

    private Declaration declaration;
    private ValueClass expectedValue = new ValueClass();
    private MockControl controlTuple = mocks.createControl(Tuple.class);
    private Tuple mockTuple = (Tuple) controlTuple.getMock();

    private FactArgument arg;

    protected void setUp() throws Exception {
        Rule rule = new Rule("for-test.declaration-factory");
        declaration = rule.addParameterDeclaration("parameter-name", null);

        arg = new FactArgument(declaration);
    }

    public void testGetDeclaration() throws Exception {
        assertSame(declaration, arg.getDeclaration());
    }

    public void testGetValue() throws Exception {
        controlTuple.expectAndReturn(mockTuple.get(declaration), expectedValue);
        mocks.replay();

        Object value = arg.getValue(mockTuple);

        mocks.verify();
        assertEquals(expectedValue, value);
    }
}
