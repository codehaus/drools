package org.drools.reteoo;

import org.drools.FactHandle;

public class DefaultFactHandleFactory
    implements
    FactHandleFactory
{
    private long idCounter;

    private long recencyCounter;

    public FactHandle newFactHandle()
    {
        return new FactHandleImpl( ++idCounter,
                                   ++recencyCounter );
    }

    public FactHandle newFactHandle(long id)
    {
        return new FactHandleImpl( id,
                                   ++recencyCounter );
    }

}
