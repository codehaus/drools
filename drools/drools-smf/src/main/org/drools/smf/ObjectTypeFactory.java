package org.drools.smf;

import org.drools.rule.Imports;
import org.drools.spi.ObjectType;

public interface ObjectTypeFactory
{
    ObjectType newObjectType(Configuration config, Imports imports) throws FactoryException;
}