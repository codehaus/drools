package org.drools.reteoo;

/*
 * $Id: TupleKey.java,v 1.16 2004-10-09 06:59:00 simon Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import org.drools.FactHandle;
import org.drools.rule.Declaration;
import org.drools.spi.Tuple;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A composite key to match tuples.
 *
 * @see Tuple
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
class TupleKey implements Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Columns. */
    private Map columns;

    /** Cached hashCode. */
    private int hashCode;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    public TupleKey()
    {
        this.columns = new HashMap( );
    }

    /**
     * Copy constructor.
     *
     * @param that The tuple key to copy.
     */
    public TupleKey(TupleKey that)
    {
        this( );
        putAll( that );
    }

    public TupleKey(Declaration declaration, FactHandle factHandle)
    {
        this( );
        put( declaration, factHandle );
    }

    public String toString()
    {
        return "[TupleKey: columns=" + this.columns + "]";
    }

    // ------------------------------------------------------------
    //
    // ------------------------------------------------------------

    /**
     * Put all values from another key into this key.
     *
     * @param key The source value key.
     */
    public void putAll(TupleKey key)
    {
        this.columns.putAll( key.columns );
        this.hashCode = 0;
    }

    /**
     * Put a value for a declaration.
     *
     * @param declaration Column declaration.
     * @param handle The handle.
     * @param value The value.
     */
    public void put(Declaration declaration, FactHandle handle)
    {
        this.columns.put( declaration, handle );
        this.hashCode = 0;
    }

    /**
     * Retrieve a <code>FactHandle</code> by declaration.
     *
     * @param declaration The declaration.
     *
     * @return The fact handle.
     */
    public FactHandle get(Declaration declaration)
    {
        return ( FactHandle ) this.columns.get( declaration );
    }

    /**
     * Determine if this key contains the specified declaration.
     *
     * @param declaration The declaration to test.
     *
     * @return <code>true</code> if this key contains a column for the
     *         specified declaration, otherwise <code>false</code>.
     */
    public boolean containsDeclaration(Declaration declaration)
    {
        return this.columns.containsKey( declaration );
    }

    /**
     * Determine if this key contains the specified root fact object.
     *
     * @param handle The fact-handle to test.
     *
     * @return <code>true</code> if this key contains the specified root
     *         fact-handle, otherwise <code>false</code>.
     */
    public boolean containsRootFactHandle(FactHandle handle)
    {
        return this.columns.values( ).contains( handle );
    }

    /**
     * Retrieve the number of columns in this key.
     *
     * @return The number of columns.
     */
    public int size()
    {
        return this.columns.size( );
    }

    /**
     * Retrieve the declarations of this key.
     *
     * @see Declaration
     *
     * @return The set of declarations for this key.
     */
    public Set getDeclarations()
    {
        return this.columns.keySet( );
    }

    /**
     * Determine if the specified key is a subset of this key.
     *
     * @param that The key to compare.
     *
     * @return <code>true</code> if the specified key is a subset of this key.
     */
    public boolean containsAll(TupleKey that)
    {
        // return this.columns.containsAll( that.columns );

        for ( Iterator declIter = that.getDeclarations( ).iterator( ); declIter
                                                                               .hasNext( ); )
        {
            Declaration eachDecl = ( Declaration ) declIter.next( );

            FactHandle thatHandle = that.get( eachDecl );
            FactHandle thisHandle = this.get( eachDecl );

            if ( !thatHandle.equals( thisHandle ) )
            {
                return false;
            }
        }

        return true;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * @see Object
     */
    public boolean equals(Object thatObj)
    {
        if (this == thatObj)
        {
            return true;
        }

        if ( thatObj instanceof TupleKey )
        {
            return this.columns.equals( ( ( TupleKey ) thatObj ).columns );
        }

        return false;
    }

    public Iterator iterator()
    {
        return this.columns.keySet( ).iterator( );
    }

    /**
     * @see Object
     */
    public int hashCode()
    {
        if ( this.hashCode == 0 )
        {
            this.hashCode = this.columns.hashCode( );
        }

        return hashCode;
    }
}