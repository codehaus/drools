package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.RuleBase;
import org.drools.conflict.SalienceConflictResolver;
import org.drools.rule.RuleSet;
import org.drools.WorkingMemory;

import junit.framework.TestCase;

import java.util.List;

public class TupleSourceTest
    extends TestCase
{
    public void setUp()
    {

    }

    public void tearDown()
    {

    }

    public void testPropagateAssertTuple()
    {
        TupleSource           source = new MockTupleSource(); 
        InstrumentedTupleSink sink   = new InstrumentedTupleSink();

        source.setTupleSink( sink );

    	RuleBase ruleBase = new RuleBaseImpl( new Rete(), new RuleSet[0], new SalienceConflictResolver());
    	WorkingMemory workingMemory = ruleBase.newWorkingMemory();
        ReteTuple tuple = new ReteTuple(workingMemory, null);

        try
        {
            source.propagateAssertTuple( tuple,
            		                     (WorkingMemoryImpl) workingMemory  );
            
            List tuples = sink.getAssertedTuples();
            
            assertEquals( 1,
                          tuples.size() );
            
            assertSame( tuple,
                        tuples.get( 0 ) );

            List objects = sink.getRetractedObjects();

            assertEquals( 0,
                          objects.size() );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }
}
