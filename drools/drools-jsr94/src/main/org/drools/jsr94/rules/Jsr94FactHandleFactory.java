package org.drools.jsr94.rules;

import org.drools.FactHandle;
import org.drools.reteoo.FactHandleFactory;

public class Jsr94FactHandleFactory implements FactHandleFactory
{
    private long counter;

    public Jsr94FactHandleFactory()
    {
        this.counter = 0;
    }

    public synchronized FactHandle newFactHandle()
    {
        return new Jsr94FactHandle( ++this.counter );
    }
}