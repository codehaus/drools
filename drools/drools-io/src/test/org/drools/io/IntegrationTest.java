package org.drools.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.drools.FactException;
import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.xml.sax.SAXException;

public class IntegrationTest  extends TestCase
{

    public void testFibonacciJava() throws IntegrationException, SAXException, IOException, FactException 
    {
        Map map;
        RuleSetLoader ruleSetLoader = new RuleSetLoader();

        // single URL parameter
        ruleSetLoader.addFromUrl( IntegrationTest.class.getResource( "fibonacci.java.drl" ) );
        
        map = new HashMap();
        
        runTest(ruleSetLoader, map);
        
        assertEquals( new Long( 12586269025L ), map.get("value") );                
    }
    
    public void testFibonacciGroovy() throws IntegrationException, SAXException, IOException, FactException 
    {
        Map map;
        RuleSetLoader ruleSetLoader = new RuleSetLoader();

        // single URL parameter
        ruleSetLoader.addFromUrl( IntegrationTest.class.getResource( "fibonacci.groovy.drl" ) );
        
        map = new HashMap();
        
        runTest(ruleSetLoader, map);
        
        assertEquals( new Long( 12586269025L ), map.get("value") );                
    }    
       
    public void testFibonacciJython() throws IntegrationException, SAXException, IOException, FactException 
    {
        Map map;
        RuleSetLoader ruleSetLoader = new RuleSetLoader();

        // single URL parameter
        ruleSetLoader.addFromUrl( IntegrationTest.class.getResource( "fibonacci.python.drl" ) );
        
        map = new HashMap();
        
        runTest(ruleSetLoader, map);
        
        assertEquals( new Long( 12586269025L ), map.get("value") );                
    }       
         
    public void testFibonacciMixed() throws IntegrationException, SAXException, IOException, FactException 
    {
        Map map;
        RuleSetLoader ruleSetLoader = new RuleSetLoader();

        // single URL parameter
        ruleSetLoader.addFromUrl( IntegrationTest.class.getResource( "fibonacci.mixed.drl" ) );
        
        map = new HashMap();
        
        runTest(ruleSetLoader, map);
        
        assertEquals( new Long( 12586269025L ), map.get("value") );                
    }       
    
    private void runTest(RuleSetLoader ruleSetLoader, Map map) throws IntegrationException, SAXException, IOException, FactException
    {
        WorkingMemory workingMemory;

        RuleBase ruleBase;
        RuleBaseLoader ruleBaseLoader;
        
        //Try with addFromRuleSetLoader
        ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSetLoader( ruleSetLoader );
        ruleBase = ruleBaseLoader.buildRuleBase();
        workingMemory = ruleBase.newWorkingMemory();
        workingMemory.setApplicationData( "map",
                                          map );

        workingMemory.assertObject(  new Fibonacci( 50 ) );
        
        workingMemory.fireAllRules();
    }
}
