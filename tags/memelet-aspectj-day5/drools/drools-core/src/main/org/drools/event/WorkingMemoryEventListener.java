package org.drools.event;

import java.util.EventListener;

public interface WorkingMemoryEventListener extends EventListener {

    void objectAsserted(ObjectAssertedEvent event);

    void objectModified(ObjectModifiedEvent event);

    void objectRetracted(ObjectRetractedEvent event);

    void conditionTested(ConditionTestedEvent event);

    void activationCreated(ActivationCreatedEvent event);

    void activationCancelled(ActivationCancelledEvent event);

    void activationFired(ActivationFiredEvent event);
}
