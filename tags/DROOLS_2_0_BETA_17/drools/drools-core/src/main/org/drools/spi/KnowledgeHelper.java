package org.drools.spi;

import org.drools.FactHandle;
import org.drools.FactException;

public class KnowledgeHelper
{
    private Tuple tuple;

    public KnowledgeHelper(Tuple tuple)
    {
        this.tuple = tuple;
    }

    public void assertObject(Object object)
        throws FactException
    {
        this.tuple.getWorkingMemory().assertObject( object );
    }

    public void modifyObject(Object object)
        throws FactException
    {
        FactHandle handle = this.tuple.getFactHandleForObject( object );

        this.tuple.getWorkingMemory().modifyObject( handle,
                                                    object );
    }

    public void modifyObject(Object oldObject,
                             Object newObject)
        throws FactException
    {
        FactHandle handle = this.tuple.getFactHandleForObject( oldObject );

        this.tuple.getWorkingMemory().modifyObject( handle,
                                                    newObject );
    }

    public void retractObject(Object object)
        throws FactException
    {
        FactHandle handle = this.tuple.getFactHandleForObject( object );

        this.tuple.getWorkingMemory().retractObject( handle );
    }

                             
}
