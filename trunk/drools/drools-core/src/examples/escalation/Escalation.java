package escalation;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.DroolsException;
import org.drools.AssertionException;
import org.drools.io.RuleSetReader;
import org.drools.io.SemanticsReader;
import org.drools.rule.RuleSet;
import org.drools.smf.SimpleSemanticsRepository;
import org.drools.smf.SemanticModule;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Iterator;

public class Escalation
{
    public static void main(String[] args)
    {
        try
        {
            SemanticsReader semanticsReader = new SemanticsReader();
            
            SemanticModule module = semanticsReader.read( Escalation.class.getResource( "/org/drools/semantics/java/semantics.properties" ) );
            
            SimpleSemanticsRepository repo = new SimpleSemanticsRepository();
            
            repo.registerSemanticModule( module );
            
            // First, construct an empty RuleBase to be the
            // container for your rule logic.
            
            RuleBase ruleBase = new RuleBase();
            
            // Then, use the [org.drools.semantic.java.RuleLoader]
            // static method to load a rule-set from a local File.
            
            //RuleSetLoader loader = new RuleSetLoader();
            
            URL url = Escalation.class.getResource("escalation.drl");
            
            RuleSetReader reader = new RuleSetReader( repo );
            
            System.err.println("loading: " + url);
            
            RuleSet ruleSet = reader.read( url );
            
            ruleBase.addRuleSet( ruleSet );

            // Create a [org.drools.WorkingMemory] to be the
            // container for your facts
            
            WorkingMemory workingMemory = ruleBase.newWorkingMemory();

            try
            {
                TroubleTicket bobTicket  = new TroubleTicket( "bob" );
                TroubleTicket daveTicket = new TroubleTicket( "dave" ); 

                System.err.println( "----------------------------------------");
                System.err.println( "    PRE" );
                System.err.println( "----------------------------------------");

                System.err.println( bobTicket );
                System.err.println( daveTicket );
                
                System.err.println( "----------------------------------------");

                // Now, simply assert them into the [org.drools.WorkingMemory]
                // and let the logic engine do the rest.

                workingMemory.assertObject( daveTicket );
                workingMemory.assertObject( bobTicket );
                workingMemory.fireAllRules();

                System.err.println( "----------------------------------------");
                System.err.println( "    POST ASSERT" );
                System.err.println( "----------------------------------------");

                System.err.println( bobTicket );
                System.err.println( daveTicket );

                System.err.println( "----------------------------------------");

                try
                {
                    System.err.println( "[[ Sleeping 10 seconds ]]" );
                    Thread.sleep( 10000 );
                    System.err.println( "[[ Done sleeping ]]" ) ;
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                System.err.println( "----------------------------------------");
                System.err.println( "    POST SLEEP" );
                System.err.println( "----------------------------------------");

                System.err.println( bobTicket );
                System.err.println( daveTicket );

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
