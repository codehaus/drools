
package org.drools.reteoo;

import org.drools.WorkingMemory;

import org.drools.spi.Action;
import org.drools.spi.Declaration;
import org.drools.spi.ActionInvokationException;

import java.util.LinkedList;
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
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class Agenda
{
    /** Working memory of this Agenda. */
    private WorkingMemory workingMemory;

    /** Items in the agenda. */
    private LinkedList items;

    /** Construct.
     *
     *  @param workingMemory The <code>WorkingMemory</code> of this agenda.
     */
    public Agenda(WorkingMemory workingMemory)
    {
        this.workingMemory = workingMemory;

        this.items = new LinkedList();
    }

    /** Schedule a rule action invokation on this <code>Agenda</code>.
     *
     *  @param tuple The matching <code>Tuple</code>.
     *  @param action The <code>Action</code> to fire.
     */
    void addToAgenda(ReteTuple tuple,
                     Action action)
    {
        if ( action == null )
        {
            return;
        }

        this.items.add( new AgendaItem( tuple,
                                        action )  );
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
    }

    void modifyAgenda(TupleSet newTuples,
                      Action action)
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
            }
        }
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
    
    AgendaItem(ReteTuple tuple,
               Action action)
    {
        this.tuple   = tuple;
        this.action = action;
    }

    boolean dependsOn(Object object)
    {
        return getTuple().dependsOn( object );
    }

    ReteTuple getTuple()
    {
        return this.tuple;
    }
    
    Action getAction()
    {
        return this.action;
    }

    void fire(WorkingMemory workingMemory) throws ActionInvokationException
    {
        getAction().invoke( getTuple(),
                            workingMemory );
    }
}
