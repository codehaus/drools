package org.drools.semantics.annotation.model;

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class KnowledgeHelperParameterValueTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );

    private Mock< Rule > mockRule = mocks.createMock( Rule.class );
    private Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );

    public void testConstructionNullRule( )
    {
        try
        {
            KnowledgeHelperParameterValue value = new KnowledgeHelperParameterValue( null );
            fail( "expected IllegalArgumentException" );
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    public void testGetValue( )
    {
        KnowledgeHelperParameterValue value = new KnowledgeHelperParameterValue( mockRule.object );
        mocks.replay( );

        KnowledgeHelper drools = value.getValue( mockTuple.object );

        // TODO Asserting the instanceof is a bit weak.
        assertTrue( drools instanceof DefaultKnowledgeHelper);
        mocks.verify( );
    }
}
