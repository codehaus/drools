package org.drools.smf;

import org.drools.rule.Rule;
import org.drools.spi.Condition;

public interface ConditionFactory
{
    Condition newCondition(Configuration config, Rule rule) throws FactoryException;
}