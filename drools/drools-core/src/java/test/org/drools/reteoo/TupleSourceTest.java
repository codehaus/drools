
package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.spi.Tuple;

import junit.framework.TestCase;

import java.util.List;

public class TupleSourceTest extends TestCase
{
    public TupleSourceTest(String name)
    {
        super( name );
    }

    public void setUp()
    {

    }

    public void tearDown()
    {

    }

    public void testPropagateAssertTuple()
    {
        TupleSource           source = new MockTupleSource(); 
        InstrumentedTupleSink sink   = new InstrumentedTupleSink();

        source.setTupleSink( sink );

        Tuple tuple = new ReteTuple();

        try
        {
            source.propagateAssertTuple( tuple,
                                         null );
            
            List tuples = sink.getAssertedTuples();
            
            assertEquals( 1,
                          tuples.size() );
            
            assertSame( tuple,
                        tuples.get( 0 ) );

            List objects = sink.getRetractedObjects();

            assertEquals( 0,
                          objects.size() );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }

    public void testPropagateRetractObject()
    {
        TupleSource   source = new MockTupleSource(); 
        InstrumentedTupleSink sink   = new InstrumentedTupleSink();

        source.setTupleSink( sink );

        Object object = new Object();

        try
        {
            source.propagateRetractObject( object,
                                           null );
            
            List objects = sink.getRetractedObjects();
            
            assertEquals( 1,
                          objects.size() );
            
            assertSame( object,
                        objects.get( 0 ) );

            List tuples = sink.getAssertedTuples();

            assertEquals( 0,
                          tuples.size() );
        }
        catch (RetractionException e)
        {
            fail( e.toString() );
        }
    }
}
