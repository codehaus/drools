
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
    private TupleKey keyColumns;
    private Map      otherColumns;

    
    /** Construct.
     */
    public ReteTuple()
    {
        // this.tuple           = new HashMap();
        this.keyColumns      = new TupleKey();
        this.otherColumns    = new HashMap();
    }

    public ReteTuple(ReteTuple that)
    {
        this.keyColumns      = new TupleKey( that.keyColumns );
        this.otherColumns    = new HashMap( that.otherColumns );
    }

    public ReteTuple(Declaration declaration,
                     Object value)
    {
        this();
        putKeyColumn( declaration,
                      value );
    }

    public void putKeyColumn(Declaration declaration,
                             Object value)
    {
        this.keyColumns.put( declaration,
                             value );
    }

    public void putAll(ReteTuple that)
    {
        this.keyColumns.putAll( that.keyColumns );
        this.otherColumns.putAll( that.otherColumns );
    }

    public void putOtherColumn(Declaration declaration,
                               Object value)
    {
        this.otherColumns.put( declaration,
                               value );
    }

    public TupleKey getKey()
    {
        return this.keyColumns;
    }

    public Map getOtherColumns()
    {
        return this.otherColumns;
    }

    public boolean dependsOn(Object object)
    {
        return this.keyColumns.containsRootFactObject( object );
    }

    /** Retrieve the value of a particular declaration column.
     *
     *  @param declaration The declaration.
     *
     *  @return The value.
     */
    public Object get(Declaration declaration)
    {
        if ( this.keyColumns.containsDeclaration( declaration ) )
        {
            return this.keyColumns.get( declaration );
        }
        
        return this.otherColumns.get( declaration );
    }

    /** Retrieve all declarations participating in this tuple.
     *
     *  @return Set of all declarations.
     */
    public Set getDeclarations()
    {
        Set decls = new HashSet( this.keyColumns.size() 
                                 + this.otherColumns.size() );

        decls.addAll( this.keyColumns.getDeclarations() );
        decls.addAll( this.otherColumns.keySet() );

        return decls;
    }

    /** Produce output suitable for debugging.
     */
    public String toString()
    {
        return "[ReteTuple: key=" + this.keyColumns + "; others=" + this.otherColumns + "]";
    }
}
