package org.drools.reteoo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;

import org.drools.WorkingMemory;
import org.drools.FactHandle;

import org.drools.event.WorkingMemoryEventListener;
import org.drools.event.ObjectAssertedEvent;
import org.drools.event.ObjectModifiedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.ConditionTestedEvent;
import org.drools.event.ActivationCreatedEvent;
import org.drools.event.ActivationFiredEvent;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.Tuple;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public aspect DroolsEventModel
{

    /** assertObject pointcut */
    pointcut assertObject(WorkingMemory workingMemory, Object object):
        execution(FactHandle WorkingMemory+.assertObject(Object))
        && target(workingMemory)
        && args(object)
        && if(workingMemory.getClass() != WorkingMemory.class);

    /** modifyObject crosspoint */
    pointcut modifyObject(WorkingMemory workingMemory, FactHandle handle, Object object):
        execution(void WorkingMemory+.modifyObject(FactHandle, Object))
        && target(workingMemory)
        && args(handle, object)
        && if(workingMemory.getClass() != WorkingMemory.class);

    /** retractObject crosspoint */
    pointcut retractObject(WorkingMemory workingMemory, FactHandle handle):
        execution(void WorkingMemory+.retractObject(FactHandle))
        && target(workingMemory)
        && args(handle)
        && if(workingMemory.getClass() != WorkingMemory.class);

    /** condition tested crosspoint */
    pointcut conditionCheck(Condition condition, Tuple tuple):
        call(boolean Condition+.isAllowed(Tuple))
        && target(condition)
        && args(tuple)
        && if(condition.getClass() != Condition.class);

    /** activation invoke crosspoint */
    pointcut activation(Consequence consequence, Tuple tuple, WorkingMemory workingMemory):
        call(void Consequence+.invoke(Tuple, WorkingMemory))
        && target(consequence)
        && args(tuple, workingMemory)
        && if(consequence.getClass() != Consequence.class);


    /** assertObject advice */
    after(WorkingMemory workingMemory, Object object) returning(FactHandle handle):
        assertObject(workingMemory, object) {
            ObjectAssertedEvent objectAssertedEvent =  new ObjectAssertedEvent(workingMemory, handle, object);
            Iterator iter = workingMemory.getListeners().iterator();
            while ( iter.hasNext() )
            {
                WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
                listener.objectAsserted(objectAssertedEvent);
            }
        }

    /** modifyObject advice */
    after(WorkingMemory workingMemory, FactHandle handle, Object object) returning:
        modifyObject(workingMemory, handle, object)
        {
            ObjectModifiedEvent objectModifiedEvent =  new ObjectModifiedEvent(workingMemory, handle, object);
            Iterator iter = workingMemory.getListeners().iterator();
            while ( iter.hasNext() )
            {
                WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
                listener.objectModified(objectModifiedEvent);
            }
        }


    /** retractObject advice */
    after(WorkingMemory workingMemory, FactHandle handle) returning:
        retractObject(workingMemory, handle)
        {
            ObjectRetractedEvent objectRetractedEvent =  new ObjectRetractedEvent(workingMemory, handle);
            Iterator iter = workingMemory.getListeners().iterator();
            while ( iter.hasNext() )
            {
                WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
                listener.objectRetracted(objectRetractedEvent);
            }
        }


  /** conditionCheck advice */
  after(Condition condition, Tuple tuple) returning(boolean isAllowed):
      conditionCheck(condition, tuple)
      {
          WorkingMemory workingMemory = (WorkingMemory) tuple.getWorkingMemory();
          ConditionTestedEvent conditionTestedEvent =  new ConditionTestedEvent(workingMemory, tuple.getRule(), condition, tuple, isAllowed);
          Iterator iter = workingMemory.getListeners().iterator();
          while ( iter.hasNext() )
          {
              WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
              listener.conditionTested(conditionTestedEvent);
          }
      }


    /** activationCreated advice */
    before(Consequence consequence, Tuple tuple, WorkingMemory workingMemory) returning:
        activation(consequence, tuple, workingMemory)
        {
            ActivationCreatedEvent activationCreatedEvent =  new ActivationCreatedEvent(workingMemory, consequence, tuple);

            Iterator iter = workingMemory.getListeners().iterator();
            while ( iter.hasNext() )
            {
                WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
                listener.activationCreated(activationCreatedEvent);
            }
        }

    /** activationFired advice */
    after(Consequence consequence, Tuple tuple, WorkingMemory workingMemory) returning:
        activation(consequence, tuple, workingMemory)
        {
            ActivationFiredEvent activationFiredEvent =  new ActivationFiredEvent(workingMemory, consequence, tuple);

            Iterator iter = workingMemory.getListeners().iterator();
            while ( iter.hasNext() )
            {
                WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
                listener.activationFired(activationFiredEvent);
            }
        }
}
