package org.drools.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    
    /** check that the rule base is serializable, via the rulebase proxy */
    public void testSerializeRuleBase() throws Exception {

        RuleBase rb = RuleBaseLoader.loadFromUrl(IntegrationTest.class.getResource( "fibonacci.mixed.drl" ));
        assertTrue(rb instanceof SerializableRuleBaseProxy);
        
        RuleBase loadedRb = serializeAndBack( rb );
        assertTrue(loadedRb instanceof SerializableRuleBaseProxy);
        
        RuleBase loadedRb2 = serializeAndBack( loadedRb );
        assertTrue(loadedRb2 instanceof SerializableRuleBaseProxy);            
        
    }

    private RuleBase serializeAndBack(RuleBase rb) throws IOException,
                                                  ClassNotFoundException
    {
        ByteArrayOutputStream outByte = new ByteArrayOutputStream();
        
        
        ObjectOutputStream objStream = new ObjectOutputStream(outByte);
        objStream.writeObject(rb);
        objStream.flush();
        objStream.close();
        
        ByteArrayInputStream inByte = new ByteArrayInputStream(outByte.toByteArray());
         
        ObjectInputStream objIn = new ObjectInputStream(inByte);
        RuleBase loadedRb = (RuleBase) objIn.readObject();
        return loadedRb;
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
