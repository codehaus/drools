package org.drools.smf;

import org.drools.spi.Condition;

public interface ConditionFactory
{
    Condition newCondition(Configuration config)
        throws FactoryException;
}
