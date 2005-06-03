package org.drools.spring.pojorule;

import junit.framework.TestCase;

import org.drools.WorkingMemory;
import org.drools.spi.Tuple;
import org.drools.spring.pojorule.ApplicationDataArgument;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class ApplicationDataArgumentTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private static class ValueClass extends Object {}
    private ValueClass expectedValue = new ValueClass();
    private MockControl controlTuple = mocks.createControl(Tuple.class);
    private Tuple mockTuple = (Tuple) controlTuple.getMock();
    private MockControl controlWorkingMemory = mocks.createControl(WorkingMemory.class);
    private WorkingMemory mockWorkingMemory = (WorkingMemory) controlWorkingMemory.getMock();

    private ApplicationDataArgument arg = new ApplicationDataArgument("id", ValueClass.class);

    public void testGetValue() throws Exception {
        controlTuple.expectAndReturn(mockTuple.getWorkingMemory(), mockWorkingMemory);
        controlWorkingMemory.expectAndReturn(mockWorkingMemory.getApplicationData("id"), expectedValue);

        mocks.replay();

        Object value = arg.getValue(mockTuple);

        mocks.verify();
        assertEquals(expectedValue, value);
    }

    public void testGetValueNotExpectedClass() throws Exception {
        controlTuple.expectAndReturn(mockTuple.getWorkingMemory(), mockWorkingMemory);
        controlWorkingMemory.expectAndReturn(mockWorkingMemory.getApplicationData("id"), new Object());

        mocks.replay();

        try {
            arg.getValue(mockTuple);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        mocks.verify();
    }
}
