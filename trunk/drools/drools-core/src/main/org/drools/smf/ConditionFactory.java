package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;

public interface ConditionFactory
{
    Condition newCondition(Declaration[] availDecls,
                           Configuration config)
        throws FactoryException;
}
