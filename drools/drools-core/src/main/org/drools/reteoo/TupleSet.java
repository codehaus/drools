package org.drools.reteoo;

/*
 * $Id: TupleSet.java,v 1.19 2004-11-16 11:37:12 simon Exp $
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A set of <code>Tuple<code>s indexed by <code>TupleKey<code>s.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class TupleSet implements Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Tuples, indexed by TupleKey. */
    private final Map tuples;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    TupleSet()
    {
        this.tuples = new HashMap( );
    }

    /**
     * Retrieve the size (number of tuples) in this set.
     *
     * @return The size of this set.
     */
    public int size()
    {
        return this.tuples.size( );
    }

    /**
     * Add a single <code>Tuple</code> to this set.
     *
     * @param tuple The tuple.
     */
    public void addTuple(ReteTuple tuple)
    {
        this.tuples.put( tuple.getKey( ), tuple );
    }

    /**
     * Retract tuples from this memory.
     *
     * @param key The key for the tuples to be removed.
     */
    public void removeAllTuples( TupleKey key )
    {
        Iterator tupleIter = iterator();
        while ( tupleIter.hasNext() )
        {
            if ( ( ( ReteTuple ) tupleIter.next() ).getKey().containsAll( key ) )
            {
                tupleIter.remove();
            }
        }
    }

    /**
     * Retrieve all <code>Tuple</code>s.
     *
     * @see org.drools.spi.Tuple
     *
     * @return The set of tuples.
     */
    public Set getTuples()
    {
        return new HashSet( this.tuples.values( ) );
    }

    /**
     * Retrieve an iterator over the tuples.
     *
     * @return The iterator.
     */
    public Iterator iterator()
    {
        return this.tuples.values( ).iterator( );
    }

    public String toString()
    {
        return this.tuples.values( ).toString( );
    }
}

