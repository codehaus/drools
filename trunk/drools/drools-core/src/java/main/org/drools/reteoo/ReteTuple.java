
package org.drools.reteoo;

import org.drools.spi.Tuple;
import org.drools.spi.Declaration;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** Base Rete-OO {@link Tuple} implementation.
 *
 *  @see Tuple
 *  
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ReteTuple implements Tuple
{
    private Map keyColumns;
    private Map otherColumns;

    
    /** Construct.
     */
    public ReteTuple()
    {
        // this.tuple           = new HashMap();
        this.keyColumns      = new HashMap();
        this.otherColumns    = new HashMap();
    }

    public ReteTuple(ReteTuple that)
    {
        this.keyColumns      = new HashMap( that.keyColumns );
        this.otherColumns    = new HashMap( that.otherColumns );
    }

    public void putKeyColumn(Declaration declaration,
                             Object value)
    {
        this.keyColumns.put( declaration,
                             value );
    }

    public void putAllKeyColumns(Map otherColumns)
    {
        this.keyColumns.putAll( otherColumns );
    }

    public void putOtherColumn(Declaration declaration,
                               Object value)
    {
        this.otherColumns.put( declaration,
                               value );
    }

    public void putAllOtherColumns(Map otherColumns)
    {
        this.otherColumns.putAll( otherColumns );
    }

    public Map getKeyColumns()
    {
        return this.keyColumns;
    }

    public Map getOtherColumns()
    {
        return this.otherColumns;
    }

    public boolean containsRootFactObject(Object object)
    {
        return this.keyColumns.containsValue( object );
    }

    public boolean dependsOn(Object object)
    {
        return this.keyColumns.containsValue( object );
    }

    public boolean hasSameIdentity(ReteTuple that)
    {
        return this.keyColumns.equals( that.keyColumns );
    }

    public Object get(Declaration declaration)
    {
        if ( this.keyColumns.containsKey( declaration ) )
        {
            return this.keyColumns.get( declaration );
        }
        
        return this.otherColumns.get( declaration );
    }

    public Set getDeclarations()
    {
        Set decls = new HashSet( this.keyColumns.size() 
                                 + this.otherColumns.size() );

        decls.addAll( this.keyColumns.keySet() );
        decls.addAll( this.otherColumns.keySet() );

        return decls;
    }

    /** Produce output suitable for debugging.
     */
    public String toString()
    {
        return "[ReteTuple: keys=" + this.keyColumns + "; others=" + this.otherColumns + "]";
    }
}
