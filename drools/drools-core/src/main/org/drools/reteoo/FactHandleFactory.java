package org.drools.reteoo;

import java.io.Serializable;

import org.drools.FactHandle;

public interface FactHandleFactory
    extends
    Serializable
{
    /**
     * Construct a handle with a new id.
     * @return The handle.
     */
    FactHandle newFactHandle();

    /**
     * Construct a handle with a specified id.
     * @param id The id to use.
     * @return The handle.
     */
    FactHandle newFactHandle(long id);
}
