package org.drools.reteoo;

import org.drools.FactHandle;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** A set of <code>Tuple<code>s indexed by <code>TupleKey<code>s.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class TupleSet
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Tuples, indexed by TupleKey. */
    private Map tuples;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    TupleSet()
    {
        this.tuples = new HashMap();
    }

    /** Construct with a single tuple.
     *
     *  @param tuple The tuple.
     */
    TupleSet(ReteTuple tuple)
    {
        this( 1 );
        addTuple( tuple );
    }

    /** Construct with a set of tuples.
     *
     *  @param tuples The tuples.
     */
    TupleSet(Set tuples)
    {
        this.tuples = new HashMap( tuples.size() );

        addAllTuples( tuples );
    }

    /** Construct with a size hint.
     *
     *  @param sizeHint Hint as to desired size.
     */
    TupleSet(int sizeHint)
    {
        this.tuples = new HashMap( sizeHint );
    }

    /** Retrieve the size (number of tuples) in this set.
     *
     *  @return The size of this set.
     */
    public int size()
    {
        return this.tuples.size();
    }

    /** Add a <code>Set</code> of <code>Tuple</code>s to this set.
     *
     *  @param tuples The tuples.
     */
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

    /** Add a <code>TupleSet</code> of <code>Tuple<code>s to this set.
     *
     *  @param tupleSet The tuple set.
     */
    public void addAllTuples(TupleSet tupleSet)
    {
        this.tuples.putAll( tupleSet.tuples );
    }

    /** Add a single <code>Tuple</code> to this set.
     *
     *  @param tuple The tuple.
     */
    public void addTuple(ReteTuple tuple)
    {
        this.tuples.put( tuple.getKey(),
                         tuple );
    }

    /** Remove a tuple from this set.
     * 
     *  @param key Key matching the tuple.
     */
    public void removeTuple(TupleKey key)
    {
        this.tuples.remove( key );
    }

    /** Remove several tuples matching a subsset key.
     *
     *  @param key The partial key to match.
     */
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

    /** Retrieve all <code>Tuple</code>s.
     *
     *  @see org.drools.spi.Tuple
     *
     *  @return The set of tuples.
     */
    public Set getTuples()
    {
        return new HashSet( this.tuples.values() );
    }

    /** Retrieve an iterator over the tuples.
     *
     *  @return The iterator.
     */
    public Iterator iterator()
    {
        return new Itr( this.tuples );
    }

    /** Retrieve all tuples related to a specified
     *  root fact object.
     *
     *  @param handle The root fact object handle.
     *
     *  @return Matching tuples.
     */
    public Set getTuples(FactHandle handle)
    {
        Set matchingTuples = new HashSet();

        Iterator keyIter = getKeys().iterator();
        TupleKey eachKey = null;

        while ( keyIter.hasNext() )
        {
            eachKey = (TupleKey) keyIter.next();

            if ( eachKey.containsRootFactHandle( handle ) )
            {
                matchingTuples.add( getTuple( eachKey ) );
            }
        }
        
        return matchingTuples;
    }

    /** Retriave all <code>TupleKey<code>s.
     *
     *  @see TupleKey
     *
     *  @return The set of tuple keys.
     */
    public Set getKeys()
    {
        return this.tuples.keySet();
    }

    /** Retrieve a <code>Tuple</code> by <code>TupleKey</code>.
     *
     *  @see org.drools.spi.Tuple
     *  @see #containsTuple
     *
     *  @param key The tuple key.
     *
     *  @return The matching tuple or <code>null</code> if this
     *          set contains no matching tuple.
     */
    public ReteTuple getTuple(TupleKey key)
    {
        return (ReteTuple) this.tuples.get( key );
    }

    /** Determine if this set contains a <code>Tuple</code> matching
     *  the specified <code>TupleKey</code>.
     *
     *  @param key The tuple key.
     *
     *  @return <code>true</code> if a matching tuple exists within
     *          this set, otherwise <code>false<code>.
     */
    public boolean containsTuple(TupleKey key)
    {
        return this.tuples.containsKey( key );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Produce a debug string.
     *
     *  @return The debug string.
     */
    public String toString()
    {
        return "[TupleSet tuple=" + this.tuples + "]";
    }

}

/** Iterator over tuples.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class Itr implements Iterator
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Tuples. */
    private Map tuples;

    /** Internal iterator. */
    private Iterator keyIter;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param tuples The tuples.
     */
    Itr(Map tuples)
    {
        this.tuples = tuples;
        this.keyIter = tuples.keySet().iterator();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.util.Iterator
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Determine if this iterator has an element in the next position.
     *
     *  @return <code>true</code> if an element exists in the next
     *          position, otherwise <code>false</code>.
     */
    public boolean hasNext()
    {
        return this.keyIter.hasNext();
    }

    /** Retrieve the element in the next position.
     *
     *  @return The element in the next position.
     *
     *  @throws NoSuchElementException If there exists no element
     *          in the next position.
     */
    public Object next() throws NoSuchElementException
    {
        return this.tuples.get( this.keyIter.next() );
    }

    /** Remove the element at the current position.
     */
    public void remove()
    {
        this.keyIter.remove();
    }

    
}
