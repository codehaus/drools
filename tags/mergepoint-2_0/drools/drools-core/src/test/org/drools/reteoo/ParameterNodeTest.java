package org.drools.reteoo;

import java.util.List;
import java.util.Set;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.MockObjectType;
import org.drools.spi.Tuple;

public class ParameterNodeTest extends DroolsTestCase
{
    /**
     * A ParameterNode MUST create a new tuple with a column based upon the
     * initialization Declaration, containing the incoming Object as its value,
     * and propagate it.
     */
    public void testAssertObject() throws Exception
    {
        Object object1 = "cheese";

        Rule rule = new Rule( "test-rule 1" );
        Declaration paramDecl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );
        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        ParameterNode node = new ParameterNode( null, paramDecl );

        InstrumentedTupleSink sink = new InstrumentedTupleSink( );

        node.addTupleSink( sink );

        FactHandleImpl handle = new FactHandleImpl( 1 );
        WorkingMemoryImpl memory = (WorkingMemoryImpl) ruleBase.newWorkingMemory( );
        memory.putObject(handle, object1);

        node.assertObject( handle, object1, memory );

        List asserted = sink.getAssertedTuples( );

        assertEquals( 1, asserted.size( ) );

        Tuple tuple = ( Tuple ) asserted.get( 0 );

        assertSame( object1, tuple.get( paramDecl ) );
    }

    /**
     * A ParameterNode MUST return a set consisting of the initialization
     * Declaration as its only member for getParamterDeclarations().
     */
    public void testGetTupleDeclarations() throws Exception
    {
        Rule rule = new Rule( "test-rule 1" );
        Declaration paramDecl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );
        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence() );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition() );

        ParameterNode node = new ParameterNode( null, paramDecl );

        Set decls = node.getTupleDeclarations( );

        assertLength( 1, decls );

        assertContains( paramDecl, decls );
    }
}