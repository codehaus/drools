package org.drools.reteoo;

/*
 * $Id: TupleKey.java,v 1.19 2004-10-30 16:13:31 simon Exp $
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A composite key to match tuples.
 *
 * @see Tuple
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
class TupleKey implements Serializable
{
    public static final TupleKey EMPTY = new TupleKey();

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Columns. */
    private final Map handles;

    /** Cached hashCode. */
    private int hashCode;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    private TupleKey()
    {
        handles = Collections.EMPTY_MAP;
    }

    public TupleKey(TupleKey that)
    {
        handles = new HashMap( that.handles.size( ), 1 );
        handles.putAll( that.handles );
    }

    public TupleKey(TupleKey left, TupleKey right)
    {
        handles = new HashMap( left.handles.size( ) + right.handles.size( ), 1 );
        this.handles.putAll( left.handles );
        this.handles.putAll( right.handles );
    }

    public TupleKey(Declaration declaration, FactHandle handle)
    {
        handles = Collections.singletonMap( declaration, handle );
    }

    public String toString()
    {
        return "[TupleKey: handles=" + this.handles + "]";
    }

    // ------------------------------------------------------------
    //
    // ------------------------------------------------------------

    /**
     * Retrieve a <code>FactHandle</code> by declaration.
     *
     * @param declaration The declaration.
     *
     * @return The fact handle.
     */
    public FactHandle get(Declaration declaration)
    {
        return ( FactHandle ) this.handles.get( declaration );
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
        return this.handles.values( ).contains( handle );
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
        // return this.handles.containsAll( that.handles );

        for ( Iterator declIter = that.handles.keySet( ).iterator( ); declIter
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
            return this.handles.equals( ( ( TupleKey ) thatObj ).handles );
        }

        return false;
    }

    public Iterator iterator()
    {
        return this.handles.keySet( ).iterator( );
    }

    /**
     * @see Object
     */
    public int hashCode()
    {
        if ( this.hashCode == 0 )
        {
            this.hashCode = this.handles.hashCode( );
        }

        return hashCode;
    }
}