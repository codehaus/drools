package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;

public interface ConditionFactory
{
    Condition newCondition(Configuration config, Declaration[] availDecls) throws FactoryException;
}