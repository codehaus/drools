package org.drools.reteoo;

import org.drools.FactHandle;

public class DefaultFactHandleFactory
    implements
    FactHandleFactory
{
    /** The fact id. */
    private long id;

    /** The number of facts created - used for recency. */
    private long counter;

    public final FactHandle newFactHandle()
    {
        return newFactHandle( ++id );
    }

    public final FactHandle newFactHandle(long id)
    {
        return new FactHandleImpl( id,
                                   ++counter );
    }
}
