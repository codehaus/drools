/*
* Created by IntelliJ IDEA.
* User: RefuX Zanzeebarr
* Date: Aug 25, 2002
* Time: 5:42:53 PM
*
* Current issue: if a rule has 2 params, and the 2nd param is modified the condition isn't reeval'd
*
*/
package org.drools.misc;

import org.drools.io.RuleSetLoader;

import junit.framework.TestCase;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.rule.RuleSet;

import java.net.URL;
import java.util.*;

public class DROOLS_25 extends TestCase {
    private WorkingMemory workingMemory;

    public DROOLS_25(String name) {
        super( name );
    }

    public void setUp() {
        try {
            // First, construct an empty RuleBase to be the
            // container for your rule logic.
            RuleBase ruleBase = new RuleBase();

            // Then, use the [org.drools.semantic.java.RuleLoader]
            // static method to load a rule-set from a local File.
            RuleSetLoader loader = new RuleSetLoader();
            URL url = getClass().getResource( "BugTest.drl" );
            assertNotNull( url );
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
            workingMemory = ruleBase.createWorkingMemory();
        }
        catch( Exception e ) {
            fail( "Failed to setup test [" + e.getMessage() + "]" );
        }
    }

    public void testSuccessWithRetractAndAssert() {
        try {
            //create vars to place in working memory
            String string = "blah";
            Properties props = new Properties();

            // Now, simply assert them into the [org.drools.WorkingMemory]
            // and let the logic engine do the rest.

            workingMemory.assertObject( string );
            workingMemory.assertObject( props );

            //change the props the notify the system

            props.setProperty( "test", "test" );

            //retract and assert method

            workingMemory.retractObject( props );
            workingMemory.assertObject( props );

            //the test property should be set to success

            String testResult = props.getProperty( "test" );

            if( !"success".equals( testResult) ) {
                fail( "the property[test] in rule[test:1] wasn't set to 'success'" );
            }
        }
        catch( Exception e ) {
            fail( e.getMessage() );
        }
    }

    public void testSuccessWithModify() {
        try {
            //create vars to place in working memory
            String string = "blah";
            Properties props = new Properties();

            // Now, simply assert them into the [org.drools.WorkingMemory]
            // and let the logic engine do the rest.

            System.err.println( "\n\n\n####### 1 #######\n\n\n" );
            workingMemory.assertObject( string );
            System.err.println( "\n\n\n####### 2 #######\n\n\n" );
            workingMemory.assertObject( props );

            //change the props the notify the system

            props.setProperty( "test", "test" );

            //modify method
            System.err.println( "\n\n\n####### 3 #######\n\n\n" );
            workingMemory.modifyObject( props );
            System.err.println( "\n\n\n####### 4 #######\n\n\n" );

            //the test property should be set to success
            String testResult = props.getProperty( "test" );
            if( !"success".equals( testResult) ) {
                fail( "the property[test] in rule[test:1] wasn't set to 'success'" );
            }
        }
        catch( Exception e ) {
            fail( e.getMessage() );
        }
    }
}
