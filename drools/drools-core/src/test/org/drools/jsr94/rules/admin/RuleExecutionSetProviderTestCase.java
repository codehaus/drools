package org.drools.jsr94.rules.admin;

import org.drools.jsr94.rules.JSR94TestBase;

import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetProvider;

/**
 * Test the RuleRuntime implementation.
 */
public class RuleExecutionSetProviderTestCase extends JSR94TestBase
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
        // not implemented
    }


    /**
     * Test createRuleExecutionSet from Serializable.
     */
    public void testCreateFromSerializable() throws Exception
    {
        // not implemented
    }

    /**
     * Test createRuleExecutionSet from URI.
     */
    public void testCreateFromURI() throws Exception
    {
        String rulesUri = getResource(RULES_RESOURCE).toExternalForm();
        RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet(rulesUri, null);
        assertEquals("rule set name", "Sisters Rules", ruleSet.getName());
        assertEquals("number of rules", 2, ruleSet.getRules().size());
    }
}
