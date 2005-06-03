package org.drools.event;

import junit.framework.TestCase;

import org.drools.FactHandle;
import org.easymock.container.CapturingArgumentsMatcher;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class WorkingMemoryEventSupportTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private Mock<FactHandle> mockFactHandle = mocks.createMock(FactHandle.class);
    private Mock<WorkingMemoryEventListener> mockWorkingMemoryEventListener = mocks.createMock(WorkingMemoryEventListener.class);
    private CapturingArgumentsMatcher capturingArgumentsMatcher = new CapturingArgumentsMatcher(mocks);

    private Object assertedObject = new Object(){};
    private Object modifiedObject = new Object(){};

    private ForTestWorkingMemory stubWorkingMemory = new ForTestWorkingMemory();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        stubWorkingMemory.assertObject_return = mockFactHandle.object;
        stubWorkingMemory.retractObject_return = assertedObject;
        stubWorkingMemory.modifyObject_return = assertedObject;
    }

    private void assertObjectAssertedEvent(ObjectAssertedEvent expectedEvent, int callCount) {
        assertTrue(capturingArgumentsMatcher.getActual(callCount)[0] instanceof ObjectAssertedEvent);
        ObjectAssertedEvent actualEvent = (ObjectAssertedEvent) capturingArgumentsMatcher.getActual(callCount)[0];
        assertSame(expectedEvent.getSource(), actualEvent.getSource());
        assertSame(expectedEvent.getFactHandle(), actualEvent.getFactHandle());
        assertSame(expectedEvent.getObject(), actualEvent.getObject());
    }

    private void assertObjectRetractedEvent(ObjectRetractedEvent expectedEvent, int callCount) {
        assertTrue(capturingArgumentsMatcher.getActual(callCount)[0] instanceof ObjectRetractedEvent);
        ObjectRetractedEvent actualEvent = (ObjectRetractedEvent) capturingArgumentsMatcher.getActual(callCount)[0];
        assertSame(expectedEvent.getSource(), actualEvent.getSource());
        assertSame(expectedEvent.getFactHandle(), actualEvent.getFactHandle());
        assertSame(expectedEvent.getOldObject(), actualEvent.getOldObject());
    }

    private void assertObjectModifiedEvent(ObjectModifiedEvent expectedEvent, int callCount) {
        assertTrue(capturingArgumentsMatcher.getActual(callCount)[0] instanceof ObjectModifiedEvent);
        ObjectModifiedEvent actualEvent = (ObjectModifiedEvent) capturingArgumentsMatcher.getActual(callCount)[0];
        assertSame(expectedEvent.getSource(), actualEvent.getSource());
        assertSame(expectedEvent.getFactHandle(), actualEvent.getFactHandle());
        assertSame(expectedEvent.getOldObject(), actualEvent.getOldObject());
        assertSame(expectedEvent.getObject(), actualEvent.getObject());
    }

    public void testObjectAsserted() throws Exception {
        ObjectAssertedEvent expectedEvent = new ObjectAssertedEvent(
                stubWorkingMemory, mockFactHandle.object, assertedObject);
        mockWorkingMemoryEventListener.object.objectAsserted(expectedEvent);
        mockWorkingMemoryEventListener.control.setMatcher(capturingArgumentsMatcher);
        mocks.replay();

        stubWorkingMemory.addListener(mockWorkingMemoryEventListener.object);
        stubWorkingMemory.assertObject(assertedObject);

        stubWorkingMemory.removeListener(mockWorkingMemoryEventListener.object);
        stubWorkingMemory.assertObject(assertedObject);

        mocks.verify();
        assertObjectAssertedEvent(expectedEvent, 0);
    }

    public void testRetractObject() throws Exception {
        ObjectRetractedEvent expectedEvent = new ObjectRetractedEvent(
                stubWorkingMemory, mockFactHandle.object, assertedObject);
        mockWorkingMemoryEventListener.object.objectRetracted(expectedEvent);
        mockWorkingMemoryEventListener.control.setMatcher(capturingArgumentsMatcher);
        mocks.replay();

        stubWorkingMemory.assertObject(assertedObject);
        stubWorkingMemory.addListener(mockWorkingMemoryEventListener.object);
        stubWorkingMemory.retractObject(mockFactHandle.object);

        mocks.verify();
        assertObjectRetractedEvent(expectedEvent, 0);
    }

    public void testObjectModified() throws Exception {
        ObjectModifiedEvent expectedEvent = new ObjectModifiedEvent(
                stubWorkingMemory, mockFactHandle.object, assertedObject, modifiedObject);
        mockWorkingMemoryEventListener.object.objectModified(expectedEvent);
        mockWorkingMemoryEventListener.control.setMatcher(capturingArgumentsMatcher);
        mocks.replay();

        stubWorkingMemory.assertObject(assertedObject);
        stubWorkingMemory.addListener(mockWorkingMemoryEventListener.object);
        stubWorkingMemory.modifyObject(mockFactHandle.object, modifiedObject);

        mocks.verify();
        assertObjectModifiedEvent(expectedEvent, 0);
    }
}