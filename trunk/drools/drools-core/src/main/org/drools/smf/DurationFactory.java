package org.drools.smf;

import org.drools.spi.Duration;

public interface DurationFactory
{
    Duration newDuration(Configuration config)
        throws FactoryException;
}
