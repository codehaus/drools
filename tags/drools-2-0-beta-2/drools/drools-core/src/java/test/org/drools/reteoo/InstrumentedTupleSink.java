
package org.drools.reteoo;

import org.drools.WorkingMemory;

import java.util.List;
import java.util.ArrayList;

public class InstrumentedTupleSink implements TupleSink
{
    private List assertedTuples;
    private List retractedObjects;
    private List retractedKeys;

    public InstrumentedTupleSink()
    {
        this.assertedTuples   = new ArrayList();
        this.retractedObjects = new ArrayList();
    }

    public void assertTuple(TupleSource tupleSource,
                            ReteTuple tuple,
                            WorkingMemory workingMemory)
    {
        this.assertedTuples.add( tuple );
    }

    public List getAssertedTuples()
    {
        return this.assertedTuples;
    }

    public void retractObject(TupleSource tupleSource,
                              Object object,
                              WorkingMemory workingMemory)
    {
        this.retractedObjects.add( object );
    }

    public void retractTuples(TupleKey key,
                              WorkingMemory workingMemory)
    {
        this.retractedKeys.add( key );
    }

    public void modifyTuples(TupleSource tupleSource,
                             Object trigger,
                             TupleSet tupleSet,
                             WorkingMemory workingMemory)
    {

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
