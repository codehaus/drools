package org.drools.smf;

import org.drools.spi.Duration;

import java.util.Set;

public interface DurationFactory
{
    Duration newDuration(Configuration config, Set availDecls) throws FactoryException;    
}