package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.spi.Tuple;
import org.drools.rule.Declaration;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/** Base Rete-OO <code>Tuple</code> implementation.
 *
 *  @see Tuple
 *  
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class ReteTuple
    implements Tuple
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Key colums for this tuple. */
    private TupleKey keyColumns;

    /** Other columns in this tuple. */
    private Map otherColumns;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------
    
    /** Construct.
     */
    public ReteTuple()
    {
        this.keyColumns      = new TupleKey();
        this.otherColumns    = new HashMap();
    }

    /** Copy constructor.
     *
     *  @param that The tuple to copy.
     */
    ReteTuple(ReteTuple that)
    {
        this.keyColumns      = new TupleKey( that.keyColumns );
        this.otherColumns    = new HashMap( that.otherColumns );
    }

    /** Construct a simple 1-column tuple.
     *
     *  @param declaration The column declaration.
     *  @param value The column value.
     */
    ReteTuple(Declaration declaration,
              FactHandle handle,
              Object value)
    {
        this();
        putKeyColumn( declaration,
                      handle,
                      value );
    }

    ReteTuple(Declaration declaration,
              FactHandle handle)
    {
        this();
        putKeyColumn( declaration,
                      handle,
                      null );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------
    
    /** Set a key column's value.
     *
     *  @param declaration The column declaration.
     *  @param value The value.
     */
    public void putKeyColumn(Declaration declaration,
                             FactHandle handle,
                             Object value)
    {
        this.keyColumns.put( declaration,
                             handle,
                             value );
    }

    /** Add all columns from another tuple.
     *
     *  @param that The column source tuple.
     */
    public void putAll(ReteTuple that)
    {
        this.keyColumns.putAll( that.keyColumns );
        this.otherColumns.putAll( that.otherColumns );
    }

    /** Set an other column's value.
     *
     *  @param declaration The column declaration.
     *  @param value The value.
     */
    public void putOtherColumn(Declaration declaration,
                               Object value)
    {
        this.otherColumns.put( declaration,
                               value );
    }

    /** Retrieve the key for this tuple.
     *
     *  @return The key.
     */
    TupleKey getKey()
    {
        return this.keyColumns;
    }

    /** Retrieve the other columns for this tuple.
     *
     *  @return The other columns.
     */
    Map getOtherColumns()
    {
        return this.otherColumns;
    }

    /** Determine if this tuple depends upon
     *  a specified object.
     *
     *  @param handle The object handle to test.
     *
     *  @return <code>true</code> if this tuple depends upon
     *          the specified object, otherwise <code>false</code>.
     */
    boolean dependsOn(FactHandle handle)
    {
        return this.keyColumns.containsRootFactHandle( handle );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.spi.Tuple
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

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

    public FactHandle getFactHandleForObject(Object object)
    {
        return this.keyColumns.getRootFactHandle( object );
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
        return "[ReteTuple: key=" + this.keyColumns + "; others=" + this.otherColumns + "]";
    }
}
