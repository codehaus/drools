package org.drools.reteoo;

import org.drools.FactHandle;

import java.io.Serializable;

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