package org.drools.rule;

import junit.framework.TestCase;

public class RuleSetTest extends TestCase
{
    private RuleSet ruleSet;

    public RuleSetTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.ruleSet = new RuleSet( "rule_set" );
    }

    public void tearDown()
    {
        this.ruleSet = null;
    }

    public void testBasics()
    {
        assertEquals( "rule_set",
                      this.ruleSet.getName() );
    }

    public void testDocumentation()
    {
        assertNull( this.ruleSet.getDocumentation() );

        this.ruleSet.setDocumentation( "the cheesiest!" );

        assertEquals( "the cheesiest!",
                      this.ruleSet.getDocumentation() );
    }

    /** A RuleSet MUST accept any Rule that does not have
     *  a conflicting name.
     */
    public void testAddRule()
        throws Exception
    {
        InstrumentedRule rule;

        rule = new InstrumentedRule( "cheese" );
        rule.isValid( true );
        this.ruleSet.addRule( rule );
        assertEquals(0, rule.getLoadOrder());

        rule = new InstrumentedRule( "meat" );
        rule.isValid( true );
        this.ruleSet.addRule( rule );
        assertEquals(1, rule.getLoadOrder());

        rule = new InstrumentedRule( "vegetables" );
        rule.isValid( true );
        this.ruleSet.addRule( rule );
        assertEquals(2, rule.getLoadOrder());
    }

    public void testGetRule()
        throws Exception
    {
        assertNull( this.ruleSet.getRule( "cheese" ) );

        InstrumentedRule rule = new InstrumentedRule( "cheese" );

        rule.isValid( true );

        this.ruleSet.addRule( rule );

        assertSame( rule,
                    this.ruleSet.getRule( "cheese" ) );

        assertNull( this.ruleSet.getRule( "betty" ) );
    }

    /** A RuleSet MUST throw a DuplicateRuleNameException
     *  if an attempt to add a Rule whose name conflicts
     *  with an already added Rule.
     */
    public void testAddRuleDuplicate()
        throws Exception
    {
        InstrumentedRule rule1 = new InstrumentedRule( "cheese" );
        InstrumentedRule rule2 = new InstrumentedRule( "cheese" );

        rule1.isValid( true );
        rule2.isValid( true );

        this.ruleSet.addRule( rule1 );

        try
        {
            this.ruleSet.addRule( rule2 );

            fail( "Should have thrown DuplicateRuleNameException" );
        }
        catch (DuplicateRuleNameException e)
        {
            assertSame( this.ruleSet,
                        e.getRuleSet() );

            assertSame( rule1,
                        e.getOriginalRule() );

            assertSame( rule2,
                        e.getConflictingRule() );
        }
    }
}
