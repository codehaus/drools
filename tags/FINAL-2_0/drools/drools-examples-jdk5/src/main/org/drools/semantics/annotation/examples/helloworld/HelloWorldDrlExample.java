package org.drools.semantics.annotation.examples.helloworld;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;

public class HelloWorldDrlExample {

    public static void main(String[] args) throws Exception {
        RuleBase ruleBase = RuleBaseLoader.loadFromUrl(
                HelloWorldDrlExample.class.getResource("helloworld.annotation.drl"));
        WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
        
        HelloWorldRunner.run(ruleBase);
    }
}
