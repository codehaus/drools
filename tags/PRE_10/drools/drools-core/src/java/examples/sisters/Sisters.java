package sisters;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.DroolsException;
import org.drools.AssertionException;
import org.drools.rule.RuleSet;

import org.drools.io.RuleSetLoader;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Iterator;

public class Sisters
{
    public static void main(String[] args)
    {
        try
        {
            // First, construct an empty RuleBase to be the
            // container for your rule logic.

            RuleBase ruleBase = new RuleBase();
            
            // Then, use the [org.drools.semantic.java.RuleLoader]
            // static method to load a rule-set from a local File.
            

            RuleSetLoader loader = new RuleSetLoader();

            URL url = Sisters.class.getResource( "sisters.drl" );

            System.err.println( "loading: " + url );

            List ruleSets = loader.load( url );

            Iterator ruleSetIter = ruleSets.iterator();
            RuleSet  eachRuleSet = null;

            while ( ruleSetIter.hasNext() )
            {
                eachRuleSet = (RuleSet) ruleSetIter.next();

                ruleBase.addRuleSet( eachRuleSet );
            }

            // Create a [org.drools.WorkingMemory] to be the
            // container for your facts
            
            WorkingMemory workingMemory = ruleBase.createWorkingMemory();

            try
            {
                Person person = null;

                person = new Person( "bob" );

                workingMemory.assertObject( person );

                person = new Person( "rebecca" );

                person.addSister( "jeannie" );

                workingMemory.assertObject( person );

                person = new Person( "jeannie" );

                person.addSister( "rebecca" );

                workingMemory.assertObject( person );
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
