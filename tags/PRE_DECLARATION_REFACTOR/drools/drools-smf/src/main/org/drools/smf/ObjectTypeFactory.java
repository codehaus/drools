package org.drools.smf;

import java.util.Set;
import org.drools.spi.ObjectType;

public interface ObjectTypeFactory
{
    ObjectType newObjectType(Configuration config, Set imports) throws FactoryException;    
}