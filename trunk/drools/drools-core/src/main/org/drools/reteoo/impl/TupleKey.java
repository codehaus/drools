package org.drools.reteoo.impl;

/*
 $Id: TupleKey.java,v 1.2 2002-07-28 15:49:50 bob Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).
 
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

import org.drools.spi.Declaration;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/** A composite key to match tuples.
 *
 *  @see TupleImpl
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

    /** Root fact objects. */
    private Set rootFactObjects;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public TupleKey()
    {
        this.columns         = new HashMap();
        this.rootFactObjects = new HashSet();
    }

    /** Construct a simple 1-column tuple-key.
     *
     *  @param declaration Column declaration.
     *  @param value Column value.
     */
    public TupleKey(Declaration declaration,
                    Object value)
    {
        this();

        put( declaration,
             value );
    }

    /** Copy constructor.
     *
     *  @param that The tuple key to copy.
     */
    public TupleKey(TupleKey that)
    {
        this();

        this.columns.putAll( that.columns );
        this.rootFactObjects.addAll( that.rootFactObjects );
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
        this.rootFactObjects.addAll( key.rootFactObjects );
    }

    /** Put a value for a declaration.
     *
     *  @param declaration Column declaration.
     *  @param value The value.
     */
    public void put(Declaration declaration,
                    Object value)
    {
        this.columns.put( declaration,
                          value );

        this.rootFactObjects.add( value );
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
    public boolean containsRootFactObject(Object object)
    {
        return this.rootFactObjects.contains( object );
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
        return "[TupleKey: columns=" + this.columns + "]";
    }
}
