package org.drools.reteoo;

/*
 $Id: PriorityQueue.java,v 1.2 2002-07-27 02:37:12 bob Exp $

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

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/** Queue that maintains the entries in sorted order.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class PriorityQueue extends LinkedList
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public PriorityQueue()
    {
        // intentionally left blank.
    }

    /** Add an item to this queue.
     *
     *  @param item The item to add.
     *  @param priority The priority of the item.
     */
    public void add(Object item,
                    int priority)
    {
        ListIterator entryIter = super.listIterator();
        Entry        eachEntry = null;

        while ( entryIter.hasNext() )
        {
            eachEntry = (Entry) entryIter.next();

            if ( eachEntry.getPriority() > priority )
            {
                entryIter.previous();
                entryIter.add( new Entry( item,
                                          priority ) );
                
                return;
            }
        }
        
        super.add( new Entry( item,
                              priority ) );
    }

    /** Retrieve a bidrectional iterator of the
     *  members of this queue.
     *
     *  @return The bidirectional list iterator.
     */
    public ListIterator listIterator()
    {
        return new ListIter( super.listIterator() );
    }

    /** Remove the first element from this queue.
     *
     *  @return The removed element.
     *
     *  @throws NoSuchElementException If this queue is empty.
     */
    public Object removeFirst() throws NoSuchElementException
    {
        Entry entry = (Entry) super.removeFirst();

        return entry.getItem();
    }
}
    
/** Bidirectional iterator implementation.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class ListIter implements ListIterator
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Internal iterator. */
    private ListIterator iter;
    
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param iter The internal iterator.
     */
    ListIter(ListIterator iter)
    {
        this.iter = iter;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Determine if this iterator has an element
     *  in the next position.
     *
     *  @see #next
     *
     *  @return <code>true</code> if this iterator contains
     *          an element in the next position,
     *          otherwise <code>false</code>.
     */
    public boolean hasNext()
    {
        return this.iter.hasNext();
    }
    
    /** Determine if this iterator has an element
     *  in the previous position.
     *
     *  @see #previous
     *
     *  @return <code>true</code> if this iterator contains
     *          an element in the previous position,
     *          otherwise <code>false</code>.
     */
    public boolean hasPrevious()
    {
        return this.iter.hasPrevious();
    }
    
    /** Retrieve the element in the next position and
     *  advance this iterator.
     *
     *  @return The element at the next position.
     *
     *  @throws NoSuchElementException If there is no
     *          element in the next position.
     */
    public Object next() throws NoSuchElementException
    {
        Entry entry = (Entry) this.iter.next();
        
        return entry.getItem();
    }

    /** Retrieve the element in the previous position and
     *  retreat this iterator.
     *
     *  @return The element at the previous position.
     *
     *  @throws NoSuchElementException If there is no
     *          element in the previous position.
     */
    public Object previous() throws NoSuchElementException
    {
        Entry entry = (Entry) this.iter.previous();

        return entry.getItem();
    }

    /** Remove the element currently pointed to.
     *
     *  @throws IllegalStateException If this iterator
     *          does not currently point to any element.
     */
    public void remove() throws IllegalStateException
    {
        this.iter.remove();
    }

    /** Add an element after the current point.
     *
     *  @param item The item to add.
     *
     *  @throws UnsupportedOperationException By default.
     */
    public void add(Object item) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    /** Set the element at the current point.
     *
     *  @param item The item to set.
     *
     *  @throws UnsupportedOperationException By default.
     */
    public void set(Object item) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    /** Retrieve the index of the next element position.
     *
     *  @return the index.
     */
    public int nextIndex()
    {
        return this.iter.nextIndex();
    }

    /** Retrieve the index of the previous element position.
     *
     *  @return the index.
     */
    public int previousIndex()
    {
        return this.iter.previousIndex();
    }
}

/** Entry in the priority queue.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class Entry
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Priority. */
    private int priority;

    /** Item, itself. */
    private Object item;
    
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param item The item.
     *  @param priority The priority.
     */
    Entry(Object item,
    int priority)
    {
        this.item     = item;
        this.priority = priority;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------
    
    /** Retrieve the priority.
     *
     *  @return The priority.
     */
    public int getPriority()
    {
        return this.priority;
    }
    
    /** Retrieve the item.
     *
     *  @return The item.
     */
    public Object getItem()
    {
        return this.item;
    }
}
