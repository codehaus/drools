package org.drools.examples.escalation;

import java.io.IOException;
import java.net.URL;

import org.drools.AssertionException;
import org.drools.DroolsException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;

public class Escalation
{
    public static void main(String[] args)
    {
        if ( args.length != 1 )
        {
            System.out.println( "Usage: " + Escalation.class.getName( )
                                + " [drl file]" );
            return;
        }
        System.out.println( "Using drl: " + args[0] );
        try
        {
            URL url = Escalation.class.getResource( args[0] );
            RuleBase ruleBase = RuleBaseLoader.loadFromUrl( url );

            WorkingMemory workingMemory = ruleBase.newWorkingMemory( );

            try
            {
                TroubleTicket bobTicket = new TroubleTicket( "bob" );
                TroubleTicket daveTicket = new TroubleTicket( "dave" );

                System.err.println( "----------------------------------------" );
                System.err.println( "    PRE" );
                System.err.println( "----------------------------------------" );

                System.err.println( bobTicket );
                System.err.println( daveTicket );

                System.err.println( "----------------------------------------" );

                // Now, simply assert them into the [org.drools.WorkingMemory]
                // and let the logic engine do the rest.

                workingMemory.assertObject( daveTicket );
                workingMemory.assertObject( bobTicket );

                System.err.println( "----------------------------------------" );
                System.err.println( "    POST ASSERT" );
                System.err.println( "----------------------------------------" );

                System.err.println( bobTicket );
                System.err.println( daveTicket );

                System.err.println( "----------------------------------------" );

                try
                {
                    System.err.println( "[[ Sleeping 10 seconds ]]" );
                    Thread.sleep( 10000 );
                    System.err.println( "[[ Done sleeping ]]" );
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace( );
                }

                System.err.println( "----------------------------------------" );
                System.err.println( "    POST SLEEP" );
                System.err.println( "----------------------------------------" );

                System.err.println( bobTicket );
                System.err.println( daveTicket );

                System.err.println( "----------------------------------------" );

            }
            catch ( AssertionException e )
            {
                e.printStackTrace( );
            }
        }
        catch ( DroolsException e )
        {
            e.printStackTrace( );
        }
        catch ( IOException e )
        {
            e.printStackTrace( );
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
    }
}