package org.drools.reteoo.impl;

/*
 $Id: AgendaImpl.java,v 1.1 2002-07-28 13:55:47 bob Exp $

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

import org.drools.reteoo.Agenda;
import org.drools.spi.Rule;
import org.drools.spi.ActionInvokationException;

import java.util.Set;
import java.util.HashSet;
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
public class AgendaImpl implements Agenda
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Working memory of this Agenda. */
    private WorkingMemory workingMemory;

    /** Items in the agenda. */
    private PriorityQueue items;

    /** Items time-delayed. */
    private Set scheduledItems;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param workingMemory The <code>WorkingMemory</code> of this agenda.
     */
    public AgendaImpl(WorkingMemory workingMemory)
    {
        this.workingMemory = workingMemory;

        this.items          = new PriorityQueue();
        this.scheduledItems = new HashSet();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Schedule a rule action invokation on this <code>Agenda</code>.
     *
     *  @param tuple The matching <code>Tuple</code>.
     *  @param rule The rule to fire.
     *  @param priority The rule-firing priority.
     */
    void addToAgenda(ReteTuple tuple,
                     Rule rule,
                     int priority)
    {
        if ( rule == null )
        {
            return;
        }

        AgendaItem item = new AgendaItem( tuple,
                                          rule );
        if ( rule.getDuration() > 0 )
        {
            this.scheduledItems.add( item );
            scheduleItem( item );
        }
        else
        {
            this.items.add( item,
                            priority );
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

            if ( eachItem.getRule() == rule )
            {
                if ( eachItem.getTuple().getKey().containsAll( key ) )
                {
                    cancelItem( eachItem );
                    itemIter.remove();
                }
            }
        }
    }

    /** Modify the agenda.
     *
     *  @param trigger The triggering root object.
     *  @param newTuples New tuples from the modification.
     *  @param rule The rule.
     *  @param priority Firing priority.
     */
    void modifyAgenda(Object trigger,
                      TupleSet newTuples,
                      Rule rule,
                      int priority)
    {
        Iterator   itemIter  = this.items.iterator();
        AgendaItem eachItem  = null;
        ReteTuple  eachTuple = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.getRule() == rule )
            {
                eachTuple = eachItem.getTuple();

                if ( eachTuple.dependsOn( trigger ) )
                {
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
        }

        itemIter = this.scheduledItems.iterator();
        eachItem = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.getRule() == rule )
            {
                eachTuple = eachItem.getTuple();

                if ( eachTuple.dependsOn( trigger ) )
                {
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
        }

        Iterator tupleIter = newTuples.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            addToAgenda( eachTuple,
                         rule,
                         priority );
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
     *  @throws ActionInvokationException If an error occurs while
     *          firing an agenda item.
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
