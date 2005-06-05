package org.drools.event;

import org.drools.*;
import org.drools.spi.*;
import org.drools.reteoo.*;

/**
 *
 */
public privileged aspect WorkingMemoryChangeNotification {

    private WorkingMemoryEventSupport WorkingMemory.eventSupport;

    private WorkingMemoryEventSupport WorkingMemory.getEventSupport() {
        if (eventSupport == null) {
            eventSupport = new WorkingMemoryEventSupport(this);
        }
        return eventSupport;
    }

    public void WorkingMemory.addListener(WorkingMemoryEventListener listener) {
        getEventSupport().addEventListener(listener);
    }

    public void WorkingMemory.removeListener(WorkingMemoryEventListener listener) {
        getEventSupport().removeEventListener(listener);
    }

    // It's important that we obtain eventSupport via this method to ensure it gets initialized.
    declare error :
        get(WorkingMemoryEventSupport WorkingMemory.eventSupport) &&
        !withincode(WorkingMemoryEventSupport WorkingMemory.getEventSupport())
        : "Only WorkingMemory.getEventSupport() may access field WorkingMemory.eventSupport";


    //---- ObjectAssertedEvent ----

    pointcut assertObject(WorkingMemory workingMemory, Object object):
        execution(FactHandle WorkingMemory+.assertObject(Object)) &&
        this(workingMemory) && args(object);

    after(WorkingMemory workingMemory, Object object) returning (FactHandle handle)
    : assertObject(workingMemory, object) {
        workingMemory.getEventSupport().fireObjectAsserted(handle, object);
    }

    //---- ObjectRetractedEvent ----

    pointcut retractObject(WorkingMemory workingMemory, FactHandle handle):
        execution(Object WorkingMemory+.retractObject(FactHandle)) &&
        this(workingMemory) && args(handle);

    after(WorkingMemory workingMemory, FactHandle handle) returning (Object object)
    : retractObject(workingMemory, handle) {
        workingMemory.getEventSupport().fireObjectRetracted(handle, object);
    }

    //---- ObjectModifiedEvent ----

    pointcut modifyObject(WorkingMemory workingMemory, FactHandle handle, Object object):
        execution(Object WorkingMemory+.modifyObject(FactHandle, Object)) &&
        this(workingMemory) && args(handle, object);

    after(WorkingMemory workingMemory, FactHandle handle, Object object) returning (Object prevObject)
    : modifyObject(workingMemory, handle, object) {
        workingMemory.getEventSupport().fireObjectModified(handle, prevObject, object);
    }

    //---- ConditionTestedEvent ----

    pointcut conditionNodeAssertTupleExecution(ConditionNode conditionNode, ReteTuple tuple, WorkingMemoryImpl workingMemory):
        execution(void org.drools.reteoo.ConditionNode.assertTuple(..)) &&
        this(conditionNode) && args(tuple, workingMemory);

    pointcut conditionTested(ConditionNode conditionNode, ReteTuple tuple, WorkingMemoryImpl workingMemory, Condition condition):
        cflow(conditionNodeAssertTupleExecution(conditionNode, tuple, workingMemory)) &&
        call(boolean Condition.isAllowed(..)) &&
        withincode(void org.drools.reteoo.ConditionNode.assertTuple(ReteTuple, WorkingMemoryImpl)) &&
        target(condition);

    after(ConditionNode conditionNode, ReteTuple tuple, WorkingMemoryImpl workingMemory, Condition condition)
    returning (boolean result)
    : conditionTested(conditionNode, tuple, workingMemory, condition) {
        workingMemory.getEventSupport().fireConditionTested(conditionNode.rule, condition, tuple, result);
    }




}
