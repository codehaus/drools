package org.drools.reteoo;

/*
 * $Id: Agenda.java,v 1.36 2004-11-06 03:29:24 mproctor Exp $
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

import org.drools.FactHandle;
import org.drools.PriorityQueue;
import org.drools.WorkingMemory;
import org.drools.rule.Rule;
import org.drools.spi.ConflictResolver;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Consequence;
import org.drools.spi.Duration;
import org.drools.spi.Tuple;

import org.drools.event.WorkingMemoryEventListener;
import org.drools.event.ActivationCreatedEvent;
import org.drools.event.ActivationCancelledEvent;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

/**
 * Rule-firing Agenda.
 *
 * <p>
 * Since many rules may be matched by a single assertObject(...) all scheduled
 * actions are placed into the <code>Agenda</code>.
 * </p>
 *
 * <p>
 * While processing a scheduled action, it may modify or retract objects in
 * other scheduled actions, which must then be removed from the agenda.
 * Non-invalidated actions are left on the agenda, and are executed in turn.
 * </p>
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris </a>
 */
class Agenda implements Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Working memory of this Agenda. */
    private final WorkingMemory workingMemory;

    /** Items in the agenda. */
    private final PriorityQueue items;

    /** Items time-delayed. */
    private final Set           scheduledItems;

    /** The current agenda item being fired; or null if none. */
    private AgendaItem          item;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param workingMemory The <code>WorkingMemory</code> of this agenda.
     * @param conflictResolver The conflict resolver.
     */
    public Agenda(WorkingMemory workingMemory, ConflictResolver conflictResolver)
    {
        this.workingMemory = workingMemory;
        this.items = new PriorityQueue( conflictResolver );
        this.scheduledItems = new HashSet( );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /**
     * Schedule a rule action invokation on this <code>Agenda</code>. Rules
     * specified with noNoop=true that are active should not be added to the
     * agenda
     *
     * @param tuple The matching <code>Tuple</code>.
     * @param rule The rule to fire.
     */
    void addToAgenda(ReteTuple tuple, Rule rule)
    {
        if ( rule == null )
        {
            return;
        }

        /*
         * if no-loop is true for this rule and the current rule is active then
         * do not not re-add to the agenda
         */

        if ( this.item != null && rule.getNoLoop( )
             && rule.equals( this.item.getRule( ) ) )
        {
            return;
        }

        AgendaItem item = new AgendaItem( tuple, rule );

        Duration dur = rule.getDuration( );

        if ( dur != null && dur.getDuration( tuple ) > 0 )
        {
            this.scheduledItems.add( item );
            scheduleItem( item );
        }
        else
        {
            this.items.add( item );
        }

        List listeners = workingMemory.getListeners();
        if (!listeners.isEmpty())
        {
            ActivationCreatedEvent activationCreatedEvent =  new ActivationCreatedEvent(workingMemory,
                                                                                        tuple.getRule( ).getConsequence( ),
                                                                                        tuple);
            Iterator iter = listeners.iterator();
            WorkingMemoryEventListener listener;
            while ( iter.hasNext() )
            {
                listener = (WorkingMemoryEventListener) iter.next();
                listener.activationCreated(activationCreatedEvent);
            }
        }
    }

    /**
     * Remove a tuple from the agenda.
     *
     * @param key The key to the tuple to be removed.
     * @param rule The rule to remove.
     */
    void removeFromAgenda(TupleKey key, Rule rule)
    {
        if ( rule == null )
        {
            return;
        }

        Iterator itemIter = this.items.iterator( );
        AgendaItem eachItem;
        Tuple tuple;
        ActivationCancelledEvent activationCancelledEvent;
        List listeners = workingMemory.getListeners();
        WorkingMemoryEventListener listener;
        Iterator iter;
        while ( itemIter.hasNext( ) )
        {
            eachItem = ( AgendaItem ) itemIter.next( );

            if ( eachItem.getRule( ) == rule )
            {
                if ( eachItem.getKey( ).containsAll( key ) )
                {
                    itemIter.remove( );
                    if (!listeners.isEmpty())
                    {
                        tuple = eachItem.getTuple();
                        activationCancelledEvent =  new ActivationCancelledEvent(workingMemory,
                                                                                                          tuple.getRule( ).getConsequence( ),
                                                                                                          tuple);

                        iter = workingMemory.getListeners().iterator();
                        while ( iter.hasNext() )
                        {
                            listener = (WorkingMemoryEventListener) iter.next();
                            listener.activationCancelled(activationCancelledEvent);
                        }
                    }
                }
            }
        }

        itemIter = this.scheduledItems.iterator( );

        while ( itemIter.hasNext( ) )
        {
            eachItem = ( AgendaItem ) itemIter.next( );

            if ( eachItem.getRule( ) == rule )
            {
                if ( eachItem.getKey( ).containsAll( key ) )
                {
                    cancelItem( eachItem );
                    itemIter.remove( );
                    if (!listeners.isEmpty())
                    {
                        tuple = eachItem.getTuple();
                        activationCancelledEvent =  new ActivationCancelledEvent(workingMemory,
                                                                                                          tuple.getRule( ).getConsequence( ),
                                                                                                          tuple);

                        iter = workingMemory.getListeners().iterator();
                        while ( iter.hasNext() )
                        {
                            listener = (WorkingMemoryEventListener) iter.next();
                            listener.activationCancelled(activationCancelledEvent);
                        }
                    }
                }
            }
        }
    }

    /**
     * Modify the agenda.
     *
     * @param trigger The triggering root object handle.
     * @param newTuples New tuples from the modification.
     * @param rule The rule.
     */
    void modifyAgenda(FactHandle trigger, TupleSet newTuples, Rule rule)
    {
        Iterator itemIter = this.items.iterator( );
        AgendaItem eachItem;
        ReteTuple eachTuple;
        Tuple tuple;
        List listeners = workingMemory.getListeners();
        WorkingMemoryEventListener listener;
        ActivationCancelledEvent activationCancelledEvent;
        Iterator iter;
        while ( itemIter.hasNext( ) )
        {
            eachItem = ( AgendaItem ) itemIter.next( );

            if ( eachItem.getRule( ) == rule )
            {
                if ( eachItem.dependsOn( trigger ) )
                {
                    if ( !newTuples.containsTuple( eachItem.getKey( ) ) )
                    {
                        itemIter.remove();

                        if (!listeners.isEmpty())
                        {
                            tuple = eachItem.getTuple();
                            activationCancelledEvent =  new ActivationCancelledEvent(workingMemory,
                                                                                                              tuple.getRule( ).getConsequence( ),
                                                                                                              tuple);

                            iter = workingMemory.getListeners().iterator();
                            while ( iter.hasNext() )
                            {
                                listener = (WorkingMemoryEventListener) iter.next();
                                listener.activationCancelled(activationCancelledEvent);
                            }
                        }
                    }
                    else
                    {
                        eachItem
                                .setTuple( newTuples
                                                    .getTuple( eachItem
                                                                       .getKey( ) ) );
                        newTuples.removeTuple( eachItem.getKey( ) );
                    }
                }
            }
        }

        itemIter = this.scheduledItems.iterator( );

        while ( itemIter.hasNext( ) )
        {
            eachItem = ( AgendaItem ) itemIter.next( );

            if ( eachItem.getRule( ) == rule )
            {
                if ( eachItem.dependsOn( trigger ) )
                {
                    if ( !newTuples.containsTuple( eachItem.getKey( ) ) )
                    {
                        cancelItem( eachItem );
                        itemIter.remove( );
                        if (!listeners.isEmpty())
                        {
                            tuple = eachItem.getTuple();
                            activationCancelledEvent =  new ActivationCancelledEvent(workingMemory,
                                                                                                              tuple.getRule( ).getConsequence( ),
                                                                                                              tuple);

                            iter = workingMemory.getListeners().iterator();
                            while ( iter.hasNext() )
                            {
                                listener = (WorkingMemoryEventListener) iter.next();
                                listener.activationCancelled(activationCancelledEvent);
                            }
                        }
                    }

                    else
                    {
                        eachItem
                                .setTuple( newTuples
                                                    .getTuple( eachItem
                                                                       .getKey( ) ) );
                        newTuples.removeTuple( eachItem.getKey( ) );
                    }
                }
            }
        }

        Iterator tupleIter = newTuples.iterator( );

        while ( tupleIter.hasNext( ) )
        {
            eachTuple = ( ReteTuple ) tupleIter.next( );

            addToAgenda( eachTuple, rule );
        }
    }

    /**
     * Schedule an agenda item for delayed firing.
     *
     * @param item The item to schedule.
     */
    void scheduleItem(AgendaItem item)
    {
        Scheduler.getInstance( ).scheduleAgendaItem( item, this.workingMemory );
    }

    /**
     * Cancel a scheduled agenda item for delayed firing.
     *
     * @param item The item to cancel.
     */
    void cancelItem(AgendaItem item)
    {
        Scheduler.getInstance( ).cancelAgendaItem( item );
    }

    /**
     * Determine if this <code>Agenda</code> has any scheduled items.
     *
     * @return <code>true<code> if the agenda is empty, otherwise
     *          <code>false</code>.
     */
    public boolean isEmpty()
    {
        return this.items.isEmpty( );
    }

    public int size()
    {
        return items.size( );
    }

    /**
     * Fire the next scheduled <code>Agenda</code> item.
     *
     * @throws ConsequenceException If an error occurs while firing an agenda
     *         item.
     */
    public void fireNextItem() throws ConsequenceException
    {
        if ( isEmpty( ) )
        {
            return;
        }

        item = ( AgendaItem ) this.items.remove( );

        try
        {
            item.fire( this.workingMemory );
        }
        finally
        {
            item = null;
        }
    }
}