package org.drools.rule;

/*
 * $Id: FixedDuration.java,v 1.4 2004-11-19 02:14:17 mproctor Exp $
 * 
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a trademark of The Werken Company.
 * 
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
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

import org.drools.spi.Duration;
import org.drools.spi.Tuple;

/**
 * A fixed truthness duration.
 * 
 * @see Rule#setDuration
 * @see Rule#getDuration
 * 
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * 
 * @version $Id: FixedDuration.java,v 1.4 2004-11-19 02:14:17 mproctor Exp $
 */
public class FixedDuration
    implements
    Duration
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** Duration, in seconds. */
    private long duration;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    public FixedDuration()
    {
        this.duration = 0;
    }

    /**
     * Construct.
     * 
     * @param seconds
     *            Number of seconds.
     */
    public FixedDuration(long seconds)
    {
        this.duration = seconds;
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Add seconds.
     * 
     * @param seconds
     *            Number of seconds.
     */
    public void addSeconds(long seconds)
    {
        this.duration += seconds;
    }

    /**
     * Add minutes.
     * 
     * @param minutes
     *            Number of minutes.
     */
    public void addMinutes(long minutes)
    {
        this.duration += (minutes * 60);
    }

    /**
     * Add hours.
     * 
     * @param hours
     *            Number of hours.
     */
    public void addHours(long hours)
    {
        this.duration += (hours * 60 * 60);
    }

    /**
     * Add days.
     * 
     * @param days
     *            Number of days.
     */
    public void addDays(long days)
    {
        this.duration += (days * 60 * 60 * 24);
    }

    /**
     * Add weeks.
     * 
     * @param weeks
     *            Number of weeks.
     */
    public void addWeeks(long weeks)
    {
        this.duration += (weeks * 60 * 60 * 24 * 7);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * @see Duration
     */
    public long getDuration(Tuple tuple)
    {
        return this.duration;
    }
}