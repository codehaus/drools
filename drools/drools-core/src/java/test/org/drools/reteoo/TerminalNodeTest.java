
package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.spi.InstrumentedAction;

import junit.framework.TestCase;

import java.util.List;

public class TerminalNodeTest extends TestCase
{
    public TerminalNodeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testNothing()
    {
    }

    /** All Tuples asserted to a TerminalNode MUST be
     *  passed to the matching Action's invoke(..) method.
     */
    /*
    public void testAssertTuple()
    {
        InstrumentedAction action = new InstrumentedAction();
        TerminalNode       node = new TerminalNode( null,
                                                    action );

        ReteTuple tuple = new ReteTuple();

        try
        {
            node.assertTuple( null,
                              tuple,
                              null );
            
            List invoked = action.getInvokedTuples();

            assertEquals( 1,
                          invoked.size() );

            assertSame( tuple,
                        invoked.get( 0 ) );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }
    */
}
