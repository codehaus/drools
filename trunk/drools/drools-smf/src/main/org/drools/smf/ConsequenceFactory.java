package org.drools.smf;

import org.drools.rule.Rule;
import org.drools.spi.Consequence;

public interface ConsequenceFactory
{
    Consequence newConsequence(Configuration config, Rule rule) throws FactoryException;
}