package org.drools.jsr94.rules;

import javax.rules.Handle;

import org.drools.reteoo.FactHandleImpl;

public class Jsr94FactHandle extends FactHandleImpl implements Handle

{
    Jsr94FactHandle()
    {
    }

    Jsr94FactHandle(long id)
    {
        super( id );
    }
}

