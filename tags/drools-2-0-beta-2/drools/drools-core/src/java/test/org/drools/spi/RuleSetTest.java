
package org.drools.spi;

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

    /** A RuleSet MUST accept any Rule that does not have
     *  a conflicting name.
     */
    public void testAddRule()
    {
        InstrumentedRule rule = new InstrumentedRule( "cheese" );

        rule.isValid( true );

        try
        {
            this.ruleSet.addRule( rule );
        }
        catch (RuleConstructionException e)
        {
            fail( e.toString() );
        }
    }

    /** A RuleSet MUST throw a DuplicateRuleNameException
     *  if an attempt to add a Rule whose name conflicts
     *  with an already added Rule.
     */
    public void testAddRuleDuplicate()
    {
        InstrumentedRule rule1 = new InstrumentedRule( "cheese" );
        InstrumentedRule rule2 = new InstrumentedRule( "cheese" );

        rule1.isValid( true );
        rule2.isValid( true );

        try
        {
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
            catch (RuleConstructionException e)
            {
                fail( e.toString() );
            }
        }
        catch (RuleConstructionException e)
        {
            fail( e.toString() );
        }
    }
}
