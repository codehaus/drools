
package org.drools.reteoo.impl;

import org.drools.AssertionException;
import org.drools.rule.Declaration;
import org.drools.spi.Tuple;
import org.drools.semantics.java.ClassObjectType;

import junit.framework.TestCase;

import java.util.List;
import java.util.Set;

public class ParameterNodeTest extends TestCase
{
    private Declaration decl;

    public ParameterNodeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.decl = new Declaration( new ClassObjectType( String.class ),
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
    {
        Object object1 = new String( "cheese" );

        ParameterNodeImpl     node = new ParameterNodeImpl( null,
                                                        this.decl );

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        node.setTupleSink( sink );

        try
        {
            node.assertObject( object1,
                               null );
            
            List asserted = sink.getAssertedTuples();
            
            assertEquals( 1,
                          asserted.size() );
            
            Tuple tuple = (Tuple) asserted.get( 0 );

            assertSame( object1,
                        tuple.get( this.decl ) );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }

    /** A ParameterNode MUST return a set consisting of
     *  the initialization Declaration as its only member
     *  for getParamterDeclarations().
     */
    public void testGetTupleDeclarations()
    {
        ParameterNodeImpl node = new ParameterNodeImpl( null,
                                                        this.decl );

        Set decls = node.getTupleDeclarations();

        assertEquals( 1,
                      decls.size() );

        assertTrue( decls.contains( this.decl ) );
    }
}
