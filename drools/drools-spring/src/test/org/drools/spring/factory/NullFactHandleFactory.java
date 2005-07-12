package org.drools.spring.factory;

import org.drools.FactHandle;
import org.drools.reteoo.FactHandleFactory;

public class NullFactHandleFactory implements FactHandleFactory {
    
    public FactHandle newFactHandle() {
        return null;
    }
    public FactHandle newFactHandle(long id) {
        return null;
    }
}

