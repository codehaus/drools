package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Declaration;
import org.drools.rule.RuleSet;
import org.drools.spi.Condition;
import org.drools.spi.TrueCondition;
import org.drools.spi.FalseCondition;
import org.drools.MockObjectType;

import junit.framework.TestCase;

import java.util.List;
import java.util.Set;

public class ConditionNodeTest
    extends TestCase
{
    private ReteTuple tuple;

    public ConditionNodeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    	RuleBase ruleBase = new RuleBaseImpl( new Rete(), new RuleSet[0], new DefaultConflictResolver());
        this.tuple = new ReteTuple(ruleBase.newWorkingMemory(), null);
    }

    public void tearDown()
    {
    }

    /** If a condition allows an incoming Object, then
     *  the Object MUST be propagated.
     */
    public void testAllowed()
    {
        ConditionNode node = new ConditionNode( null,
                                                new TrueCondition() );


        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        node.setTupleSink( sink );

        try
        {
        	RuleBase ruleBase = new RuleBaseImpl( new Rete(), new RuleSet[0], new DefaultConflictResolver());
            node.assertTuple( this.tuple,
            		(WorkingMemoryImpl) ruleBase.newWorkingMemory() );

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
        ConditionNode node = new ConditionNode( null,
                                                new FalseCondition() );

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        node.setTupleSink( sink );

        try
        {
        	RuleBase ruleBase = new RuleBaseImpl( new Rete(), new RuleSet[0], new DefaultConflictResolver());
            node.assertTuple( this.tuple,
                              (WorkingMemoryImpl) ruleBase.newWorkingMemory() );

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
        Declaration decl = new Declaration( new MockObjectType( Object.class ),
                                            "object" );

        ParameterNode paramNode = new ParameterNode( null,
                                                     null,
                                                     decl );

        ConditionNode condNode = new ConditionNode( paramNode,
                                                    null );

        Set decls = condNode.getTupleDeclarations();

        assertEquals( 1,
                      decls.size() );

        assertTrue( decls.contains( decl ) );
    }
}
