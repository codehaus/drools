package sisters;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.DroolsException;
import org.drools.AssertionException;
import org.drools.rule.RuleSet;

import org.drools.io.RuleSetLoader;

import java.util.HashMap;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Iterator;

public class Sisters
{

    public static void main(String[] args) throws Exception
    {

        // read number of runs from command line
        int maxrun = 1;
        if (args.length > 0)
        {
            maxrun = new Integer(args[0]).intValue();
        }

        for (int i = 0; i < maxrun; i++)
        {
            System.err.println("run: " + i);
            Sisters sis = new Sisters();
            sis.runSisters();
        }
    }

    private void runSisters() throws Exception
    {
        // First, construct an empty RuleBase to be the
        // container for your rule logic.

        RuleBase ruleBase = new RuleBase();

        // Then, use the [org.drools.semantic.java.RuleLoader]
        // static method to load a rule-set from a local File.

        RuleSetLoader loader = new RuleSetLoader();

        URL url = Sisters.class.getResource("sisters.drl");

        System.err.println("loading: " + url);

        List ruleSets = loader.load(url);

        Iterator ruleSetIter = ruleSets.iterator();
        RuleSet eachRuleSet = null;

        while (ruleSetIter.hasNext())
        {
            eachRuleSet = (RuleSet) ruleSetIter.next();

            ruleBase.addRuleSet(eachRuleSet);
        }

        // Create some application specific data that will
        // be needed by our consequence.

        HashMap map = new HashMap();
        map.put("message", "This came from the appData HashMap");

        // Create a [org.drools.WorkingMemory] to be the
        // container for your facts

        WorkingMemory workingMemory = ruleBase.newWorkingMemory();
        workingMemory.setApplicationData(map);


        Person person = null;

        person = new Person("bob");

        workingMemory.assertObject(person);

        person = new Person("rebecca");

        person.addSister("jeannie");

        workingMemory.assertObject(person);

        person = new Person("jeannie");

        person.addSister("rebecca");

        workingMemory.assertObject(person);

        workingMemory.fireAllRules();
    }
}
