package org.drools.reteoo;

import junit.framework.TestCase;
import org.drools.AssertionException;
import org.drools.RuleBase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.FalseCondition;
import org.drools.spi.MockObjectType;
import org.drools.spi.TrueCondition;

import java.util.List;
import java.util.Set;

public class ConditionNodeTest extends TestCase
{
    private RuleBase    ruleBase;
    private Rule        rule;
    private ReteTuple   tuple;

    public ConditionNodeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.ruleBase = new RuleBaseImpl( new Rete( ) );
        this.rule = new Rule( "test-rule 1" );

        //add consequence
        this.rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );

        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        this.tuple = new ReteTuple( (WorkingMemoryImpl) ruleBase.newWorkingMemory( ) );
    }

    public void tearDown()
    {
    }

    /**
     * If a condition allows an incoming Object, then the Object MUST be
     * propagated.
     */
    public void testAllowed()
    {
        ConditionNode node = new ConditionNode( rule, null, new TrueCondition( ) );

        InstrumentedTupleSink sink = new InstrumentedTupleSink( );

        node.addTupleSink( sink );

        try
        {
            RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );
            node.assertTuple( this.tuple, ( WorkingMemoryImpl ) ruleBase.newWorkingMemory( ) );

            List asserted = sink.getAssertedTuples( );

            assertEquals( 1, asserted.size( ) );

            ReteTuple tuple = ( ReteTuple ) asserted.get( 0 );

            assertSame( this.tuple, tuple );
        }
        catch ( AssertionException e )
        {
            fail( e.toString( ) );
        }
    }

    /**
     * If a Condition does not allow an incoming Object, then the object MUST
     * NOT be propagated.
     */
    public void testNotAllowed()
    {
        ConditionNode node = new ConditionNode( rule, null, new FalseCondition( ) );

        InstrumentedTupleSink sink = new InstrumentedTupleSink( );

        node.addTupleSink( sink );

        try
        {
            node.assertTuple( this.tuple, ( WorkingMemoryImpl ) this.ruleBase.newWorkingMemory( ) );

            List asserted = sink.getAssertedTuples( );

            assertEquals( 0, asserted.size( ) );
        }
        catch ( AssertionException e )
        {
            fail( e.toString( ) );
        }
    }

    /**
     * A FilterNode MUST delegate to its input source for getTupleDeclarations()
     * since it does not alter the structure of the Tuples.
     */
    public void testGetTupleDeclarations() throws Exception
    {
        Declaration decl = this.rule.addParameterDeclaration( "object", new MockObjectType( Object.class ) );

        ParameterNode paramNode = new ParameterNode( null, decl );

        ConditionNode condNode = new ConditionNode( rule, paramNode, null );

        Set decls = condNode.getTupleDeclarations( );

        assertEquals( 1, decls.size( ) );

        assertTrue( decls.contains( decl ) );
    }
}