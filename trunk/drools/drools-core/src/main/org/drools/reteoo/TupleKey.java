
package org.drools.reteoo;

import org.drools.spi.Declaration;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

class TupleKey
{
    private Map columns;
    private Set rootFactObjects;

    public TupleKey()
    {
        this.columns         = new HashMap();
        this.rootFactObjects = new HashSet();
    }

    public TupleKey(Declaration declaration,
                    Object value)
    {
        this();

        put( declaration,
             value );
    }

    public TupleKey(TupleKey that)
    {
        this();

        this.columns.putAll( that.columns );
        this.rootFactObjects.addAll( that.rootFactObjects );
    }

    public void putAll(TupleKey key)
    {
        this.columns.putAll( key.columns );
        this.rootFactObjects.addAll( key.rootFactObjects );
    }

    public void put(Declaration declaration,
                    Object value)
    {
        this.columns.put( declaration,
                          value );

        this.rootFactObjects.add( value );
    }

    public Object get(Declaration declaration)
    {
        return this.columns.get( declaration );
    }

    public boolean containsDeclaration(Declaration declaration)
    {
        return this.columns.containsKey( declaration );
    }

    public boolean containsRootFactObject(Object object)
    {
        return this.rootFactObjects.contains( object );
    }

    public int size()
    {
        return this.columns.size();
    }

    public Set getDeclarations()
    {
        return this.columns.keySet();
    }

    public boolean containsAll(TupleKey that)
    {
        Iterator    declIter = that.columns.keySet().iterator(); 
        Declaration eachDecl = null;

        Object thisValue = null;
        Object thatValue = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            thisValue = this.get( eachDecl );
            thatValue = that.get( eachDecl );

            if ( thisValue == null
                 && 
                 thatValue == null )
            {
                continue;
            }
            else if (thisValue == null
                     ||
                     thatValue == null )
            {
                return false;
            }
            else if ( ! thisValue.equals( thatValue ) )
            {
                return false;
            }
        }

        return true;
    }

    public boolean equals(Object thatObj)
    {
        TupleKey that = (TupleKey) thatObj;

        return this.columns.equals( that.columns );
    }

    public int hashCode()
    {
        return this.columns.hashCode();
    }

    public String toString()
    {
        return "[TupleKey: columns=" + this.columns + "]";
    }
}
