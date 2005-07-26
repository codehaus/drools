package org.drools.reteoo;

import org.drools.FactException;

public interface ObjectSink
{
    void assertObject(Object object,
                      FactHandleImpl handle,
                      PropagationContext context,
                      WorkingMemoryImpl workingMemory) throws FactException;

    void retractObject(FactHandleImpl handle,
                       PropagationContext context,
                       WorkingMemoryImpl workingMemory) throws FactException;
}
