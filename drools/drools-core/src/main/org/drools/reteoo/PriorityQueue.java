
package org.drools.reteoo;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class PriorityQueue extends LinkedList
{
    public PriorityQueue()
    {

    }

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

    public ListIterator listIterator()
    {
        return new ListIter( super.listIterator() );
    }

    public Object removeFirst() throws NoSuchElementException
    {
        Entry entry = (Entry) super.removeFirst();

        return entry.getItem();
    }
}
    
class ListIter implements ListIterator
{
    private ListIterator iter;
    
    public ListIter(ListIterator iter)
    {
        this.iter = iter;
    }
    
    public boolean hasNext()
    {
        return this.iter.hasNext();
    }
    
    public boolean hasPrevious()
    {
        return this.iter.hasPrevious();
    }
    
    public Object next() throws NoSuchElementException
    {
        Entry entry = (Entry) this.iter.next();
        
        return entry.getItem();
    }

    public Object previous() throws NoSuchElementException
    {
        Entry entry = (Entry) this.iter.previous();

        return entry.getItem();
    }

    public void remove() throws IllegalStateException
    {
        this.iter.remove();
    }

    public void add(Object item) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    public void set(Object item) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    public int nextIndex()
    {
        return this.iter.nextIndex();
    }

    public int previousIndex()
    {
        return this.iter.previousIndex();
    }
}

class Entry
{
    private int priority;
    private Object item;
    
    public Entry(Object item,
                 int priority)
    {
        this.item     = item;
        this.priority = priority;
    }
    
    public int getPriority()
    {
        return this.priority;
    }
    
    public Object getItem()
    {
        return this.item;
    }
}
