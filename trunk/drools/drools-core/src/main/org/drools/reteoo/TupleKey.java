package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.rule.Declaration;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

/** A composite key to match tuples.
 *
 *  @see Tuple
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class TupleKey
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Columns. */
    private Map columns;

    /** Root fact object handles. */
    private Map rootFactHandles;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public TupleKey()
    {
        this.columns         = new HashMap();
        this.rootFactHandles = new HashMap();
    }

    public TupleKey(FactHandle handle)
    {
        this();
        this.rootFactHandles.put( new Object(),
                                  handle );
    }

    /** Copy constructor.
     *
     *  @param that The tuple key to copy.
     */
    public TupleKey(TupleKey that)
    {
        this();
        putAll( that );
    }

    // ------------------------------------------------------------
    //
    // ------------------------------------------------------------

    void addRootFactHandle(FactHandle handle)
    {
        this.rootFactHandles.put( new Object(),
                                  handle );
    }

    /** Put all values from another key into this key.
     *
     *  @param key The source value key.
     */
    public void putAll(TupleKey key)
    {
        this.columns.putAll( key.columns );
        this.rootFactHandles.putAll( key.rootFactHandles );
    }

    /** Put a value for a declaration.
     *
     *  @param declaration Column declaration.
     *  @param value The value.
     */
    public void put(Declaration declaration,
                    FactHandle handle,
                    Object value)
    {
        this.columns.put( declaration,
                          value );

        this.rootFactHandles.put( value,
                                  handle );
    }

    /** Retrieve a value by declaration.
     *
     *  @param declaration The declaration.
     *
     *  @return The value.
     */
    public Object get(Declaration declaration)
    {
        return this.columns.get( declaration );
    }

    /** Determine if this key contains the specified declaration.
     *
     *  @param declaration The declaration to test.
     *
     *  @return <code>true</code> if this key contains a column
     *          for the specified declaration, otherwise
     *          <code>false</code>.
     */
    public boolean containsDeclaration(Declaration declaration)
    {
        return this.columns.containsKey( declaration );
    }

    /** Determine if this key contains the specified root fact object.
     *
     *  @param object The object to test.
     *
     *  @return <code>true</code> if this key contains the 
     *          specified root fact object, otherwise
     *          <code>false</code>.
     */
    public boolean containsRootFactHandle(FactHandle handle)
    {
        return this.rootFactHandles.values().contains( handle );
    }

    public FactHandle getRootFactHandle(Object value)
    {
        return (FactHandle) this.rootFactHandles.get( value );
    }

    /** Retrieve the number of columns in this key.
     *
     *  @return The number of columns.
     */
    public int size()
    {
        return this.columns.size();
    }

    /** Retrieve the declarations of this key.
     *
     *  @see Declaration
     *
     *  @return The set of declarations for this key.
     */
    public Set getDeclarations()
    {
        return this.columns.keySet();
    }

    public boolean containsAll(TupleKey that)
    {
        return this.rootFactHandles.values().containsAll( that.rootFactHandles.values() );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Determine if this key is semantically equal to another.
     *
     *  @param thatObj The object to compare.
     *
     *  @return <code>true</code> if the two objects are semantically
     *          equal, otherwise <code>false</code>.
     */
    public boolean equals(Object thatObj)
    {
        if ( thatObj instanceof TupleKey )
        {
            TupleKey that = (TupleKey) thatObj;

            Collection thisKeys = this.rootFactHandles.values();
            Collection thatKeys = that.rootFactHandles.values();

            boolean result = ( thisKeys.size() == thatKeys.size()
                               &&
                               thisKeys.containsAll( thatKeys ) );

            return result;
        }
        
        return false;
    }

    /** Retrieve the hash-code for this key.
     *
     *  @return The hash-code.
     */
    public int hashCode()
    {
        return new HashSet( this.rootFactHandles.values() ).hashCode();
    }
}
