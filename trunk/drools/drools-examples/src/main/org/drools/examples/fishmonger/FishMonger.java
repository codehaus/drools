package org.drools.examples.fishmonger;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.DroolsException;
import org.drools.AssertionException;
import org.drools.rule.RuleSet;
import org.drools.io.RuleBaseBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Iterator;

public class FishMonger
{
    public static void main(String[] args)
    {
        try
        {
            URL url = FishMonger.class.getResource( "fishmonger.drl" );

            RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( url );

            WorkingMemory workingMemory = ruleBase.newWorkingMemory();

            try
            {
                ShoppingCart cart = new ShoppingCart();

                cart.addItem( new CartItem( "tropical fish",
                                            12.99 ) );
                
                cart.addItem( new CartItem( "tropical fish",
                                            12.99 ) );
                
                cart.addItem( new CartItem( "tropical fish",
                                            12.99 ) );
                
                cart.addItem( new CartItem( "tropical fish",
                                            12.99 ) );

                cart.addItem( new CartItem( "tropical fish",
                                            12.99 ) );

                cart.addItem( new CartItem( "tropical fish",
                                            12.99 ) );

                cart.addItem( new CartItem( "tropical fish",
                                            12.99 ) );

                if ( false )
                {
                    cart.addItem( new CartItem( "tropical fish food",
                                                8.00 ) );
                }

                if ( false )
                {
                    cart.addItem( new CartItem( "tank",
                                                80.00 ) );
                }
                              

                System.err.println( "----------------------------------------");
                System.err.println( "    PRE" );
                System.err.println( "----------------------------------------");

                System.err.println( cart );
                
                System.err.println( "----------------------------------------");

                // Now, simply assert them into the [org.drools.WorkingMemory]
                // and let the logic engine do the rest.

                workingMemory.assertObject( cart );

                System.err.println( "----------------------------------------");
                System.err.println( "    POST" );
                System.err.println( "----------------------------------------");

                System.err.println( cart );

                System.err.println( "----------------------------------------");
            }
            catch (AssertionException e)
            {
                e.printStackTrace();
            }
        }
        catch (DroolsException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
