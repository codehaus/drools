package org.drools.reteoo.impl;

/*
 $Id: TerminalNodeImpl.java,v 1.1 2002-07-28 13:55:47 bob Exp $

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
import org.drools.AssertionException;
import org.drools.reteoo.TerminalNode;
import org.drools.reteoo.TupleSource;
import org.drools.spi.Rule;

/** Leaf Rete-OO node responsible for enacting <code>Action</code>s
 *  on a matched <code>Rule</code>.
 *
 *  @see org.drools.spi.Rule
 *  @see org.drools.spi.Action
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class TerminalNodeImpl implements TerminalNode, TupleSinkImpl
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Total-ordering priority of this terminal node for rule-firings. */
    private int priority;

    /** The rule to invoke upon match. */
    private Rule rule;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param inputSource The parent tuple source.
     *  @param rule The rule.
     *  @param priority The priority.
     */
    public TerminalNodeImpl(TupleSource inputSource,
                            Rule rule,
                            int priority)
    {
        this.rule = rule;

        this.priority = priority;

        if ( inputSource != null )
        {
            ((TupleSourceImpl)inputSource).setTupleSink( this );
        }
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the priority
     *
     *  @return The priority.
     */
    public int getPriority()
    {
        return this.priority;
    }

    /** Retrieve the <code>Action</code> associated with
     *  this node.
     *
     *  @return The <code>Action</code> associated with
     *          this node.
     */
    public Rule getRule()
    {
        return this.rule;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.impl.TupleSink
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
        AgendaImpl agenda = (AgendaImpl) workingMemory.getAgenda();

        agenda.addToAgenda( tuple,
                            getRule(),
                            getPriority() );
    }

    /** Retract tuples.
     *
     *  @param key The tuple key.
     *  @param workingMemory The working memory seesion.
     */
    public void retractTuples(TupleKey key,
                              WorkingMemory workingMemory) 
    {
        AgendaImpl agenda = (AgendaImpl) workingMemory.getAgenda();

        agenda.removeFromAgenda( key,
                                 getRule() );
    }

    /** Modify tuples.
     *
     *  @param inputSource Source of modifications.
     *  @param trigger The root fact object.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     */
    public void modifyTuples(TupleSourceImpl inputSource,
                             Object trigger,
                             TupleSet newTuples,
                             WorkingMemory workingMemory)
    {
        AgendaImpl agenda = (AgendaImpl) workingMemory.getAgenda();
        
        agenda.modifyAgenda( trigger,
                             newTuples,
                             getRule(),
                             getPriority() );
    }
}
