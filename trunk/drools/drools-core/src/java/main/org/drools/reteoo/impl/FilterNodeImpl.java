package org.drools.reteoo.impl;

/*
 $Id: FilterNodeImpl.java,v 1.2 2002-07-28 15:49:50 bob Exp $

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

import org.drools.WorkingMemory;
import org.drools.FactException;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.reteoo.FilterNode;
import org.drools.spi.FilterCondition;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** Node which filters <code>ReteTuple</code>s.
 *
 *  <p>
 *  Using a semantic <code>FilterCondition</code>, this node
 *  may allow or disallow <code>Tuples</code> to proceed
 *  further through the Rete-OO network.
 *  </p>
 *
 *  @see FilterNode
 *  @see FilterCondition
 *  @see ReteTuple
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class FilterNodeImpl extends TupleSourceImpl implements FilterNode, TupleSinkImpl
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The semantic <code>FilterCondition</code>. */
    private FilterCondition filterCondition;

    /** The source of incoming <code>Tuples</code>. */
    private TupleSourceImpl tupleSource;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param tupleSource The source of incoming <code>Tuples</code>.
     *  @param filterCondition The semantic <code>FilterCondition</code>.
     */
    public FilterNodeImpl(TupleSourceImpl tupleSource,
                          FilterCondition filterCondition)
    {
        this.filterCondition = filterCondition;
        this.tupleSource     = tupleSource;

        if ( tupleSource != null )
        {
            this.tupleSource.setTupleSink( this );
        }
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the <code>FilterCondition</code> associated
     *  with this node.
     *
     *  @return The <code>FilterCondition</code>.
     */
    public FilterCondition getFilterCondition()
    {
        return this.filterCondition;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.impl.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>Set</code> of <code>Declaration</code>s
     *  in the propagated <code>Tuples</code>.
     *
     *  @return The <code>Set</code> of <code>Declarations</code>
     *          in progated <code>Tuples</code>.
     */
    public Set getTupleDeclarations()
    {
        return this.tupleSource.getTupleDeclarations();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.impl.TupleSink
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Assert a new <code>Tuple</code>.
     *
     *  @param inputSource The source of the <code>Tuple</code>.
     *  @param tuple The <code>Tuple</code> being asserted.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws AssertionException If an error occurs while asserting.
     */
    public void assertTuple(TupleSourceImpl inputSource,
                            ReteTuple tuple,
                            WorkingMemory workingMemory) throws AssertionException
    {
        if ( getFilterCondition().isAllowed( tuple ) )
        {
            propagateAssertTuple( tuple,
                                  workingMemory );
        }
    }

    /** Retract tuples.
     *
     *  @param key The tuple key.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws RetractionException If an error occurs while retracting.
     */
    public void retractTuples(TupleKey key,
                              WorkingMemory workingMemory) throws RetractionException
    {
        propagateRetractTuples( key,
                                workingMemory );
    }

    /** Modify tuples.
     *
     *  @param inputSource Source of modifications.
     *  @param trigger The root fact object.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while modifying.
     */
    public void modifyTuples(TupleSourceImpl inputSource,
                             Object trigger,
                             TupleSet newTuples,
                             WorkingMemory workingMemory) throws FactException
    {
        Set retractedKeys = new HashSet();

        Iterator  tupleIter = newTuples.iterator();
        ReteTuple eachTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            if ( ! getFilterCondition().isAllowed( eachTuple ) )
            {
                tupleIter.remove();
                retractedKeys.add( eachTuple.getKey() );
            }
        }

        propagateModifyTuples( trigger,
                               newTuples,
                               workingMemory );

        Iterator keyIter = retractedKeys.iterator();
        TupleKey eachKey = null;

        while ( keyIter.hasNext() )
        {
            eachKey = (TupleKey) keyIter.next();

            propagateRetractTuples( eachKey,
                                    workingMemory );
        }
    }
}
