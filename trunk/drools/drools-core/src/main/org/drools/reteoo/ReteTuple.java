package org.drools.reteoo;

/*
 * $Id: ReteTuple.java,v 1.48 2004-11-16 07:26:33 simon Exp $
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
import org.drools.NoSuchFactHandleException;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Tuple;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Base Rete-OO <code>Tuple</code> implementation.
 *
 * @see Tuple
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 */
class ReteTuple implements Tuple, Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    private final WorkingMemory workingMemory;

    private final Rule          rule;

    /** Key objects for this tuple. */
    private final TupleKey      key;

    /** Value objects in this tuple. */
    private final Map           objects;

    private FactHandleImpl      mostRecentFact;

    private FactHandleImpl      leastRecentFact;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    public ReteTuple(WorkingMemory workingMemory, Rule rule)
    {
        this.workingMemory = workingMemory;
        this.rule = rule;
        this.key = TupleKey.EMPTY;
        this.objects = Collections.EMPTY_MAP;
    }

    ReteTuple(ReteTuple left, ReteTuple right)
    {
        this.workingMemory = left.workingMemory;
        this.rule = left.rule;
        this.key = new TupleKey( left.key, right.key );
        this.objects = new HashMap( left.objects.size( ) + right.objects.size( ), 1 );
        this.objects.putAll( left.objects );
        this.objects.putAll( right.objects );
    }

    ReteTuple(ReteTuple that,
              Declaration declaration,
              Object value)
    {
        this.workingMemory = that.workingMemory;
        this.rule = that.rule;
        this.key = that.key;
        this.objects = new HashMap( that.objects.size( ) + 1, 1 );
        this.objects.putAll( that.objects );
        this.objects.put( declaration, value );
    }

    ReteTuple(WorkingMemory workingMemory,
              Rule rule,
              Declaration declaration,
              FactHandle handle,
              Object value)
    {
        this.workingMemory = workingMemory;
        this.rule = rule;
        this.key = new TupleKey( declaration, handle );
        this.objects = Collections.singletonMap( declaration, value );
    }

    public String toString()
    {
        return "{" + this.objects + "}";
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the key for this tuple.
     *
     * @return The key.
     */
    TupleKey getKey()
    {
        return this.key;
    }

    /**
     * Determine if this tuple depends upon a specified object.
     *
     * @param handle The object handle to test.
     *
     * @return <code>true</code> if this tuple depends upon the specified
     *         object, otherwise <code>false</code>.
     */
    boolean dependsOn(FactHandle handle)
    {
        return this.key.containsRootFactHandle( handle );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * @see Tuple
     */
    public Object get(Declaration declaration)
    {
        return this.objects.get( declaration );
    }

    /**
     * @see Tuple
     */
    public Set getDeclarations()
    {
        return this.objects.keySet( );
    }

    /**
     * @see Tuple
     */
    public FactHandle getFactHandleForObject(Object object)
    {
        FactHandle factHandle;
        try
        {
            factHandle = this.workingMemory.getFactHandle( object );

        }
        catch ( NoSuchFactHandleException e )
        {
            factHandle = null;
        }

        return factHandle;
    }

    public Rule getRule()
    {
        return this.rule;
    }

    public WorkingMemory getWorkingMemory()
    {
        return this.workingMemory;
    }

    public long getMostRecentFactTimeStamp()
    {
        if ( this.mostRecentFact == null )
        {
            this.mostRecentFact = this.key.getMostRecentFact( );
        }
        return this.mostRecentFact != null ? this.mostRecentFact.getRecency( ) : -1;
    }

    public long getLeastRecentFactTimeStamp()
    {
        if ( this.leastRecentFact == null )
        {
            this.leastRecentFact = this.key.getLeastRecentFact();
        }
        return this.leastRecentFact != null ? this.leastRecentFact.getRecency( ) : -1;
    }
}
