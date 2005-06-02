package org.drools.spring.examples.helloworld;

import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.event.DebugWorkingMemoryEventListener;

public class HelloWorldRunner {
    
    public static void run(RuleBase ruleBase) throws FactException {
        System.out.println("FIRE RULES(Hello)");
        System.out.println("----------");
        WorkingMemory workingMemory = ruleBase.newWorkingMemory();
        workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
        workingMemory.assertObject("Hello");
        workingMemory.fireAllRules();
        
        System.out.println( "\n" );

        System.out.println("FIRE RULES(GoodBye)");
        System.out.println("----------");
        workingMemory = ruleBase.newWorkingMemory( );
        workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
        workingMemory.assertObject("Goodbye");
        workingMemory.fireAllRules();
    }
}
