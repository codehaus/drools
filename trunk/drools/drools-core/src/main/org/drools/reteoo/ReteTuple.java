package org.drools.reteoo;

/*
 * $Id: ReteTuple.java,v 1.40 2004-10-30 01:11:48 simon Exp $
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Base Rete-OO <code>Tuple</code> implementation.
 *
 * @see Tuple
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 *
 * @version $Id: ReteTuple.java,v 1.40 2004-10-30 01:11:48 simon Exp $
 */
class ReteTuple implements Tuple, Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    private final WorkingMemory  workingMemory;

    private final Rule           rule;

    /** Key columns for this tuple. */
    private final TupleKey       key;

    /** Value columns in this tuple. */
    private final Map            columns;

    /** return array of condition time stamps */
    private final long conditionTimeStamps[];

    private FactHandleImpl mostRecentFact;

    private FactHandleImpl leastRecentFact;

    private boolean        isChanged = true;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    public ReteTuple(WorkingMemory workingMemory, Rule rule)
    {
        this.workingMemory = workingMemory;
        this.rule = rule;
        this.key = new TupleKey( );
        this.columns = new HashMap( );

        this.conditionTimeStamps = new long[rule.getConditionSize( )];
    }

    /**
     * Copy constructor.
     *
     * @param that The tuple to copy.
     */
    ReteTuple(ReteTuple that)
    {
        this.workingMemory = that.workingMemory;
        this.rule = that.rule;
        this.key = new TupleKey( that.key );
        this.columns = new HashMap( that.columns );
        this.conditionTimeStamps = that.getConditionTimeStamps();
    }

    /**
     * Construct a simple 1-column tuple.
     *
     * @param declaration The column declaration.
     * @param handle The fact-handle.
     * @param value The column value.
     */
    ReteTuple(WorkingMemory workingMemory,
              Rule rule,
              Declaration declaration,
              FactHandle handle,
              Object value)
    {
        this( workingMemory, rule );

        key.put( declaration, handle );

        putTargetDeclarationColumn( declaration, value );
    }

    public String toString()
    {
        return "{" + this.columns + "}";
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /**
     * Add all columns from another tuple.
     *
     * @param that The column source tuple.
     */
    public void putAll(ReteTuple that)
    {
        this.key.putAll( that.key );
        this.columns.putAll( that.columns );
        this.isChanged = true;

        long[] conditionTimeStamps = that.getConditionTimeStamps( );
        for ( int i = 0; i < conditionTimeStamps.length; i++ )
        {
            if ( conditionTimeStamps[i] > 0 )
            {
                this.conditionTimeStamps[i] = conditionTimeStamps[i];
            }
        }

        this.isChanged = true;
    }

    /**
     * Extractors may not have a key, so need to keep track
     * of Columns derived from extractors
     *
     * @param declaration
     * @param value
     */
    public void putTargetDeclarationColumn(Declaration declaration, Object value)
    {
        this.columns.put( declaration, value );

        this.isChanged = true;
    }

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
        return this.columns.get( declaration );
    }

    /**
     * @see Tuple
     */
    public Set getDeclarations()
    {
        return this.columns.keySet( );
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
        FactHandleImpl mostRecentFact = getMostRecentFact( );
        return ( mostRecentFact != null ) ? mostRecentFact.getRecency() : -1;
    }

    public long getLeastRecentFactTimeStamp()
    {
        FactHandleImpl leastRecentFact = getLeastRecentFact();
        return ( leastRecentFact != null ) ? leastRecentFact.getRecency() : -1;
    }

    public void setConditionTimeStamp(int order, long timeStamp)
    {
        this.conditionTimeStamps[order] = timeStamp;
    }

    public long getConditionTimeStamp(int order)
    {
        return this.conditionTimeStamps[order];
    }

    long[] getConditionTimeStamps()
    {
        return this.conditionTimeStamps;
    }

    private FactHandleImpl getMostRecentFact()
    {
        if ( this.isChanged )
        {
            long currentRecency = ( this.mostRecentFact != null ) ? this.mostRecentFact.getRecency() : Long.MIN_VALUE;
            FactHandleImpl fact;
            long recency;

            for ( Iterator i = this.key.iterator(); i.hasNext(); )
            {
                fact = ( FactHandleImpl ) this.key.get( ( Declaration ) i.next() );
                recency = fact.getRecency();
                if ( recency > currentRecency )
                {
                    currentRecency = recency;
                    this.mostRecentFact = fact;
                }
            }

            this.isChanged = false;
        }
        return this.mostRecentFact;
    }

    private FactHandleImpl getLeastRecentFact()
    {
        if ( this.isChanged )
        {
            long currentRecency = ( this.mostRecentFact != null ) ? this.mostRecentFact.getRecency() : Long.MAX_VALUE;
            FactHandleImpl fact;
            long recency;

            for ( Iterator i = this.key.iterator(); i.hasNext(); )
            {
                fact = ( FactHandleImpl ) this.key.get( ( Declaration ) i.next() );
                recency = fact.getRecency();
                if ( recency < currentRecency )
                {
                    currentRecency = recency;
                    this.leastRecentFact = fact;
                }
            }

            this.isChanged = false;
        }
        return this.leastRecentFact;
    }
}
