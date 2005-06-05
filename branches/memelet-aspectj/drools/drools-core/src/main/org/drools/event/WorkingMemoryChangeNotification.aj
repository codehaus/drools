package org.drools.event;

import org.drools.*;
import org.drools.spi.*;
import org.drools.reteoo.*;

public privileged aspect WorkingMemoryChangeNotification {

    private WorkingMemoryEventSupport WorkingMemory.eventSupport = new WorkingMemoryEventSupport(this);

    public void WorkingMemory.addListener(WorkingMemoryEventListener listener) {
        eventSupport.addEventListener(listener);
    }

    public void WorkingMemory.removeListener(WorkingMemoryEventListener listener) {
        eventSupport.removeEventListener(listener);
    }

    //---- ---- ----

    pointcut assertObject(WorkingMemory workingMemory, Object object):
        execution(FactHandle WorkingMemory+.assertObject(Object)) &&
        this(workingMemory) && args(object);

    after(WorkingMemory workingMemory, Object object) returning (FactHandle handle)
    : assertObject(workingMemory, object) {
        workingMemory.eventSupport.fireObjectAsserted(handle, object);
    }


    pointcut retractObject(WorkingMemory workingMemory, FactHandle handle):
        execution(Object WorkingMemory+.retractObject(FactHandle)) &&
        this(workingMemory) && args(handle);

    after(WorkingMemory workingMemory, FactHandle handle) returning (Object object)
    : retractObject(workingMemory, handle) {
        workingMemory.eventSupport.fireObjectRetracted(handle, object);
    }


    pointcut modifyObject(WorkingMemory workingMemory, FactHandle handle, Object object):
        execution(Object WorkingMemory+.modifyObject(FactHandle, Object)) &&
        this(workingMemory) && args(handle, object);

    after(WorkingMemory workingMemory, FactHandle handle, Object object) returning (Object prevObject)
    : modifyObject(workingMemory, handle, object) {
        workingMemory.eventSupport.fireObjectModified(handle, prevObject, object);
    }


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
        workingMemory.eventSupport.fireConditionTested(conditionNode.rule, condition, tuple, result);
    }




}
