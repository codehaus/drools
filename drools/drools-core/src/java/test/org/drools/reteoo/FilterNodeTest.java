
package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.spi.Declaration;
import org.drools.spi.FilterCondition;
import org.drools.spi.TrueFilterCondition;
import org.drools.spi.FalseFilterCondition;
import org.drools.semantic.java.JavaObjectType;

import junit.framework.TestCase;

import java.util.List;
import java.util.Set;

public class FilterNodeTest extends TestCase
{
    private ReteTuple tuple;

    public FilterNodeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.tuple = new ReteTuple();
    }

    public void tearDown()
    {
    }

    /** If a FilterCondition allows an incoming Object, then
     *  the Object MUST be propagated.
     */
    public void testAllowed()
    {
        FilterNode node = new FilterNode( null,
                                          new TrueFilterCondition() );
                                         

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        node.setTupleSink( sink );

        try
        {
            node.assertTuple( null,
                              this.tuple,
                              null );
            
            List asserted = sink.getAssertedTuples();
            
            assertEquals( 1,
                          asserted.size() );
            
            ReteTuple tuple = (ReteTuple) asserted.get( 0 );
            
            assertSame( this.tuple,
                        tuple );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }

    /** If a FilterCondition does not allow an incoming Object,
     *  then the object MUST NOT be propagated.
     */
    public void testNotAllowed()
    {
        FilterNode node = new FilterNode( null,
                                          new FalseFilterCondition() );

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        node.setTupleSink( sink );

        try
        {
            node.assertTuple( null,
                              this.tuple,
                              null );
            
            List asserted = sink.getAssertedTuples();
            
            assertEquals( 0,
                          asserted.size() );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }

    /** A FilterNode MUST delegate to its input source
     *  for getTupleDeclarations() since it does not alter
     *  the structure of the Tuples.
     */
    public void testGetTupleDeclarations()
    {
        Declaration decl = new Declaration( new JavaObjectType( String.class ),
                                            "object" );

        ParameterNode paramNode = new ParameterNode( null,
                                                     decl );

        FilterNode filterNode = new FilterNode( paramNode,
                                                null );

        Set decls = filterNode.getTupleDeclarations();

        assertEquals( 1,
                      decls.size() );

        assertTrue( decls.contains( decl ) );
    }
}
