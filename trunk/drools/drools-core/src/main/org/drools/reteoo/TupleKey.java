package org.drools.reteoo;

/*
 $Id: TupleKey.java,v 1.8 2003-11-19 21:31:10 bob Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a trademark of 
    The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werken.com/)
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

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
