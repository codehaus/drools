
package org.drools.reteoo;

import org.drools.WorkingMemory;

import org.drools.spi.Action;
import org.drools.spi.Declaration;
import org.drools.spi.ActionInvokationException;

import fr.dyade.jdring.AlarmManager;
import fr.dyade.jdring.AlarmListener;
import fr.dyade.jdring.AlarmEntry;
import fr.dyade.jdring.PastDateException;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Date;

/** Rule-firing Agenda.
 *
 *  <p>
 *  Since many rules may be matched by a single assertObject(...)
 *  all scheduled actions are placed into the <code>Agenda</code>.
 *  </p>
 *
 *  <p>
 *  While processing a scheduled action, it may modify or retract
 *  objects in other scheduled actions, which must then be removed
 *  from the agenda.  Non-invalidated actions are left on the agenda,
 *  and are executed in turn.
 *  </p>
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class Agenda
{
    /** Working memory of this Agenda. */
    private WorkingMemory workingMemory;

    /** Items in the agenda. */
    private LinkedList items;

    /** Items time-delayed. */
    private LinkedList scheduledItems;

    private AlarmManager scheduler;

    /** Construct.
     *
     *  @param workingMemory The <code>WorkingMemory</code> of this agenda.
     */
    public Agenda(WorkingMemory workingMemory)
    {
        this.workingMemory = workingMemory;

        this.items          = new LinkedList();
        this.scheduledItems = new LinkedList();

        this.scheduler = new AlarmManager( true,
                                           this + " scheduler" );
    }

    /** Schedule a rule action invokation on this <code>Agenda</code>.
     *
     *  @param tuple The matching <code>Tuple</code>.
     *  @param action The <code>Action</code> to fire.
     */
    void addToAgenda(ReteTuple tuple,
                     Action action,
                     long duration)
    {
        if ( action == null )
        {
            return;
        }

        AgendaItem item = new AgendaItem( tuple,
                                          action,
                                          duration );
        if ( duration > 0 )
        {
            this.scheduledItems.add( item );
            scheduleItem( item );
        }
        else
        {
            this.items.add( item );
        }
    }

    void removeFromAgenda(TupleKey key,
                          Action action)
    {
        if ( action == null )
        {
            return;
        }

        Iterator   itemIter = this.items.iterator();
        AgendaItem eachItem = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.getAction() == action )
            {
                if ( eachItem.getTuple().getKey().containsAll( key ) )
                {
                    itemIter.remove();
                }
            }
        }

        itemIter = this.scheduledItems.iterator();
        eachItem = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.getAction() == action )
            {
                if ( eachItem.getTuple().getKey().containsAll( key ) )
                {
                    cancelItem( eachItem );
                    itemIter.remove();
                }
            }
        }
    }

    void modifyAgenda(TupleSet newTuples,
                      Action action,
                      long duration)
    {
        Iterator   itemIter  = this.items.iterator();
        AgendaItem eachItem  = null;
        ReteTuple  eachTuple = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.getAction() == action )
            {
                eachTuple = eachItem.getTuple();

                if ( ! newTuples.containsTuple( eachTuple.getKey() ) )
                {
                    itemIter.remove();
                }
                else
                {
                    eachItem.setTuple( newTuples.getTuple( eachTuple.getKey() ) );
                    newTuples.removeTuple( eachTuple.getKey() );
                }
            }
        }

        itemIter = this.scheduledItems.iterator();
        eachItem = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.getAction() == action )
            {
                eachTuple = eachItem.getTuple();

                if ( ! newTuples.containsTuple( eachTuple.getKey() ) )
                {
                    cancelItem( eachItem );
                    itemIter.remove();
                }
                else
                {
                    eachItem.setTuple( newTuples.getTuple( eachTuple.getKey() ) );
                    newTuples.removeTuple( eachTuple.getKey() );
                }
            }
        }

        Iterator tupleIter = newTuples.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            addToAgenda( eachTuple,
                         action,
                         duration );
        }
    }

    void scheduleItem(AgendaItem item)
    {
        Date now = new Date();

        Date then = new Date( now.getTime() + ( item.getDuration() * 1000 ) );

        try
        {
            this.scheduler.addAlarm( then,
                                     new FireListener( item,
                                                       this.workingMemory ) );
        }
        catch (PastDateException e)
        {
            e.printStackTrace();
        }
    }

    void cancelItem(AgendaItem item)
    {

    }
    
    /** Determine if this <code>Agenda</code> has any
     *  scheduled items.
     */
    public boolean isEmpty()
    {
        return this.items.isEmpty();
    }

    /** Fire the next scheduled <code>Agenda</code> item.
     */
    public void fireNextItem() throws ActionInvokationException
    {
        if ( isEmpty() )
        {
            return;
        }

        AgendaItem item = (AgendaItem) this.items.removeFirst();

        item.fire( this.workingMemory );
    }
}

/** Item entry in the <code>Agenda</code>.
 */
class AgendaItem
{
    private ReteTuple tuple;
    private Action    action;
    private long      duration;
    
    AgendaItem(ReteTuple tuple,
               Action action,
               long duration)
    {
        this.tuple    = tuple;
        this.action   = action;
        this.duration = duration;
    }

    boolean dependsOn(Object object)
    {
        return getTuple().dependsOn( object );
    }

    void setTuple(ReteTuple tuple)
    {
        this.tuple = tuple;
    }

    ReteTuple getTuple()
    {
        return this.tuple;
    }
    
    Action getAction()
    {
        return this.action;
    }

    long getDuration()
    {
        return this.duration;
    }

    void fire(WorkingMemory workingMemory) throws ActionInvokationException
    {
        getAction().invoke( getTuple(),
                            workingMemory );
    }
}

class FireListener implements AlarmListener
{
    private AgendaItem    item;
    private WorkingMemory workingMemory;

    FireListener(AgendaItem item,
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
