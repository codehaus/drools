package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.spi.ConsequenceException;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
    private Timer scheduler;

    /** Scheduled tasks. */
    private Map tasks;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    private Scheduler()
    {
        this.scheduler = new Timer( true );

        this.tasks = new HashMap();
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

        Date then = new Date( now.getTime() + ( item.getRule().getDuration().getDuration( item.getTuple() ) * 1000 ) );

        try
        {
            TimerTask task = new AgendaItemFireListener( item,
                                                         workingMemory );

            this.scheduler.schedule( task,
                                     then );

            this.tasks.put( item,
                            task );
        }
        catch (Exception e)
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
        TimerTask task = (TimerTask) this.tasks.get( item );

        if ( task != null )
        {
            task.cancel();
        }
    }
}

/** Fire listener.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class AgendaItemFireListener extends TimerTask
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
     */
    public void run()
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
