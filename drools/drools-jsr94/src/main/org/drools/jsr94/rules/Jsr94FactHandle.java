package org.drools.jsr94.rules;

import org.drools.reteoo.FactHandleImpl;

import java.io.Serializable;

import javax.rules.Handle;

public class Jsr94FactHandle
    extends FactHandleImpl
    implements Handle, Serializable

{
    Jsr94FactHandle(long id)
    {
        super( id );
    }
}

