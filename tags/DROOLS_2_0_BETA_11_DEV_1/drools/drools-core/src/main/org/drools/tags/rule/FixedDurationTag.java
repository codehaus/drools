package org.drools.tags.rule;

/*
 $Id: FixedDurationTag.java,v 1.2 2003-03-25 19:47:32 tdiesler Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.drools.rule.FixedDuration;

/** Construct a <code>FixedDuration</code> for a <code>Rule</code>.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: FixedDurationTag.java,v 1.2 2003-03-25 19:47:32 tdiesler Exp $
 */
public class FixedDurationTag extends TagSupport
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Seconds. */
    private long seconds;

    /** Minutes. */
    private long minutes;

    /** Hours. */
    private long hours;

    /** Days. */
    private long days;

    /** Weeks. */
    private long weeks;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public FixedDurationTag()
    {
    }

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Set the number of seconds.
     *
     *  @param seconds Number of seconds.
     */
    public void setSeconds(long seconds)
    {
        this.seconds = seconds;
    }

    /** Set the number of minutes.
     *
     *  @param minutes Number of minutes.
     */
    public void setMinutes(long minutes)
    {
        this.minutes = minutes;
    }

    /** Set the number of hours.
     *
     *  @param hours Number of hours.
     */
    public void setHours(long hours)
    {
        this.hours = hours;
    }

    /** Set the number of days.
     *
     *  @param days Number of days.
     */
    public void setDays(long days)
    {
        this.days = days;
    }

    /** Set the number of weeks.
     *
     *  @param weeks Number of weeks.
     */
    public void setWeeks(long weeks)
    {
        this.weeks = weeks;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.apache.commons.jelly.Tag
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Perform this tag.
     *
     *  @param output The output sink.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to perform this tag.
     */
    public void doTag(XMLOutput output) throws MissingAttributeException, JellyTagException
    {
        DurationTag tag = (DurationTag) findAncestorWithClass( DurationTag.class );

        if ( tag == null )
        {
            throw new JellyTagException( "No duration available" );
        }

        FixedDuration duration = new FixedDuration();

        duration.addSeconds( this.seconds );
        duration.addMinutes( this.minutes );
        duration.addHours( this.hours );
        duration.addDays( this.days );
        duration.addWeeks( this.weeks );

        tag.setDuration( duration );
    }
}

