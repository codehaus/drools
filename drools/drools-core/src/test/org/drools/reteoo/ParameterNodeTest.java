package org.drools.reteoo;

import java.util.List;
import java.util.Set;

import org.drools.DroolsTestCase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.MockObjectType;
import org.drools.spi.Tuple;

public class ParameterNodeTest
    extends DroolsTestCase
{
    private Declaration decl;

    public void setUp()
    {
        this.decl = new Declaration( new MockObjectType(true),
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

        Rule rule = new Rule( "test-rule 1" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );                                                 
        rule.addParameterDeclaration( paramDecl );
        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence() );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition() );        
        
        ParameterNode node = new ParameterNode( rule,
                                                null,
                                                this.decl );
        
        InstrumentedTupleSink sink = new InstrumentedTupleSink();
        
        node.setTupleSink( sink );

        FactHandleImpl handle = new FactHandleImpl( 1 );
        
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
