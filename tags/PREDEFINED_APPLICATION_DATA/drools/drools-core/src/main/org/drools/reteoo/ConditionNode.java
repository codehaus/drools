package org.drools.reteoo;

/*
 * $Id: ConditionNode.java,v 1.26 2004-11-21 12:33:52 simon Exp $
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

import org.drools.AssertionException;
import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.RetractionException;
import org.drools.spi.Condition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Node which filters <code>ReteTuple</code>s.
 *
 * <p>
 * Using a semantic <code>Condition</code>, this node may allow or disallow
 * <code>Tuples</code> to proceed further through the Rete-OO network.
 * </p>
 *
 * @see ConditionNode
 * @see Condition
 * @see ReteTuple
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
class ConditionNode extends TupleSource
    implements
    TupleSink
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The semantic <code>Condition</code>. */
    private final Condition condition;

    /** The source of incoming <code>Tuples</code>. */
    private final TupleSource tupleSource;

    /** The order the condition is declared in its rule */
    private final int order;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param tupleSource
     *            The source of incoming <code>Tuples</code>.
     * @param condition
     *            The semantic <code>Condition</code>.
     */
    ConditionNode(TupleSource tupleSource,
                  Condition condition,
                  int order)
    {
        this.condition = condition;
        this.tupleSource = tupleSource;
        this.order = order;

        if ( tupleSource != null )
        {
            this.tupleSource.setTupleSink( this );
        }
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the <code>Condition</code> associated with this node.
     *
     * @return The <code>Condition</code>.
     */
    public Condition getCondition()
    {
        return this.condition;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // org.drools.reteoo.impl.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Retrieve the <code>Set</code> of <code>Declaration</code> s in the
     * propagated <code>Tuples</code>.
     *
     * @return The <code>Set</code> of <code>Declarations</code> in progated
     *         <code>Tuples</code>.
     */
    public Set getTupleDeclarations()
    {
        return this.tupleSource.getTupleDeclarations( );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // org.drools.reteoo.impl.TupleSink
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Assert a new <code>Tuple</code>.
     *
     * @param tuple
     *            The <code>Tuple</code> being asserted.
     * @param workingMemory
     *            The working memory seesion.
     *
     * @throws AssertionException
     *             If an error occurs while asserting.
     */
    public void assertTuple(ReteTuple tuple,
                            WorkingMemoryImpl workingMemory) throws AssertionException
    {
        Condition condition = getCondition( );
        boolean isAllowed = condition.isAllowed( tuple );

        workingMemory.getEventSupport( ).fireConditionTested( tuple.getRule( ),
                                                              condition,
                                                              tuple,
                                                              isAllowed );

        if ( isAllowed )
        {
            // tuple.setConditionTimeStamp( order,
            // workingMemory.getConditionTimeStamp() );
            propagateAssertTuple( tuple,
                                  workingMemory );
        }

    }

    /**
     * Retract tuples.
     *
     * @param key
     *            The tuple key.
     * @param workingMemory
     *            The working memory seesion.
     *
     * @throws RetractionException
     *             If an error occurs while retracting.
     */
    public void retractTuples(TupleKey key,
                              WorkingMemoryImpl workingMemory) throws RetractionException
    {
        propagateRetractTuples( key,
                                workingMemory );
    }

    /**
     * Modify tuples.
     *
     * @param trigger
     *            The root fact object handle.
     * @param newTuples
     *            Modification replacement tuples.
     * @param workingMemory
     *            The working memory session.
     *
     * @throws FactException
     *             If an error occurs while modifying.
     */
    public void modifyTuples(FactHandle trigger,
                             TupleSet newTuples,
                             WorkingMemoryImpl workingMemory) throws FactException
    {
        Set retractedKeys = new HashSet( );

        Iterator tupleIter = newTuples.iterator( );
        ReteTuple eachTuple;
        TupleKey eachKey;

        Condition condition = getCondition( );
        boolean isAllowed;
        while ( tupleIter.hasNext( ) )
        {
            eachTuple = (ReteTuple) tupleIter.next( );

            isAllowed = condition.isAllowed( eachTuple );

            workingMemory.getEventSupport( ).fireConditionTested( eachTuple.getRule( ),
                                                                  condition,
                                                                  eachTuple,
                                                                  isAllowed );

            if ( !isAllowed )
            {
                tupleIter.remove( );

                eachKey = eachTuple.getKey( );

                if ( retractedKeys.add( eachKey ) )
                {
                    propagateRetractTuples( eachKey,
                                            workingMemory );

                }
            }
        }

        if ( !newTuples.isEmpty( ) )
        {
            propagateModifyTuples( trigger,
                                   newTuples,
                                   workingMemory );
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Produce a debug string.
     *
     * @return The debug string.
     */
    public String toString()
    {
        return "[ConditionNode: cond=" + this.condition + "]";
    }
}