package org.drools.reteoo;

import org.drools.FactHandle;

import java.io.Serializable;

public interface FactHandleFactory extends Serializable
{
    FactHandle newFactHandle();
}
