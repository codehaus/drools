
package org.drools.semantic.java.io;

import org.dom4j.Element;
import org.dom4j.ElementPath;

class DurationHandler extends BaseRuleSetHandler
{
    DurationHandler(RuleSetReader reader)
    {
        super( reader );
    }

    public void onEnd(ElementPath path)
    {
        Element elem = path.getCurrent();

        String secondsStr = elem.attributeValue( "seconds" );
        String minutesStr = elem.attributeValue( "minutes" );
        String hoursStr   = elem.attributeValue( "hours" );
        String daysStr    = elem.attributeValue( "days" );

        int seconds = 0;
        int minutes = 0;
        int hours   = 0;
        int days    = 0;

        if ( secondsStr != null )
        {
            seconds = Integer.parseInt( secondsStr );
        }

        if ( minutesStr != null )
        {
            minutes = Integer.parseInt( minutesStr );
        }

        if ( hoursStr != null )
        {
            hours = Integer.parseInt( hoursStr );
        }

        if ( daysStr != null )
        {
            days = Integer.parseInt( daysStr );
        }

        long totalTime = seconds + ( minutes * 60 ) + ( hours * 60 * 60 ) + ( days * 60 * 60 * 24 );

        getReader().getCurrentRule().setDuration( totalTime );
    }
}
