package org.drools.reteoo.impl;

/*
 $Id: JoinNodeImpl.java,v 1.1 2002-07-28 13:55:47 bob Exp $

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
import org.drools.reteoo.JoinNode;
import org.drools.reteoo.TupleSource;
import org.drools.spi.Declaration;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** A two-input Rete-OO <i>join node</i>.
 *
 *  @see TupleSource
 *  @see TupleSink
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class JoinNodeImpl extends TupleSourceImpl implements JoinNode, TupleSinkImpl
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The left input <code>TupleSource</code>.
     */
    private TupleSourceImpl leftInput;

    /** The right input <code>TupleSource</code>.
     */
    private TupleSourceImpl rightInput;

    /** A <code>Set</code> of <code>Declarations</code>
     *  common to both left and right input sources.
     */
    private Set commonDeclarations;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param leftInput The left input <code>TupleSource</code>.
     *  @param rightInput The right input <code>TupleSource</code>.
     */
    public JoinNodeImpl(TupleSourceImpl leftInput,
                        TupleSourceImpl rightInput)
    {
        this.leftInput  = leftInput;
        this.rightInput = rightInput;

        determineCommonDeclarations();

        leftInput.setTupleSink( this );
        rightInput.setTupleSink( this );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set up the <code>Set</code> of common <code>Declarations</code>
     *  across the two input <code>TupleSources</code>.
     */
    private void determineCommonDeclarations()
    {
        this.commonDeclarations = new HashSet();

        Set leftDecls = leftInput.getTupleDeclarations();
        Set rightDecls = rightInput.getTupleDeclarations();

        Iterator    declIter = rightDecls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            if ( leftDecls.contains( eachDecl ) )
            {
                this.commonDeclarations.add( eachDecl );
            }
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.JoinNode
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the set of common <code>Declarations</code>
     *  across the two input <code>TupleSources</code>.
     *
     *  @return The <code>Set</code> of common <code>Declarations</code>.
     */
    public Set getCommonDeclarations()
    {
        return this.commonDeclarations;
    }

    /** Retrieve the left input <code>TupleSource</code>.
     *
     *  @return The left input <code>TupleSource</code>.
     */
    public TupleSource getLeftInput()
    {
        return this.leftInput;
    }

    /** Retrieve the right input <code>TupleSource</code>.
     *
     *  @return The right input <code>TupleSource</code>.
     */
    public TupleSource getRightInput()
    {
        return this.rightInput;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>Set</code> of <code>Declaration</code>s.
     *  in the propagated <code>Tuples</code>.
     *
     *  @see Declaration
     *
     *  @return The <code>Set</code> of <code>Declarations</code>
     *          in progated <code>Tuples</code>.
     */
    public Set getTupleDeclarations()
    {
        Set decls = new HashSet();

        decls.addAll( getLeftInput().getTupleDeclarations() );
        decls.addAll( getRightInput().getTupleDeclarations() );

        return decls;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.impl.TupleSinkImpl
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
        JoinMemoryImpl memory = (JoinMemoryImpl) workingMemory.getJoinMemory( this );

        Set joinedTuples = null;

        if ( inputSource == leftInput )
        {
            joinedTuples = memory.addLeftTuple( tuple );
        }
        else
        {
            joinedTuples = memory.addRightTuple( tuple );
        }

        if ( joinedTuples.isEmpty() )
        {
            return;
        }

        Iterator  tupleIter = joinedTuples.iterator();
        ReteTuple eachTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            propagateAssertTuple( eachTuple,
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
        JoinMemoryImpl memory = (JoinMemoryImpl) workingMemory.getJoinMemory( this );

        memory.retractTuples( key );

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
        JoinMemoryImpl memory = (JoinMemoryImpl) workingMemory.getJoinMemory( this );

        if ( inputSource == leftInput )
        {
            memory.modifyLeftTuples( trigger,
                                     newTuples,
                                     this,
                                     workingMemory );
        }
        else
        {
            memory.modifyRightTuples( trigger,
                                      newTuples,
                                      this,
                                      workingMemory );
        }
    }
}
