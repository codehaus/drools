package org.drools.reteoo.impl;

/*
 $Id: TupleSourceImpl.java,v 1.2 2002-07-28 15:49:50 bob Exp $

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
import org.drools.reteoo.TupleSink;
import org.drools.reteoo.TupleSource;

/** A source of <code>ReteTuple</code>s for a <code>TupleSink</code>.
 *
 *  <p>
 *  Nodes that propagate <code>Tuples</code> extend this class.
 *  </p>
 *
 *  @see TupleSource
 *  @see TupleSinkImpl
 *  @see ReteTuple
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public abstract class TupleSourceImpl implements TupleSource
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The destination for <code>Tuples</code>. */
    private TupleSinkImpl tupleSink;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected TupleSourceImpl()
    {
        // intentionally left blank.
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the <code>TupleSink</code> that receives <code>Tuples</code>
     *  propagated from this <code>TupleSource</code>.
     *
     *  @param tupleSink The <code>TupleSink</code> to receive
     *         propagated <code>Tuples</code>.
     */
    protected void setTupleSink(TupleSinkImpl tupleSink)
    {
        this.tupleSink = tupleSink;
    }

    /** Propagate the assertion of a <code>Tuple</code>
     *  to this node's <code>TupleSink</code>.
     *
     *  @param tuple The <code>Tuple</code> to propagate.
     *  @param workingMemory the working memory session.
     *
     *  @throws AssertionException If an errors occurs while
     *          attempting assertion.
     */
    protected void propagateAssertTuple(ReteTuple tuple,
                                        WorkingMemory workingMemory) throws AssertionException
    {
        TupleSinkImpl sink = (TupleSinkImpl) getTupleSink();

        if ( sink != null )
        {
            sink.assertTuple( this,
                              tuple,
                              workingMemory );
        }
    }

    /** Propagate the retration of a <code>Tuple</code>
     *  to this node's <code>TupleSink</code>.
     *
     *  @param key The tuple key.
     *  @param workingMemory The working memory session.
     *
     *  @throws RetractionException If an error occurs while
     *          attempting retraction
     *
     */
    protected void propagateRetractTuples(TupleKey key,
                                          WorkingMemory workingMemory) throws RetractionException
    {
        TupleSinkImpl sink = (TupleSinkImpl) getTupleSink();

        if ( sink != null )
        {
            sink.retractTuples( key,
                                workingMemory );
        }
    }

    /** Propagate the modification of <code>Tuple</code>s
     *  to this node's <code>TupleSink</code>.
     *
     *  @param trigger The modification trigger object.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while
     *          attempting modification.
     */
    protected void propagateModifyTuples(Object trigger,
                                         TupleSet newTuples,
                                         WorkingMemory workingMemory) throws FactException
    {
        TupleSinkImpl sink = (TupleSinkImpl) getTupleSink();

        if ( sink != null )
        {
            sink.modifyTuples( this,
                               trigger,
                               newTuples,
                               workingMemory );
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>TupleSink</code> that receives
     *  propagated <code>Tuples</code>.
     *
     *  @return The <code>TupleSink</code> that receives
     *          propagated <code>Tuples</code>.
     */
    public TupleSink getTupleSink()
    {
        return this.tupleSink;
    }
}
