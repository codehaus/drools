package org.drools.smf;

import org.drools.spi.ObjectType;

public interface ObjectTypeFactory
{
    ObjectType newObjectType(Configuration config)
        throws FactoryException;
}
