package org.drools.examples.state;

import java.net.URL;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseBuilder;

public class StateExample
{

    public static void main(String[] args)
    {
        if ( args.length != 1 )
        {
            System.out.println( "Usage: " + StateExample.class.getName( )
                                + " [drl file]" );
            return;
        }
        System.out.println( "Using drl: " + args[0] );
        try
        {
            URL url = StateExample.class.getResource( args[0] );
            RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( url );

            WorkingMemory workingMemory = ruleBase.newWorkingMemory( );

            State a = new State( "A" );
            State b = new State( "B" );
            State c = new State( "C" );
            State d = new State( "D" );

            FactHandle factA = workingMemory.assertObject( a );
            FactHandle factB = workingMemory.assertObject( b );
            FactHandle factC = workingMemory.assertObject( c );
            FactHandle factD = workingMemory.assertObject( d );

            workingMemory.modifyObject( factA, a );
            workingMemory.fireAllRules( );
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
    }
}