package org.drools.misc;

import org.drools.FactHandle;
import org.drools.io.RuleSetReader;
import org.drools.io.SemanticsReader;
import org.drools.smf.SimpleSemanticsRepository;

import junit.framework.TestCase;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.rule.RuleSet;

import java.net.URL;
import java.util.*;

public class DROOLS_25_Test
    extends TestCase
{
    private WorkingMemory workingMemory;

    public DROOLS_25_Test(String name)
    {
        super( name );
    }

    public void setUp()
        throws Exception
    {
        SimpleSemanticsRepository repo = new SimpleSemanticsRepository();

        SemanticsReader semanticsReader = new SemanticsReader();
        
        repo.registerSemanticModule( semanticsReader.read( getClass().getResource( "/org/drools/semantics/java/semantics.properties" ) ) );
        
        RuleSetReader ruleSetReader = new RuleSetReader( repo );
        
        RuleBase ruleBase = new RuleBase();
        
        ruleBase.addRuleSet( ruleSetReader.read( getClass().getResource( "DROOLS_25_Test.drl" ) ) );

        this.workingMemory = ruleBase.newWorkingMemory();
    }

    public void testSuccessWithRetractAndAssert()
        throws Exception
    {
        String string = "blah";
        Properties props = new Properties();

        FactHandle stringHandle = workingMemory.assertObject( string );
        FactHandle propsHandle = workingMemory.assertObject( props );
        
        workingMemory.fireAllRules();

        props.setProperty( "test", "test" );

        workingMemory.retractObject( propsHandle );
        propsHandle = workingMemory.assertObject( props );
        
        workingMemory.fireAllRules();
        
        String testResult = props.getProperty( "test" );
        
        assertEquals( "success",
                      testResult );
    }

    public void testSuccessWithModify()
        throws Exception
    {
        String string = "blah";
        Properties props = new Properties();
        
        FactHandle stringHandle = workingMemory.assertObject( string );
        FactHandle propsHandle = workingMemory.assertObject( props );
        
        workingMemory.fireAllRules();
        
        props.setProperty( "test", "test" );
        
        workingMemory.modifyObject( propsHandle,
                                    props );

        workingMemory.fireAllRules();
        
        String testResult = props.getProperty( "test" );
        
        assertEquals( "success",
                      testResult );
    }
}
