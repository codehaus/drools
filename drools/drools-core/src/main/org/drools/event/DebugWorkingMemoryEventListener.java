package org.drools.event;

public class DebugWorkingMemoryEventListener
    implements WorkingMemoryEventListener
{
    public DebugWorkingMemoryEventListener()
    {
        // intentionally left blank
    }

    public void objectAsserted(ObjectAssertedEvent event)
    {
        System.err.println( event );
    }
    public void objectModified(ObjectModifiedEvent event)
    {
        System.err.println( event );
    }

    public void objectRetracted(ObjectRetractedEvent event)
    {
        System.err.println( event );
    }

    public void conditionTested(ConditionTestedEvent event)
    {
        System.err.println( event );
    }

    public void activationCreated(ActivationCreatedEvent event)
    {
        System.err.println( event );
    }

    public void activationFired(ActivationFiredEvent event)
    {
        System.err.println( event );
    }
}
