package org.drools.reteoo;

import java.util.List;

public interface BetaNodeDecorator
{
    
    TupleSet addLeftTuple(BetaMemory betaNodeMemory,
                          ReteTuple leftTuple,
                          WorkingMemoryImpl workingMemory);
    
    TupleSet addRightHandle(BetaMemory betaNodeMemory,
                            Object object,
                            FactHandleImpl handle,
                            WorkingMemoryImpl workingMemory);  
    
    List retractLeftTuple(BetaMemory betaNodeMemory,
                          TupleKey key,
                          WorkingMemoryImpl workingMemory);      
    
    List retractRightHandle(BetaMemory betaNodeMemory,
                            FactHandleImpl handle,
                            WorkingMemoryImpl workingMemory);                            
}
