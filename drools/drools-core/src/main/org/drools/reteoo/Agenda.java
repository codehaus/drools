package org.drools.reteoo;

/*
 $Id: Agenda.java,v 1.24 2003-12-02 23:12:41 bob Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a trademark of 
    The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werken.com/)
 
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
import org.drools.FactHandle;
import org.drools.rule.Rule;
import org.drools.spi.Duration;
import org.drools.spi.ConflictResolver;
import org.drools.spi.ConsequenceException;

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

    /** Conflict resolver. */
    private ConflictResolver conflictResolver;

    /** Items in the agenda. */
    private LinkedList items;

    /** Items time-delayed. */
    private Set scheduledItems;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param workingMemory The <code>WorkingMemory</code> of this agenda.
     *  @param conflictResolver The conflict resolver.
     */
    public Agenda(WorkingMemory workingMemory,
                  ConflictResolver conflictResolver)
    {
        this.workingMemory      = workingMemory;
        this.conflictResolver   = conflictResolver;

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

        Duration dur = rule.getDuration();

        if ( dur != null
             &&
             dur.getDuration( tuple ) > 0 )
        {
            this.scheduledItems.add( item );
            scheduleItem( item );
        }
        else
        {
            this.conflictResolver.insert( item,
                                          this.items );
            //this.items.add( item );
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
        
        AgendaItem item = (AgendaItem) this.items.removeFirst();

        item.fire( this.workingMemory );
    }
}
