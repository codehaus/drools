package org.drools.reteoo;

/*
 * $Id: Agenda.java,v 1.50 2004-12-06 01:23:02 dbarnett Exp $
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
import org.drools.rule.Rule;
import org.drools.spi.AgendaFilter;
import org.drools.spi.ConflictResolver;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Duration;
import org.drools.spi.Tuple;
import org.drools.util.PriorityQueue;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
class Agenda
    implements
    Serializable
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** Working memory of this Agenda. */
    private final WorkingMemoryImpl workingMemory;

    /** Items in the agenda. */
    private final PriorityQueue items;

    /** Items time-delayed. */
    private final Set scheduledItems;

    /** The current agenda item being fired; or null if none. */
    private AgendaItem item;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param workingMemory
     *            The <code>WorkingMemory</code> of this agenda.
     * @param conflictResolver
     *            The conflict resolver.
     */
    public Agenda(WorkingMemoryImpl workingMemory,
                  ConflictResolver conflictResolver)
    {
        this.workingMemory = workingMemory;
        this.items = new PriorityQueue( conflictResolver );
        this.scheduledItems = new HashSet( );
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Schedule a rule action invokation on this <code>Agenda</code>. Rules
     * specified with noNoop=true that are active should not be added to the
     * agenda
     *
     * @param tuple
     *            The matching <code>Tuple</code>.
     * @param rule
     *            The rule to fire.
     */
    void addToAgenda(ReteTuple tuple,
                     Rule rule)
    {
        // TODO: Remove this defensive rubbish.
        if ( rule == null )
        {
            return;
        }

        /*
         * if no-loop is true for this rule and the current rule is active then
         * do not not re-add to the agenda
         */

        if ( this.item != null && rule.getNoLoop( ) && rule.equals( this.item.getRule( ) ) )
        {
            return;
        }

        AgendaItem item = new AgendaItem( tuple,
                                          rule );

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

        workingMemory.getEventSupport( ).fireActivationCreated( rule,
                                                                tuple );
    }

    /**
     * Remove a tuple from the agenda.
     *
     * @param key
     *            The key to the tuple to be removed.
     * @param rule
     *            The rule to remove.
     */
    void removeFromAgenda(TupleKey key,
                          Rule rule)
    {
        if ( rule == null )
        {
            return;
        }

        AgendaItem eachItem;
        Tuple tuple;
        Iterator itemIter = this.items.iterator( );

        while ( itemIter.hasNext( ) )
        {
            eachItem = (AgendaItem) itemIter.next( );

            if ( eachItem.getRule( ) == rule && eachItem.getKey( ).containsAll( key ) )
            {
                tuple = eachItem.getTuple( );

                itemIter.remove( );

                this.workingMemory.getEventSupport( ).fireActivationCancelled( rule,
                                                                               tuple );
            }
        }

        itemIter = this.scheduledItems.iterator( );

        while ( itemIter.hasNext( ) )
        {
            eachItem = (AgendaItem) itemIter.next( );

            if ( eachItem.getRule( ) == rule && eachItem.getKey( ).containsAll( key ) )
            {
                tuple = eachItem.getTuple( );

                cancelItem( eachItem );

                itemIter.remove( );

                this.workingMemory.getEventSupport( ).fireActivationCancelled( rule,
                                                                               tuple );
            }
        }
    }

    /**
     * Modify the agenda.
     *
     * @param trigger The triggering root object handle.
     * @param modifyTuples New tuples from the modification.
     * @param rule The rule.
     * TODO: Can't simply remove them from the TupleSet!!!
     */
    void modifyAgenda(FactHandle trigger,
                      TupleSet modifyTuples,
                      Rule rule)
    {
        Iterator itemIter = this.items.iterator( );
        AgendaItem eachItem;
        ReteTuple eachTuple;

        // make sure we dont add an existing Tuple onto the Agenda
        while ( itemIter.hasNext( ) )
        {
            eachItem = (AgendaItem) itemIter.next( );

            if ( eachItem.getRule( ) == rule
                 && eachItem.dependsOn( trigger ) )
            {
                modifyTuples.removeTuple( eachItem.getKey( ) );
            }
        }

        // make sure we dont add an existing Tuple onto the Schedule
        itemIter = this.scheduledItems.iterator( );
        while ( itemIter.hasNext( ) )
        {
            eachItem = (AgendaItem) itemIter.next( );

            if ( eachItem.getRule( ) == rule
                 && eachItem.dependsOn( trigger ) )
            {
                modifyTuples.removeTuple( eachItem.getKey( ) );
            }
        }

        // All remaining tuples are new so add them to the Agenda
        Iterator tupleIter = modifyTuples.iterator( );
        while ( tupleIter.hasNext( ) )
        {
            eachTuple = (ReteTuple) tupleIter.next( );

            addToAgenda( eachTuple,
                         rule );
        }
    }

    /**
     * Clears all Activations from the Agenda
     *
     */
    void clearAgenda()
    {
        AgendaItem eachItem;

        //Remove all items in the Agenda and fire a Cancelled event for each
        Iterator iter = this.items.iterator( );
        while ( iter.hasNext( ) )
        {
            eachItem = ( AgendaItem ) iter.next( );

            iter.remove( );

            this.workingMemory.getEventSupport( ).fireActivationCancelled( eachItem.getRule( ),
                                                                           eachItem.getTuple( ) );
        }

        iter = this.scheduledItems.iterator( );

        //Cancel all items in the Schedule and fire a Cancelled event for each
        while ( iter.hasNext( ) )
        {
            eachItem = (AgendaItem) iter.next( );

            cancelItem( eachItem );

            iter.remove( );

            this.workingMemory.getEventSupport( ).fireActivationCancelled( eachItem.getRule( ),
                                                                           eachItem.getTuple( ) );
        }
    }

    /**
     * Schedule an agenda item for delayed firing.
     *
     * @param item
     *            The item to schedule.
     */
    void scheduleItem(AgendaItem item)
    {
        Scheduler.getInstance( ).scheduleAgendaItem( item,
                                                     this.workingMemory );
    }

    /**
     * Cancel a scheduled agenda item for delayed firing.
     *
     * @param item
     *            The item to cancel.
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
     * @throws ConsequenceException
     *             If an error occurs while firing an agenda item.
     */
    public void fireNextItem(AgendaFilter filter) throws ConsequenceException
    {
        if ( isEmpty( ) )
        {
            return;
        }

        item = (AgendaItem) this.items.remove( );

        try
        {
            if ( filter == null || filter.accept( item ) )
            {
                item.fire( this.workingMemory );
            }
        }
        finally
        {
            item = null;
        }
    }
}
