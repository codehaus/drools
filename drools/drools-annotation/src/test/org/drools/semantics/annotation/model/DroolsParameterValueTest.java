package org.drools.semantics.annotation.model;

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.semantics.annotation.Drools;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class DroolsParameterValueTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );

    private Mock< Rule > mockRule = mocks.createMock( Rule.class );
    private Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );

    public void testConstructionNullRule( )
    {
        try
        {
            DroolsParameterValue value = new DroolsParameterValue( null );
            fail( "expected IllegalArgumentException" );
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    public void testGetValue( )
    {
        DroolsParameterValue value = new DroolsParameterValue( mockRule.object );
        mocks.replay( );

        Drools drools = value.getValue( mockTuple.object );

        // TODO Asserting the instanceof is a bit weak.
        assertTrue( drools instanceof KnowledgeHelperDrools );
        mocks.verify( );
    }
}
