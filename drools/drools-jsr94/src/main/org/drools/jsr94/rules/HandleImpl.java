package org.drools.jsr94.rules;

import org.drools.FactHandle;

import java.io.Serializable;

import javax.rules.Handle;

public class HandleImpl
    implements Handle, Serializable
{
    private FactHandle factHandle;

    HandleImpl(FactHandle factHandle)
    {
        this.factHandle = factHandle;
    }

    FactHandle getFactHandle()
    {
        return this.factHandle;
    }
}
