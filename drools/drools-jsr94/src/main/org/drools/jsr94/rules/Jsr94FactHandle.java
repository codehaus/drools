package org.drools.jsr94.rules;

import org.drools.reteoo.FactHandleImpl;

import javax.rules.Handle;

public class Jsr94FactHandle extends FactHandleImpl implements Handle

{
    Jsr94FactHandle(long id)
    {
        super( id );
    }
}

