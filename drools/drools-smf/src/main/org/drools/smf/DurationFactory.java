package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.spi.Duration;

public interface DurationFactory
{
    Duration newDuration(Configuration config,
                         Declaration[] availDecls)
        throws FactoryException;
}
