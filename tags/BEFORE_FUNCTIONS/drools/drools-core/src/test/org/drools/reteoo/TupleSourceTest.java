package org.drools.reteoo;

import junit.framework.TestCase;
import org.drools.AssertionException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.rule.Rule;

import java.util.List;

public class TupleSourceTest extends TestCase
{
    public void testPropagateAssertTuple()
    {
        TupleSource source = new MockTupleSource( );
        InstrumentedTupleSink sink = new InstrumentedTupleSink( );

        source.addTupleSink( sink );

        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );
        WorkingMemory workingMemory = ruleBase.newWorkingMemory( );

        Rule rule = new Rule( "test-rule" );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( (WorkingMemoryImpl) workingMemory );

        try
        {
            source.propagateAssertTuple( tuple,
                                         ( WorkingMemoryImpl ) workingMemory );

            List tuples = sink.getAssertedTuples( );

            assertEquals( 1, tuples.size( ) );

            assertSame( tuple, tuples.get( 0 ) );

            assertTrue( sink.getRetractedKeys( ).isEmpty( ) );
        }
        catch ( AssertionException e )
        {
            fail( e.toString( ) );
        }
    }
}