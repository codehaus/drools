package org.drools.reteoo;

import java.util.ArrayList;
import java.util.List;

import org.drools.FactHandle;

public class InstrumentedTupleSink implements TupleSink
{
    private List assertedTuples;

    private List retractedObjects;

    private List retractedKeys;

    public InstrumentedTupleSink()
    {
        this.assertedTuples = new ArrayList( );
        this.retractedObjects = new ArrayList( );
    }

    public void assertTuple(ReteTuple tuple, WorkingMemoryImpl workingMemory)
    {
        this.assertedTuples.add( tuple );
    }

    public List getAssertedTuples()
    {
        return this.assertedTuples;
    }

    public void retractObject(Object object, WorkingMemoryImpl workingMemory)
    {
        this.retractedObjects.add( object );
    }

    public void retractTuples(TupleKey key, WorkingMemoryImpl workingMemory)
    {
        this.retractedKeys.add( key );
    }

    public List getRetractedObjects()
    {
        return this.retractedObjects;
    }

    public List getRetractedTupleKeys()
    {
        return this.retractedKeys;
    }
}