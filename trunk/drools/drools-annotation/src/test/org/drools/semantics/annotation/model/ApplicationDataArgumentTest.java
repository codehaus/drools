package org.drools.semantics.annotation.model;

import junit.framework.TestCase;

import org.drools.WorkingMemory;
import org.drools.semantics.annotation.model.ApplicationDataArgument;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class ApplicationDataArgumentTest extends TestCase {
    
    private EasymockContainer mocks = new EasymockContainer();

    private static final String APP_DATA_NAME = "app-data-name";

    private Mock<WorkingMemory> mockWorkingMemory = mocks.createMock(WorkingMemory.class);
    private Mock<Tuple> mockTuple = mocks.createMock(Tuple.class);

    public void testConstructionNullDeclaration() {
        try {
            ApplicationDataArgument arg = new ApplicationDataArgument(
                    null, Integer.class);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testConstructionNullClass() {
        try {
            ApplicationDataArgument arg = new ApplicationDataArgument("name", null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testGetValue() throws Exception {
        ApplicationDataArgument arg = new ApplicationDataArgument(
                APP_DATA_NAME, Integer.class);
        Integer expectedValue = new Integer(1);

        mockTuple.control.expectAndReturn(mockTuple.object.getWorkingMemory(),
                mockWorkingMemory.object);
        mockWorkingMemory.control.expectAndReturn(mockWorkingMemory.object
                .getApplicationData("app-data-name"), expectedValue);

        mocks.replay();

        arg.getValue(mockTuple.object);

        mocks.verify();
    }

    public void testGetValueWrongClass() throws Exception {
        ApplicationDataArgument arg = new ApplicationDataArgument(
                APP_DATA_NAME, Integer.class);
        String expectedValue = "value";

        mockTuple.control.expectAndReturn(mockTuple.object.getWorkingMemory(),
                mockWorkingMemory.object);
        mockWorkingMemory.control.expectAndReturn(mockWorkingMemory.object
                .getApplicationData("app-data-name"), expectedValue);

        mocks.replay();

        try {
            arg.getValue(mockTuple.object);
            fail("expected RuntimeException");
        } catch (RuntimeException e) {
            // expected
        }

        mocks.verify();
    }
}
