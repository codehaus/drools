package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.rule.Declaration;
import org.drools.spi.Tuple;
import org.drools.MockObjectType;
import org.drools.MockFactHandle;
import org.drools.DroolsTestCase;

import java.util.List;
import java.util.Set;

public class ParameterNodeTest
    extends DroolsTestCase
{
    private Declaration decl;

    public void setUp()
    {
        this.decl = new Declaration( new MockObjectType(),
                                     "object" );
    }

    public void tearDown()
    {
        this.decl = null;
    }

    /** A ParameterNode MUST create a new tuple with a column
     *  based upon the initialization Declaration, containing
     *  the incoming Object as its value, and propagate it.
     */
    public void testAssertObject()
        throws Exception
    {
        Object object1 = new String( "cheese" );
        
        ParameterNode node = new ParameterNode( null,
                                                null,
                                                this.decl );
        
        InstrumentedTupleSink sink = new InstrumentedTupleSink();
        
        node.setTupleSink( sink );

        MockFactHandle handle = new MockFactHandle( 1 );
        
        node.assertObject( handle,
                           object1,
                           null );
        
        List asserted = sink.getAssertedTuples();
        
        assertEquals( 1,
                      asserted.size() );
        
        Tuple tuple = (Tuple) asserted.get( 0 );
        
        assertSame( object1,
                    tuple.get( this.decl ) );
    }
    
    /** A ParameterNode MUST return a set consisting of
     *  the initialization Declaration as its only member
     *  for getParamterDeclarations().
     */
    public void testGetTupleDeclarations()
    {
        ParameterNode node = new ParameterNode( null,
                                                null,
                                                this.decl );

        Set decls = node.getTupleDeclarations();
        
        assertLength( 1,
                      decls );
        
        assertContains( this.decl,
                        decls );
    }
}
