package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.FactHandle;
import org.drools.rule.Rule;
import org.drools.spi.ConsequenceException;
import org.drools.spi.ConflictResolutionStrategy;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Iterator;

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
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class Agenda
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Working memory of this Agenda. */
    private WorkingMemory workingMemory;

    /** Conflict resolution strategy. */
    private ConflictResolutionStrategy conflictResolutionStrategy;

    /** Conflict resolution strategy comparator. */
    private ConflictResolutionComparator conflictResolutionComparator;

    /** Items in the agenda. */
    private LinkedList items;

    /** Items time-delayed. */
    private Set scheduledItems;

    private boolean needsSort;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param workingMemory The <code>WorkingMemory</code> of this agenda.
     */
    public Agenda(WorkingMemory workingMemory,
                  ConflictResolutionStrategy conflictResolutionStrategy)
    {
        this.workingMemory                = workingMemory;
        this.conflictResolutionStrategy   = conflictResolutionStrategy;
        this.conflictResolutionComparator = new ConflictResolutionComparator( conflictResolutionStrategy );

        this.items          = new LinkedList();
        this.scheduledItems = new HashSet();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Schedule a rule action invokation on this <code>Agenda</code>.
     *
     *  @param tuple The matching <code>Tuple</code>.
     *  @param rule The rule to fire.
     */
    void addToAgenda(ReteTuple tuple,
                     Rule rule)
    {
        if ( rule == null )
        {
            return;
        }

        AgendaItem item = new AgendaItem( tuple,
                                          rule );
        if ( rule.getDuration( tuple ) > 0 )
        {
            this.scheduledItems.add( item );
            scheduleItem( item );
        }
        else
        {
            this.items.add( item );
            this.needsSort = true;
        }
    }

    /** Remove a tuple from the agenda.
     *
     *  @param key The key to the tuple to be removed.
     *  @param rule The rule to remove.
     */
    void removeFromAgenda(TupleKey key,
                          Rule rule)
    {
        if ( rule == null )
        {
            return;
        }

        Iterator   itemIter = this.items.iterator();
        AgendaItem eachItem = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.getRule() == rule )
            {
                if ( eachItem.getKey().containsAll( key ) )
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

            if ( eachItem.getRule() == rule )
            {
                if ( eachItem.getKey().containsAll( key ) )
                {
                    cancelItem( eachItem );
                    itemIter.remove();
                }
            }
        }
    }

    /** Modify the agenda.
     *
     *  @param trigger The triggering root object handle.
     *  @param newTuples New tuples from the modification.
     *  @param rule The rule.
     */
    void modifyAgenda(FactHandle trigger,
                      TupleSet newTuples,
                      Rule rule)
    {
        Iterator   itemIter  = this.items.iterator();
        AgendaItem eachItem  = null;
        ReteTuple  eachTuple = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.getRule() == rule )
            {
                if ( eachItem.dependsOn( trigger ) )
                {
                    if ( ! newTuples.containsTuple( eachItem.getKey() ) )
                    {
                        itemIter.remove();
                    }
                    else
                    {
                        eachItem.setTuple( newTuples.getTuple( eachItem.getKey() ) );
                        newTuples.removeTuple( eachItem.getKey() );
                    }
                }
            }
        }

        itemIter = this.scheduledItems.iterator();
        eachItem = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.getRule() == rule )
            {
                if ( eachItem.dependsOn( trigger ) )
                {
                    if ( ! newTuples.containsTuple( eachItem.getKey() ) )
                    {
                        cancelItem( eachItem );
                        itemIter.remove();
                    }

                    else
                    {
                        eachItem.setTuple( newTuples.getTuple( eachItem.getKey() ) );
                        newTuples.removeTuple( eachItem.getKey() );
                    }
                }
            }
        }

        Iterator tupleIter = newTuples.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            addToAgenda( eachTuple,
                         rule );
        }
    }

    /** Schedule an agenda item for delayed firing.
     *
     *  @param item The item to schedule.
     */
    void scheduleItem(AgendaItem item)
    {
        Scheduler.getInstance().scheduleAgendaItem( item,
                                                    this.workingMemory );
    }

    /** Cancel a scheduled agenda item for delayed firing.
     *
     *  @param item The item to cancel.
     */
    void cancelItem(AgendaItem item)
    {
        Scheduler.getInstance().cancelAgendaItem( item );
    }
    
    /** Determine if this <code>Agenda</code> has any
     *  scheduled items.
     *
     *  @return <code>true<code> if the agenda is empty, otherwise
     *          <code>false</code>.
     */
    public boolean isEmpty()
    {
        return this.items.isEmpty();
    }

    /** Fire the next scheduled <code>Agenda</code> item.
     *
     *  @throws ConsequenceException If an error occurs while
     *          firing an agenda item.
     */
    public void fireNextItem() throws ConsequenceException
    {

        if ( isEmpty() )
        {
            return;
        }

        if ( this.needsSort )
        {
            Collections.sort( this.items,
                              this.conflictResolutionComparator );

            this.needsSort = false;
        }

        AgendaItem item = (AgendaItem) this.items.removeFirst();

        item.fire( this.workingMemory );
    }
}
