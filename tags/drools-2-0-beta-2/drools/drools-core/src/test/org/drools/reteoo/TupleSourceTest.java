
package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.RetractionException;

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

        ReteTuple tuple = new ReteTuple();

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
}
