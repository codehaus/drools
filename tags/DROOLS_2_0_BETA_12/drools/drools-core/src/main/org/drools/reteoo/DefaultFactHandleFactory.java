package org.drools.reteoo;

import org.drools.FactHandle;

public class DefaultFactHandleFactory
    implements FactHandleFactory
{
    private long counter;

    public DefaultFactHandleFactory()
    {
        this.counter = 0;
    }

    public FactHandle newFactHandle()
    {
        return new FactHandleImpl( ++this.counter );
    }
}
