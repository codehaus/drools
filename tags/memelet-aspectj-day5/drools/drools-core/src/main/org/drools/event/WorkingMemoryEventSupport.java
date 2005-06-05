package org.drools.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.Tuple;

public class WorkingMemoryEventSupport implements Serializable {

    private final List listeners = new ArrayList();
    private final WorkingMemory workingMemory;

    
    
    
    public WorkingMemoryEventSupport(WorkingMemory workingMemory) {
        this.workingMemory = workingMemory;
    }

    public void addEventListener(WorkingMemoryEventListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeEventListener(WorkingMemoryEventListener listener) {
        this.listeners.remove(listener);
    }

    public List getEventListeners() {
        return Collections.unmodifiableList(this.listeners);
    }

    public int size() {
        return this.listeners.size();
    }

    public boolean isEmpty() {
        return this.listeners.isEmpty();
    }

    public void fireObjectAsserted(FactHandle handle, Object object) {
        if (this.listeners.isEmpty()) {
            return;
        }

        ObjectAssertedEvent event = new ObjectAssertedEvent(this.workingMemory, handle, object);

        for (int i = 0, size = this.listeners.size(); i < size; i++) {
            ((WorkingMemoryEventListener) this.listeners.get(i)).objectAsserted(event);
        }
    }

    public void fireObjectModified(FactHandle handle, Object oldObject, Object object) {
        if (this.listeners.isEmpty()) {
            return;
        }

        ObjectModifiedEvent event = new ObjectModifiedEvent(this.workingMemory, handle, oldObject,
                object);

        for (int i = 0, size = this.listeners.size(); i < size; i++) {
            ((WorkingMemoryEventListener) this.listeners.get(i)).objectModified(event);
        }
    }

    public void fireObjectRetracted(FactHandle handle, Object oldObject) {
        if (this.listeners.isEmpty()) {
            return;
        }

        ObjectRetractedEvent event = new ObjectRetractedEvent(this.workingMemory, handle, oldObject);

        for (int i = 0, size = this.listeners.size(); i < size; i++) {
            ((WorkingMemoryEventListener) this.listeners.get(i)).objectRetracted(event);
        }
    }

    public void fireConditionTested(Rule rule, Condition condition, Tuple tuple, boolean result) {
        if (this.listeners.isEmpty()) {
            return;
        }

        ConditionTestedEvent event = new ConditionTestedEvent(this.workingMemory, rule, condition,
                tuple, result);

        for (int i = 0, size = this.listeners.size(); i < size; i++) {
            ((WorkingMemoryEventListener) this.listeners.get(i)).conditionTested(event);
        }
    }

    public void fireActivationCreated(Rule rule, Tuple tuple) {
        if (this.listeners.isEmpty()) {
            return;
        }

        ActivationCreatedEvent event = new ActivationCreatedEvent(this.workingMemory, rule, tuple);

        for (int i = 0, size = this.listeners.size(); i < size; i++) {
            ((WorkingMemoryEventListener) this.listeners.get(i)).activationCreated(event);
        }
    }

    public void fireActivationCancelled(Rule rule, Tuple tuple) {
        if (this.listeners.isEmpty()) {
            return;
        }

        ActivationCancelledEvent event = new ActivationCancelledEvent(this.workingMemory, rule,
                tuple);

        for (int i = 0, size = this.listeners.size(); i < size; i++) {
            ((WorkingMemoryEventListener) this.listeners.get(i)).activationCancelled(event);
        }
    }

    public void fireActivationFired(Rule rule, Tuple tuple) {
        if (this.listeners.isEmpty()) {
            return;
        }

        ActivationFiredEvent event = new ActivationFiredEvent(this.workingMemory, rule, tuple);

        for (int i = 0, size = this.listeners.size(); i < size; i++) {
            ((WorkingMemoryEventListener) this.listeners.get(i)).activationFired(event);
        }
    }
}
