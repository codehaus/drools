package org.drools.spi;

import junit.framework.TestCase;

import org.drools.rule.Rule;

/**
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris</a>
 */
public class RuleNameEqualsAgendaFilterTest extends TestCase
{
    private final RuleNameEqualsAgendaFilter filter = new RuleNameEqualsAgendaFilter( "foo" );

    public void testExactRuleNameIsAccepted()
    {
        assertTrue( filter.accept( new MockAgendaItem( new MockTuple( ), new Rule( "foo" ) ) ) );
    }

    public void testRuleNamePrefixIsNotAccepted()
    {
        assertFalse( filter.accept( new MockAgendaItem( new MockTuple( ), new Rule( "foobar" ) ) ) );
    }

    public void testRuleNameSuffixIsNotAccepted()
    {
        assertFalse( filter.accept( new MockAgendaItem( new MockTuple( ), new Rule( "snafoo" ) ) ) );
    }

    public void testEntrelyDifferentRuleNameIsNotAccepted()
    {
        assertFalse( filter.accept( new MockAgendaItem( new MockTuple( ), new Rule( "baz" ) ) ) );
    }
}