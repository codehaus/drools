package org.drools.smf;

import org.drools.spi.Duration;

import java.util.List;

public interface DurationFactory
{
    Duration newDuration(Configuration config, List availDecls) throws FactoryException;    
}