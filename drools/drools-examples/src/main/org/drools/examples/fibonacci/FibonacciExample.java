package org.drools.examples.fibonacci;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleSetReader;
import org.drools.io.RuleBaseBuilder;
import org.drools.rule.RuleSet;

import org.drools.event.DefaultWorkingMemoryEventListener;
import org.drools.event.WorkingMemoryEventListener;
import org.drools.event.ObjectAssertedEvent;
import org.drools.event.ObjectModifiedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.ConditionTestedEvent;
import org.drools.event.ActivationCreatedEvent;
import org.drools.event.ActivationFiredEvent;

public class FibonacciExample
{
    public static void main(String[] args)
        throws Exception
    {
        RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( FibonacciExample.class.getResource( "fibonacci.drl" ) );

        WorkingMemory workingMemory = ruleBase.newWorkingMemory();

        WorkingMemoryEventListener listener = new DefaultWorkingMemoryEventListener()
        {
            public void objectAsserted(ObjectAssertedEvent event)
            {
                System.out.println("asserted");
            }

            public void objectModified(ObjectModifiedEvent event)
            {
                System.out.println("modified");
            }

            public void objectRetracted(ObjectRetractedEvent event)
            {
                System.out.println("retracted");
            }

            public void conditionTested(ConditionTestedEvent event)
            {
                System.out.println("tested : " + event.getPassed());
            }

            public void activationCreated(ActivationCreatedEvent event)
            {
                System.out.println("activation created");
            }

            public void activationFired(ActivationFiredEvent event)
            {
                System.out.println("activation fired");
            }
        };

        workingMemory.addEventListener(listener);

        Fibonacci fibonacci = new Fibonacci( 50 );

        long start = System.currentTimeMillis();

        workingMemory.assertObject( fibonacci );


        workingMemory.fireAllRules();

        long stop = System.currentTimeMillis();

        System.err.println( "fibanacci(" + fibonacci.getSequence() + ") == " + fibonacci.getValue() + " took " + (stop-start) + "ms" );
    }
}
