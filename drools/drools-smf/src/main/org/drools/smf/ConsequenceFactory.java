package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.spi.Consequence;

public interface ConsequenceFactory
{
    Consequence newConsequence(Configuration config,
                               Declaration[] availDecls)
        throws FactoryException;
}
