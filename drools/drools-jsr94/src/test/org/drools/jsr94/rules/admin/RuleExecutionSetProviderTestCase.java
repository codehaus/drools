package org.drools.jsr94.rules.admin;

import org.drools.jsr94.rules.RuleEngineTestBase;
import org.drools.RuleBase;
import org.drools.io.RuleSetReader;
import org.drools.RuleBaseBuilder;
import org.drools.rule.RuleSet;

import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSetCreateException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test the RuleExecutionSetProvider implementation.
 *
 * @author N. Alex Rupp (n_alex <at> codehaus.org)
 */
public class RuleExecutionSetProviderTestCase extends RuleEngineTestBase
{
    private RuleAdministrator ruleAdministrator;
    private RuleExecutionSetProvider ruleSetProvider;
    private RuleBase ruleBase = null;

    /**
     * Setup the test case.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
        ruleSetProvider = ruleAdministrator.getRuleExecutionSetProvider( null );

        initRuleBase();
    }

    private void initRuleBase()
    {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = RuleEngineTestBase.class.getResourceAsStream(bindUri);
            Reader reader = new InputStreamReader( resourceAsStream );
            RuleSetReader ruleSetReader = new RuleSetReader();
            RuleBaseBuilder builder = new RuleBaseBuilder();
            builder.addRuleSet( ruleSetReader.read( reader ) );
            ruleBase = builder.build();
        }
        catch (IOException e)
        {
            throw new ExceptionInInitializerError("setUp() could not init the " +
                   "RuleBase due to an IOException in the InputStream: " + e);
        }
        catch (Exception e)
        {
           throw new ExceptionInInitializerError("setUp() could not init the " +
                   "RuleBase, " + e);
        }
        finally
        {
            if (resourceAsStream != null)
            {
                try
                {
                    resourceAsStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void tearDown()
    {
        ruleBase = null;
    }

    /**
     * Test createRuleExecutionSet from DOM.
     */
    public void testCreateFromElement() throws Exception
    {
        // TODO: implement testCreateFromElement()
        throw new UnsupportedOperationException("" +
                "The createRuleExecutionSet(Element, Map) method has not yet " +
                "been implemented.");
    }

    /**
     * Test createRuleExecutionSet from Serializable.
     */
    public void testCreateFromSerializable() throws Exception
    {
        RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet(ruleBase, null);
        assertEquals("rule set name", "Sisters Rules", ruleSet.getName());
        assertEquals("number of rules", 2, ruleSet.getRules().size());
    }

    /**
     * Test createRuleExecutionSet from URI.
     */
    public void testCreateFromURI() throws Exception
    {
        String rulesUri = RuleEngineTestBase.class.getResource(bindUri).toExternalForm();
        RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet(rulesUri, null);
        assertEquals("rule set name", "Sisters Rules", ruleSet.getName());
        assertEquals("number of rules", 2, ruleSet.getRules().size());
    }

    public void testIncompatibleSerializableCreation() throws Exception
    {
        try
        {
            RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet(
                    new ArrayList(), null);
            fail("Should have thrown an IllegalArgumentException. ArrayList " +
                    "objects are not valid AST representations.");
        }
        catch (IllegalArgumentException e)
        {
            /* this is supposed to happen if you pass in a serializable object
             * that isn't a supported AST representation.
             */
        }
    }

    public static void main (String[] args) {
        String[] name = { RuleExecutionSetProviderTestCase.class.getName() };
        junit.textui.TestRunner.main(name);
    }

    public static Test suite() {
        return new TestSuite(RuleExecutionSetProviderTestCase.class);
    }
}
