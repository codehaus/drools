
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.spi.ActionInvokationException;

import fr.dyade.jdring.AlarmManager;
import fr.dyade.jdring.AlarmListener;
import fr.dyade.jdring.AlarmEntry;
import fr.dyade.jdring.PastDateException;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

class Scheduler
{
    private static final Scheduler instance = new Scheduler();

    static Scheduler getInstance()
    {
        return instance;
    }

    private AlarmManager scheduler;
    private Map          entries;

    private Scheduler()
    {
        this.scheduler = new AlarmManager( true,
                                           "DroolsScheduler" + new Date().getTime() );

        this.entries = new HashMap();
    }

    void scheduleAgendaItem(AgendaItem item,
                            WorkingMemory workingMemory)
    {
        Date now = new Date();

        Date then = new Date( now.getTime() + ( item.getDuration() * 1000 ) );

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

    void cancelAgendaItem(AgendaItem item)
    {
        AlarmEntry entry = (AlarmEntry) this.entries.get( item );

        if ( entry != null )
        {
            this.scheduler.removeAlarm( entry );
        }
    }
}

class AgendaItemFireListener implements AlarmListener
{
    private AgendaItem    item;
    private WorkingMemory workingMemory;

    AgendaItemFireListener(AgendaItem item,
                           WorkingMemory workingMemory)
    {
        this.item          = item;
        this.workingMemory = workingMemory;
    }

    public void handleAlarm(AlarmEntry alarmEntry)
    {
        try
        {
            this.item.fire( this.workingMemory );
        }
        catch (ActionInvokationException e)
        {
            e.printStackTrace();
        }
    }
}
