package org.drools.reteoo.impl;

/*
 $Id: Scheduler.java,v 1.3 2002-08-22 07:42:39 bob Exp $

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

import org.drools.WorkingMemory;
import org.drools.spi.ConsequenceException;

import fr.dyade.jdring.AlarmManager;
import fr.dyade.jdring.AlarmListener;
import fr.dyade.jdring.AlarmEntry;
import fr.dyade.jdring.PastDateException;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/** Scheduler for rules requiring truth duration.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class Scheduler
{
    // ------------------------------------------------------------
    //     Class members
    // ------------------------------------------------------------

    /** Singleton instance. */
    private static final Scheduler INSTANCE = new Scheduler();

    // ------------------------------------------------------------
    //     Class methods
    // ------------------------------------------------------------

    /** Retrieve the singleton instance.
     *
     *  @return The singleton instance.
     */
    static Scheduler getInstance()
    {
        return INSTANCE;
    }

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Alarm manager. */
    private AlarmManager scheduler;

    /** Scheduled entries. */
    private Map entries;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    private Scheduler()
    {
        this.scheduler = new AlarmManager( true,
                                           "DroolsScheduler" + new Date().getTime() );

        this.entries = new HashMap();
    }

    /** Schedule an agenda item.
     *
     *  @param item The item to schedule.
     *  @param workingMemory The working memory session.
     */
    void scheduleAgendaItem(AgendaItem item,
                            WorkingMemory workingMemory)
    {
        Date now = new Date();

        Date then = new Date( now.getTime() + ( item.getRule().getDuration( item.getTuple() ) * 1000 ) );

        try
        {
            AlarmEntry entry = this.scheduler.addAlarm( then,
                                                        new AgendaItemFireListener( item,
                                                                                    workingMemory ) );

            this.entries.put( item,
                              entry );
        }
        catch (PastDateException e)
        {
            e.printStackTrace();
        }
    }

    /** Cancel an agenda item.
     *
     *  @param item The item to cancle.
     */
    void cancelAgendaItem(AgendaItem item)
    {
        AlarmEntry entry = (AlarmEntry) this.entries.get( item );

        if ( entry != null )
        {
            this.scheduler.removeAlarm( entry );
        }
    }
}

/** Fire listener.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class AgendaItemFireListener implements AlarmListener
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The agenda item. */
    private AgendaItem item;

    /** The working-memory session. */
    private WorkingMemory workingMemory;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param item The agenda item.
     *  @param workingMemory The working memory session.
     */
    AgendaItemFireListener(AgendaItem item,
                           WorkingMemory workingMemory)
    {
        this.item          = item;
        this.workingMemory = workingMemory;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     fr.dyade.jdring.AlarmListener
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Handle the firing of an alarm.
     *
     *  @param alarmEntry The alarm entry.
     */
    public void handleAlarm(AlarmEntry alarmEntry)
    {
        try
        {
            this.item.fire( this.workingMemory );
        }
        catch (ConsequenceException e)
        {
            e.printStackTrace();
        }
    }
}
