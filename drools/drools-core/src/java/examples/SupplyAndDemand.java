
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.DroolsException;

import org.drools.semantic.java.RuleLoader;

public class SupplyAndDemand
{
    public static void main(String[] args)
    {
        try
        {
            RuleBase ruleBase = new RuleBase();
            
            RuleLoader.load( ruleBase,
                             new File( "supply_and_demand.xml" ) );

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

    }
}
