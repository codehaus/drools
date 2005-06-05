package org.drools.event;

import junit.framework.TestCase;

import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.reteoo.ConditionNode;
import org.drools.reteoo.ReteTuple;
import org.drools.reteoo.TupleSource;
import org.drools.reteoo.WorkingMemoryImpl;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.Tuple;
import org.easymock.container.CapturingArgumentsMatcher;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class WorkingMemoryEventSupportTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private Mock<FactHandle> mockFactHandle = mocks.createMock(FactHandle.class);
    private Mock<WorkingMemoryEventListener> mockWorkingMemoryEventListener = mocks.createMock(WorkingMemoryEventListener.class);
    private CapturingArgumentsMatcher capturingArgumentsMatcher = new CapturingArgumentsMatcher(mocks);

    private Mock<Tuple> mockTuple = mocks.createMock(Tuple.class);
    private Mock<ReteTuple> mockReteTuple = mocks.createMock(ReteTuple.class);
    private Mock<TupleSource> mockTupleSource = mocks.createMock(TupleSource.class);
    private Mock<Rule> mockRule = mocks.createMock(Rule.class);
    private Mock<Condition> mockCondition = mocks.createMock(Condition.class);
    private Mock<WorkingMemoryImpl> mockWorkingMemoryImpl = mocks.createMock(WorkingMemoryImpl.class);

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

    private void assertConditionTestedEvent(ConditionTestedEvent expectedEvent, int callCount) {
        assertTrue(capturingArgumentsMatcher.getActual(callCount)[0] instanceof ConditionTestedEvent);
        ConditionTestedEvent actualEvent = (ConditionTestedEvent) capturingArgumentsMatcher.getActual(callCount)[0];
        assertSame(expectedEvent.getSource(), actualEvent.getSource());
        assertSame(expectedEvent.getRule(), actualEvent.getRule());
        assertSame(expectedEvent.getCondition(), actualEvent.getCondition());
        assertSame(expectedEvent.getTuple(), actualEvent.getTuple());
        assertEquals(expectedEvent.getPassed(), actualEvent.getPassed());
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

    public void testConditionTested() throws Exception {
        ConditionTestedEvent expectedEvent = new ConditionTestedEvent(
                mockWorkingMemoryImpl.object, mockRule.object, mockCondition.object, mockReteTuple.object, false);
        mockCondition.control.expectAndReturn(
                mockCondition.object.isAllowed(mockReteTuple.object), false);
        mockWorkingMemoryEventListener.object.conditionTested(expectedEvent);
        mockWorkingMemoryEventListener.control.setMatcher(capturingArgumentsMatcher);

        ConditionNode conditionNode = new ConditionNode(mockRule.object, mockTupleSource.object, mockCondition.object);

        mocks.replay();

        conditionNode.assertTuple(mockReteTuple.object, mockWorkingMemoryImpl.object);

        mocks.verify();
        assertConditionTestedEvent(expectedEvent, 0);
    }
}