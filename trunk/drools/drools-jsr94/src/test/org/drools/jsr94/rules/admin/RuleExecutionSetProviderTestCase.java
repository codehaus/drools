package org.drools.jsr94.rules.admin;

import org.drools.jsr94.rules.RuleEngineTestBase;
import org.drools.RuleBase;
import org.drools.io.RuleSetReader;
import org.drools.RuleBaseBuilder;

import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetProvider;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;

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

    /**
     * Setup the test case.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
        ruleSetProvider = ruleAdministrator.getRuleExecutionSetProvider( null );
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
        InputStream resourceAsStream = RuleEngineTestBase.class.getResourceAsStream(bindUri);
        Reader reader = new InputStreamReader( resourceAsStream );
        RuleSetReader ruleSetReader = new RuleSetReader();
        RuleBaseBuilder builder = new org.drools.RuleBaseBuilder();
        builder.addRuleSet( ruleSetReader.read( reader ) );
        RuleBase ruleBase = builder.build();

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

    public static void main (String[] args) {
        String[] name = { RuleExecutionSetProviderTestCase.class.getName() };
        junit.textui.TestRunner.main(name);
    }

    public static Test suite() {
        return new TestSuite(RuleExecutionSetProviderTestCase.class);
    }
}
