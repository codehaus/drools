package org.drools.examples.java.state;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.DroolsException;
import org.drools.AssertionException;
import org.drools.io.RuleBaseBuilder;

import java.util.HashMap;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Iterator;

public class StateExample
{

    public static void main(String[] args) throws Exception
    {
		    URL url = StateExample.class.getResource( "state.drl" );
		    RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( url );
        // Create working memory
        WorkingMemory workingMemory = ruleBase.newWorkingMemory();

        // Add application data map
        //HashMap map = new HashMap();
        //workingMemory.setApplicationData(map);

        State a = new State("A");
        State b = new State("B");
        State c = new State("C");
        State d = new State("D");
        State e = new State("E");

        FactHandle factA = workingMemory.assertObject(a);
        FactHandle factB = workingMemory.assertObject(b);
        FactHandle factC = workingMemory.assertObject(c);
        FactHandle factD = workingMemory.assertObject(d);
        FactHandle factE = workingMemory.assertObject(e);

        a.setState("FINISHED");
        System.err.println("A finished" );
        workingMemory.modifyObject(factA, a);
        workingMemory.fireAllRules();
    }
}
