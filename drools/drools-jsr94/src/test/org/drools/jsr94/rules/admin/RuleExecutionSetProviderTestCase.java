package org.drools.jsr94.rules.admin;

import org.drools.jsr94.rules.RuleEngineTestBase;

import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetProvider;

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
    public void testCreateFromDOM() throws Exception
    {
        // TODO: implement testCreateFromDOM()
        // not implemented
    }


    /**
     * Test createRuleExecutionSet from Serializable.
     */
    public void testCreateFromSerializable() throws Exception
    {
        // TODO: implement testCreateFromSerializable()
        // not implemented
    }

    /**
     * Test createRuleExecutionSet from URI.
     */
    public void testCreateFromURI() throws Exception
    {
        String rulesUri = org.drools.jsr94.rules.RuleEngineTestBase.class.getResource(bindUri).toExternalForm();
        RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet(rulesUri, null);
        assertEquals("rule set name", "Sisters Rules", ruleSet.getName());
        assertEquals("number of rules", 2, ruleSet.getRules().size());
    }
}
