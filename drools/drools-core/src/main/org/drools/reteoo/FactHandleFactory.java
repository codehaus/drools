package org.drools.reteoo;

import java.io.Serializable;

import org.drools.FactHandle;

public interface FactHandleFactory
    extends
    Serializable
{
    FactHandle newFactHandle();

    FactHandle newFactHandle(long id);
}