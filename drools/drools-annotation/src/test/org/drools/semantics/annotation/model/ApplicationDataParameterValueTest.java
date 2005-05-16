package org.drools.semantics.annotation.model;

import junit.framework.TestCase;

import org.drools.WorkingMemory;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class ApplicationDataParameterValueTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );

    private static final String APP_DATA_NAME = "app-data-name";

    private Mock< WorkingMemory > mockWorkingMemory = mocks.createMock( WorkingMemory.class );
    private Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );

    public void testConstructionNullDeclaration( )
    {
        try
        {
            ApplicationDataParameterValue value = new ApplicationDataParameterValue( null,
                    Integer.class );
            fail( "expected IllegalArgumentException" );
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    public void testConstructionNullClass( )
    {
        try
        {
            ApplicationDataParameterValue value = new ApplicationDataParameterValue( "name", null );
            fail( "expected IllegalArgumentException" );
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    public void testGetValue( ) throws Exception
    {
        ApplicationDataParameterValue value = new ApplicationDataParameterValue( APP_DATA_NAME,
                Integer.class );
        Integer expectedValue = new Integer( 1 );

        mockTuple.control.expectAndReturn( mockTuple.object.getWorkingMemory( ),
                                           mockWorkingMemory.object );
        mockWorkingMemory.control.expectAndReturn( mockWorkingMemory.object
                .getApplicationData( "app-data-name" ), expectedValue );

        mocks.replay( );

        value.getValue( mockTuple.object );

        mocks.verify( );
    }

    public void testGetValueWrongClass( ) throws Exception
    {
        ApplicationDataParameterValue value = new ApplicationDataParameterValue( APP_DATA_NAME,
                Integer.class );
        String expectedValue = "value";

        mockTuple.control.expectAndReturn( mockTuple.object.getWorkingMemory( ),
                                           mockWorkingMemory.object );
        mockWorkingMemory.control.expectAndReturn( mockWorkingMemory.object
                .getApplicationData( "app-data-name" ), expectedValue );

        mocks.replay( );

        try
        {
            value.getValue( mockTuple.object );
            fail( "expected RuntimeException" );
        }
        catch (RuntimeException e)
        {
            // expected
        }

        mocks.verify( );
    }
}
