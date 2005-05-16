package org.drools.semantics.annotation.model;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

import junit.framework.TestCase;

public class TupleParameterValueTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );

    private static class ValueClass extends Object
    {}

    Declaration declaration;
    ValueClass expectedValue = new ValueClass( );
    Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );

    private TupleParameterValue value;

    protected void setUp( ) throws Exception
    {
        Rule rule = new Rule( "for-test.declaration-factory" );
        declaration = rule.addParameterDeclaration( "parameter-name", null );

        value = new TupleParameterValue( declaration );
    }

    public void testConstructionNullDeclaration( )
    {
        try
        {
            TupleParameterValue value = new TupleParameterValue( null );
            fail( "expected IllegalArgumentException" );
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    public void testGetValue( ) throws Exception
    {
        mockTuple.control.expectAndReturn( mockTuple.object.get( declaration ), expectedValue );
        mocks.replay( );

        value.getValue( mockTuple.object );

        mocks.verify( );
    }
}
