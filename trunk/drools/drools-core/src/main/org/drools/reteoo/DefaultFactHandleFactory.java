package org.drools.reteoo;

import org.drools.FactHandle;

public class DefaultFactHandleFactory implements FactHandleFactory
{
    private long counter;

    public FactHandle newFactHandle()
    {
        return new FactHandleImpl( ++this.counter );
    }
    
    public FactHandle newFactHandle(long id)
    {
        return new FactHandleImpl( id );
    }    
    
}

