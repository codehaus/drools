package org.drools.reteoo;

import java.util.LinkedList;
import java.util.List;

import org.drools.FactHandle;

public class InstrumentedTupleSink implements TupleSink
{
    private final List assertedTuples = new LinkedList( );
    private final List retractedKeys = new LinkedList( );

    public void assertTuple(ReteTuple tuple, WorkingMemoryImpl workingMemory)
    {
        this.assertedTuples.add( tuple );
    }

    public List getAssertedTuples()
    {
        return this.assertedTuples;
    }

    public void retractTuples(TupleKey key, WorkingMemoryImpl workingMemory)
    {
        this.retractedKeys.add( key );
    }    

    public void modifyTuples(FactHandle trigger,
                             TupleSet tupleSet,
                             WorkingMemoryImpl workingMemory)
    {

    }    
    
    public List getRetractedKeys()
    {
        return this.retractedKeys;
    }
}