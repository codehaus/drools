
package org.drools.reteoo;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

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

    TupleSet(int sizeHint)
    {
        this.tuples = new HashMap( sizeHint );
    }

    public void addTuple(ReteTuple tuple)
    {
        this.tuples.put( tuple.getKeyColumns(),
                         tuple );
    }

    public Set getTuples()
    {
        return new HashSet( this.tuples.values() );
    }

    public Set getTuples(Object rootFactObject)
    {
        Set matchingTuples = new HashSet();

        Iterator keyIter = getKeys().iterator();
        Map      eachKey = null;

        while ( keyIter.hasNext() )
        {
            eachKey = (Map) keyIter.next();

            if ( eachKey.containsValue( rootFactObject ) )
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

    public ReteTuple getTuple(Map key)
    {
        return (ReteTuple) this.tuples.get( key );
    }
}
