
package org.drools.reteoo;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

class TupleSet
{
    private Map tuples;

    TupleSet(ReteTuple tuple)
    {
        this( 1 );
        addTuple( tuple );
    }

    TupleSet()
    {
        this.tuples = new HashMap();
    }

    TupleSet(Set tuples)
    {
        this.tuples = new HashMap( tuples.size() );

        addAllTuples( tuples );
    }

    TupleSet(int sizeHint)
    {
        this.tuples = new HashMap( sizeHint );
    }

    public String toString()
    {
        return "[TupleSet tuple=" + this.tuples + "]";
    }

    public int size()
    {
        return this.tuples.size();
    }

    public void addAllTuples(Set tuples)
    {
        Iterator tupleIter = tuples.iterator();
        ReteTuple eachTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            addTuple( eachTuple );
        }
    }

    public void addAllTuples(TupleSet tupleSet)
    {
        this.tuples.putAll( tupleSet.tuples );
    }

    public void addTuple(ReteTuple tuple)
    {
        this.tuples.put( tuple.getKey(),
                         tuple );
    }

    public void removeTuple(TupleKey key)
    {
        this.tuples.remove( key );
    }

    public void removeTuplesByPartialKey(TupleKey key)
    {
        Iterator  tupleIter = iterator();
        ReteTuple eachTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            if ( eachTuple.getKey().containsAll( key ) )
            {
                tupleIter.remove();
            }
        }
    }

    public Set getTuples()
    {
        return new HashSet( this.tuples.values() );
    }

    public Iterator iterator()
    {
        return new Itr( this.tuples );
    }

    public Set getTuples(Object rootFactObject)
    {
        Set matchingTuples = new HashSet();

        Iterator keyIter = getKeys().iterator();
        TupleKey eachKey = null;

        while ( keyIter.hasNext() )
        {
            eachKey = (TupleKey) keyIter.next();

            if ( eachKey.containsRootFactObject( rootFactObject ) )
            {
                matchingTuples.add( getTuple( eachKey ) );
            }
        }
        
        return matchingTuples;
    }

    public Set getKeys()
    {
        return this.tuples.keySet();
    }

    public ReteTuple getTuple(TupleKey key)
    {
        return (ReteTuple) this.tuples.get( key );
    }

    public boolean containsTuple(TupleKey key)
    {
        return this.tuples.containsKey( key );
    }
}

class Itr implements Iterator
{
    private Map      columns;
    private Iterator keyIter;

    Itr(Map columns)
    {
        this.columns = columns;
        this.keyIter = columns.keySet().iterator();
    }

    public boolean hasNext()
    {
        return this.keyIter.hasNext();
    }

    public Object next() throws NoSuchElementException
    {
        return this.columns.get( this.keyIter.next() );
    }

    public void remove()
    {
        this.keyIter.remove();
    }

    
}
