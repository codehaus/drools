
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.DroolsException;
import org.drools.AssertionException;

import org.drools.semantic.java.RuleLoader;

import java.io.File;
import java.net.MalformedURLException;

public class SupplyAndDemand
{
    public static void main(String[] args)
    {
        if ( args.length != 1 )
        {
            System.err.println( "usage: java SupplyAndDemand <rule-set.xml>" );

            System.exit( 1 );
        }

        try
        {
            RuleBase ruleBase = new RuleBase();
            
            RuleLoader.load( ruleBase,
                             new File( args[0] ) );

            WorkingMemory memory = ruleBase.createWorkingMemory();

            runDemo( memory );
        }
        catch (DroolsException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    private static void runDemo(WorkingMemory memory)
    {
        Buyer buyer1 = new Buyer( "buyer-1",
                                  100,
                                  9.99 );

        Buyer buyer2 = new Buyer( "buyer-2",
                                  20,
                                  15.00 );

        Seller seller1 = new Seller( "seller-1",
                                     50,
                                     9.99 );

        Seller seller2 = new Seller ( "seller-2",
                                      75,
                                      12.85 );

        Seller seller3 = new Seller( "seller-3",
                                     20,
                                     5.00 );

        System.err.println( "----------------------------------------" );
        System.err.println( "    PRE" );
        System.err.println( "----------------------------------------" );
        System.err.println( buyer1 );
        System.err.println( buyer2 );
        System.err.println( seller1 );
        System.err.println( seller2 );
        System.err.println( seller3 );

        System.err.println( "----------------------------------------" );
        try
        {
            memory.assertObject( buyer1 );
            memory.assertObject( buyer2 );
            memory.assertObject( seller1 );
            memory.assertObject( seller2 );
            memory.assertObject( seller3 );
        }
        catch (AssertionException e)
        {
            e.printStackTrace();
        }

        System.err.println( "----------------------------------------" );
        System.err.println( "    POST" );
        System.err.println( "----------------------------------------" );
        System.err.println( buyer1 );
        System.err.println( buyer2 );
        System.err.println( seller1 );
        System.err.println( seller2 );
        System.err.println( seller3 );
    }
}
