
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

        int seconds = 0;

        if ( secondsStr != null )
        {
            seconds = Integer.parseInt( secondsStr );
        }

        getReader().getCurrentRule().setDuration( seconds );
    }
}
