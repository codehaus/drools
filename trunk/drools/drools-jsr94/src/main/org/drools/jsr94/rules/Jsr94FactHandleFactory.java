package org.drools.jsr94.rules;

import org.drools.FactHandle;
import org.drools.reteoo.FactHandleFactory;

public final class Jsr94FactHandleFactory implements FactHandleFactory
{
    private static final Jsr94FactHandleFactory INSTANCE = new Jsr94FactHandleFactory( );

    private long counter;

    private Jsr94FactHandleFactory()
    {
    }

    public static Jsr94FactHandleFactory getInstance()
    {
        return INSTANCE;
    }

    public synchronized FactHandle newFactHandle()
    {
        return new Jsr94FactHandle( ++this.counter );
    }
}