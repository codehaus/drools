package org.drools;

import org.drools.rule.Rule;

import junit.framework.TestCase;

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
