
package org.drools.reteoo.impl;

import org.drools.AssertionException;
import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.spi.TrueCondition;
import org.drools.spi.FalseCondition;
import org.drools.semantic.java.JavaObjectType;

import junit.framework.TestCase;

import java.util.List;
import java.util.Set;

public class ConditionNodeImplTest extends TestCase
{
    private ReteTuple tuple;

    public ConditionNodeImplTest(String name)
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

    /** If a condition allows an incoming Object, then
     *  the Object MUST be propagated.
     */
    public void testAllowed()
    {
        ConditionNodeImpl node = new ConditionNodeImpl( null,
                                                  new TrueCondition() );
                                         

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        node.setTupleSink( sink );

        try
        {
            node.assertTuple( this.tuple,
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

    /** If a Condition does not allow an incoming Object,
     *  then the object MUST NOT be propagated.
     */
    public void testNotAllowed()
    {
        ConditionNodeImpl node = new ConditionNodeImpl( null,
                                                  new FalseCondition() );

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        node.setTupleSink( sink );

        try
        {
            node.assertTuple( this.tuple,
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

        ParameterNodeImpl paramNode = new ParameterNodeImpl( null,
                                                             decl );

        ConditionNodeImpl condNode = new ConditionNodeImpl( paramNode,
                                                        null );

        Set decls = condNode.getTupleDeclarations();

        assertEquals( 1,
                      decls.size() );

        assertTrue( decls.contains( decl ) );
    }
}
