/*
 * Created on 21/05/2005
 */
package org.drools.decisiontable;

import java.io.StringReader;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.decisiontable.model.Ruleset;
import org.drools.io.RuleBaseLoader;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * Tests actually executing the ruleset that is being created from the model
 * class (via DRL).
 */
public class RulesetExecTest extends TestCase
{

    /**
     * Shows basic use of the Ruleset model class, with a real instance of
     * drools.
     * 
     * @throws Exception
     */
    public void testRulesetExec() throws Exception
    {
        Ruleset ruleSet = RulesetToDRLTest.getTestRuleSet( );
        RuleBase rb = RuleBaseLoader.loadFromReader( new StringReader( ruleSet.toXML( ) ) );

        WorkingMemory engine = rb.newWorkingMemory( );
        engine.assertObject( "yes" );
        engine.fireAllRules( );
    }

}
