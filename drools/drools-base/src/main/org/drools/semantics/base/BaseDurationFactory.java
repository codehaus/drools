package org.drools.semantics.base;

import org.drools.smf.Configuration;
import org.drools.smf.DurationFactory;
import org.drools.spi.Duration;

import java.util.List;

public class BaseDurationFactory implements DurationFactory
{
    public Duration newDuration(Configuration config, List availDecls)
    {
        String daysStr = config.getAttribute( "days" );
        String hoursStr = config.getAttribute( "hours" );
        String minutesStr = config.getAttribute( "minutes" );
        String secondsStr = config.getAttribute( "seconds" );

        long seconds = 0;

        if ( daysStr != null && !daysStr.trim( ).equals( "" ) )
        {
            seconds += Integer.parseInt( daysStr.trim( ) );
        }

        if ( hoursStr != null && !hoursStr.trim( ).equals( "" ) )
        {
            seconds += Integer.parseInt( hoursStr.trim( ) );
        }

        if ( minutesStr != null && !minutesStr.trim( ).equals( "" ) )
        {
            seconds += Integer.parseInt( minutesStr.trim( ) );
        }

        if ( secondsStr != null && !secondsStr.trim( ).equals( "" ) )
        {
            seconds += Integer.parseInt( secondsStr.trim( ) );
        }

        return new BaseDuration( seconds );
    }
}