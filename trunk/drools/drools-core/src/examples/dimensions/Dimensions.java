package dimensions;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.drools.FactHandle;
import org.drools.AssertionException;
import org.drools.DroolsException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleSetReader;
import org.drools.io.SemanticsReader;
import org.drools.rule.RuleSet;
import org.drools.smf.SimpleSemanticsRepository;
import org.drools.smf.SemanticModule;

public class Dimensions
{
	public static void main(String[] args)
	{
		try
		{
            SemanticsReader semanticsReader = new SemanticsReader();
            
            SemanticModule module = semanticsReader.read( Dimensions.class.getResource( "/org/drools/semantics/java/semantics.properties" ) );
            
            SimpleSemanticsRepository repo = new SimpleSemanticsRepository();
            
            repo.registerSemanticModule( module );
            
            // First, construct an empty RuleBase to be the
            // container for your rule logic.
            
            RuleBase ruleBase = new RuleBase();
            
            // Then, use the [org.drools.semantic.java.RuleLoader]
            // static method to load a rule-set from a local File.
            
            //RuleSetLoader loader = new RuleSetLoader();
            
            URL url = Dimensions.class.getResource("dimensions.drl");
            
            RuleSetReader reader = new RuleSetReader( repo );
            
            System.err.println("loading: " + url);
            
            RuleSet ruleSet = reader.read( url );
            
            ruleBase.addRuleSet( ruleSet );

			// Create a [org.drools.WorkingMemory] to be the
			// container for your facts

			WorkingMemory mem = ruleBase.newWorkingMemory();
            
			try
			{

				Height height = new Height(150);
				FactHandle heightHandle = mem.assertObject(height);

				Width width = new Width(240);
				FactHandle widthHandle = mem.assertObject(width);
				
				Length length = new Length(-1);
				FactHandle lengthHandle = mem.assertObject(length);

                                mem.fireAllRules();

				width = new Width(100);
				mem.modifyObject(widthHandle, width);

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
