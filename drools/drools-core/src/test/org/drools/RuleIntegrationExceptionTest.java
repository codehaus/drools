package org.drools;

import junit.framework.TestCase;

import org.drools.rule.Rule;

public class RuleIntegrationExceptionTest
    extends TestCase
{
    public void testConstruct()
    {
        Rule rule = new Rule( "cheese" );

        RuleIntegrationException e = new RuleIntegrationException( rule );

        assertSame( rule,
                    e.getRule() );

        assertEquals( "cheese cannot be integrated",
                      e.getMessage() );
    }
}
