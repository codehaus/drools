package org.drools.jsr94.rules;

import org.drools.FactHandle;
import org.drools.reteoo.FactHandleFactory;
import java.io.Serializable;

public class Jsr94FactHandleFactory
    implements FactHandleFactory, Serializable
{
    private long counter;

    public Jsr94FactHandleFactory()
    {
        this.counter = 0;
    }

    public FactHandle newFactHandle()
    {
        return new Jsr94FactHandle( ++this.counter );
    }
}
