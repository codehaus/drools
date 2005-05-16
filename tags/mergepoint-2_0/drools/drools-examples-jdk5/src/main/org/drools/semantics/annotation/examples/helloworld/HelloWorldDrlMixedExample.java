package org.drools.semantics.annotation.examples.helloworld;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;

public class HelloWorldDrlMixedExample {

    public static void main(String[] args) throws Exception {
        RuleBase ruleBase = RuleBaseLoader.loadFromUrl(
                HelloWorldDrlMixedExample.class.getResource("helloworld.mixed.drl"));
        WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
        
        HelloWorldRunner.run(ruleBase);
    }
}
