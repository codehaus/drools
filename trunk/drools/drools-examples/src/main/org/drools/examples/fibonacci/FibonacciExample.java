package org.drools.examples.fibonacci;

import org.drools.RuleBaseBuilder;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleSetReader;
import org.drools.rule.RuleSet;

public class FibonacciExample
{
    public static void main(String[] args)
        throws Exception
    {
        RuleSetReader reader = new RuleSetReader();
        
        RuleSet ruleSet = reader.read( FibonacciExample.class.getResource( "fibonacci.drl" ) );
        
        RuleBaseBuilder builder = new RuleBaseBuilder();
        
        builder.addRuleSet( ruleSet );
        
        RuleBase ruleBase = builder.build();
        
        WorkingMemory workingMemory = ruleBase.newWorkingMemory();
        
        Fibonacci fibonacci = new Fibonacci( 80 );
        
        long start = System.currentTimeMillis();

        workingMemory.assertObject( fibonacci );

        workingMemory.fireAllRules();
        
        long stop = System.currentTimeMillis();

        System.err.println( "fibanacci(" + fibonacci.getSequence() + ") == " + fibonacci.getValue() + " took " + (stop-start) + "ms" );

    }
}
