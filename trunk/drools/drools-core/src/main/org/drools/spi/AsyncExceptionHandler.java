package org.drools.spi;

import org.drools.WorkingMemory;

public interface AsyncExceptionHandler
{

    void handleException(ConsequenceException exception, WorkingMemory workingMemory);
    
}
