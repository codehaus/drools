package org.drools.semantics.base;

/*
 * $Id: BaseDurationFactory.java,v 1.10 2004-12-14 21:00:26 mproctor Exp $
 *
 * Copyright 2003-2004 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.DurationFactory;
import org.drools.spi.Duration;
import org.drools.spi.RuleBaseContext;

/**
 * An implementation of the <code>DurationFactory</code> interface.
 */
public class BaseDurationFactory implements DurationFactory
{
    /** The number of seconds in one minute (60). */
    private static final int SECONDS_IN_A_MINUTE = 60;
    
    /** The number of seconds in one hour (60 * 60). */
    private static final int SECONDS_IN_AN_HOUR = 60 * SECONDS_IN_A_MINUTE;
    
    /** The number of seconds in one day (60 * 60 * 24). */
    private static final int SECONDS_IN_A_DAY = 24 * SECONDS_IN_AN_HOUR;
    
    /**
     * Returns a new <code>Duration</code> object configured
     * using the given <code>Configuration</code>.
     * 
     * @param config a <code>Configuration</code> object containing "days",
     *        "hours", "minutes", and/or "seconds" attributes. All attributes
     *        are optional and the given <code>Configuration</code> object
     *        may be <code>null</code>.
     * 
     * @return a new <code>Duration</code> object.
     */
    public Duration newDuration( Rule rule,
                                 RuleBaseContext context,
                                 Configuration config )
    {
        long seconds = 0;

        if ( null != config )
        {
            String daysStr = config.getAttribute( "days" );
            String hoursStr = config.getAttribute( "hours" );
            String minutesStr = config.getAttribute( "minutes" );
            String secondsStr = config.getAttribute( "seconds" );
    
            if ( daysStr != null && !daysStr.trim( ).equals( "" ) )
            {
                seconds += Integer.parseInt( daysStr.trim( ) ) * SECONDS_IN_A_DAY;
            }
    
            if ( hoursStr != null && !hoursStr.trim( ).equals( "" ) )
            {
                seconds += Integer.parseInt( hoursStr.trim( ) ) * SECONDS_IN_AN_HOUR;
            }
    
            if ( minutesStr != null && !minutesStr.trim( ).equals( "" ) )
            {
                seconds += Integer.parseInt( minutesStr.trim( ) ) * SECONDS_IN_A_MINUTE;
            }
    
            if ( secondsStr != null && !secondsStr.trim( ).equals( "" ) )
            {
                seconds += Integer.parseInt( secondsStr.trim( ) );
            }
        }

        return new BaseDuration( rule, seconds );
    }
}
