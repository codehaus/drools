package org.drools.reteoo;

import org.drools.FactHandle;

public class ExtractionHandleFactory
    implements
    FactHandleFactory
{
    private long idCounter;

    private long recencyCounter;

    public FactHandle newFactHandle()
    {
        return new ExtractionHandleImpl( ++idCounter,
                                   ++recencyCounter );
    }

    public FactHandle newFactHandle(long id)
    {
        return new ExtractionHandleImpl( id,
                                   ++recencyCounter );
    }

}
