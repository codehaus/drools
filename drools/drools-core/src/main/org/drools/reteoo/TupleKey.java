package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.rule.Declaration;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
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
    //private Set rootFactHandles;
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
        this.rootFactHandles.put( null,
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

    /** Determine if this key is a super-set of another key.
     *  
     *  @param that The key to test for subset.
     *
     *  @return <code>true</code> if this key is a super-set of
     *          the specified parameter key, otherwise <code>false</code>.
     */
    /*
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
    */

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
        TupleKey that = (TupleKey) thatObj;

        return this.columns.equals( that.columns );
    }

    /** Retrieve the hash-code for this key.
     *
     *  @return The hash-code.
     */
    public int hashCode()
    {
        return this.columns.hashCode();
    }

    /** Produce a debug string.
     *
     *  @return The debug string.
     */
    public String toString()
    {
        return "[TupleKey: columns=" + this.columns
            + "; handles=" + this.rootFactHandles
            + "]";
    }
}
