package org.drools.semantics.base;

import org.drools.spi.Duration;
import org.drools.spi.Tuple;

public class BaseDuration
{
    private long seconds;

    public BaseDuration(long seconds)
    {
        this.seconds = seconds;
    }

    public long getDuration(Tuple tuple)
    {
        return this.seconds;
    }
}
