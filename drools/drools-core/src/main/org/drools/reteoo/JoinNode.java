package org.drools.reteoo;

/*
 $Id: JoinNode.java,v 1.14 2003-11-19 21:31:10 bob Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a trademark of 
    The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werken.com/)
 
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

import org.drools.FactHandle;
import org.drools.FactException;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.rule.Declaration;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** A two-input Rete-OO <i>join node</i>.
 *
 *  @see org.drools.reteoo.TupleSource
 *  @see org.drools.reteoo.TupleSink
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class JoinNode
    extends TupleSource
    implements Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The left input <code>TupleSource</code>.
     */
    private TupleSource leftInput;

    /** The right input <code>TupleSource</code>.
     */
    private TupleSource rightInput;

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
    public JoinNode(TupleSource leftInput,
                    TupleSource rightInput)
    {
        this.leftInput  = leftInput;
        this.rightInput = rightInput;

        determineCommonDeclarations();

        leftInput.setTupleSink( getLeftNodeInput() );
        rightInput.setTupleSink( getRightNodeInput() );
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

    /** Propagate joined asserted tuples.
     *
     *  @param joinedTuples The tuples to propagate.
     *  @param workingMemory The working memory session.
     *
     *  @throws AssertionException If an errors occurs while asserting.
     */
    void propagateAssertTuples(Set joinedTuples,
                               WorkingMemoryImpl workingMemory) throws AssertionException
    {
        Iterator  tupleIter = joinedTuples.iterator();
        ReteTuple eachTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            propagateAssertTuple( eachTuple,
                                  workingMemory );
        }
    }

    /** Assert a new <code>Tuple</code> from the left input.
     *
     *  @param tuple The <code>Tuple</code> being asserted.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws AssertionException If an error occurs while asserting.
     */
    void assertLeftTuple(ReteTuple tuple,
                         WorkingMemoryImpl workingMemory) throws AssertionException
    {
        JoinMemory memory = workingMemory.getJoinMemory( this );
        Set joinedTuples = memory.addLeftTuple( tuple );

        if ( joinedTuples.isEmpty() )
        {
            return;
        }

        propagateAssertTuples( joinedTuples,
                               workingMemory );
    }

    /** Assert a new <code>Tuple</code> from the right input.
     *
     *  @param tuple The <code>Tuple</code> being asserted.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws AssertionException If an error occurs while asserting.
     */
    void assertRightTuple(ReteTuple tuple,
                          WorkingMemoryImpl workingMemory) throws AssertionException
    {
        JoinMemory memory = workingMemory.getJoinMemory( this );
        Set joinedTuples = memory.addRightTuple( tuple );

        if ( joinedTuples.isEmpty() )
        {
            return;
        }

        propagateAssertTuples( joinedTuples,
                               workingMemory );
    }

    /** Retrieve the node input for the left side.
     *
     *  @return The node input for the left side.
     */
    JoinNodeInput getLeftNodeInput()
    {
        return new JoinNodeInput( this,
                                  JoinNodeInput.LEFT );
    }

    /** Retrieve the node input for the right side.
     *
     *  @return The node input for the right side.
     */
    JoinNodeInput getRightNodeInput()
    {
        return new JoinNodeInput( this,
                                  JoinNodeInput.RIGHT );
    }

    /** Retract tuples.
     *
     *  @param key The tuple key.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws RetractionException If an error occurs while retracting.
     */
    public void retractTuples(TupleKey key,
                              WorkingMemoryImpl workingMemory) throws RetractionException
    {
        JoinMemory memory = workingMemory.getJoinMemory( this );

        memory.retractTuples( key );

        propagateRetractTuples( key,
                                workingMemory );
    }

    /** Modify tuples from the left input.
     *
     *  @param trigger The root fact object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while modifying.
     */
    void modifyLeftTuples(FactHandle trigger,
                          TupleSet newTuples,
                          WorkingMemoryImpl workingMemory) throws FactException
    {
        JoinMemory memory = workingMemory.getJoinMemory( this );

        memory.modifyLeftTuples( trigger,
                                 newTuples,
                                 this,
                                 workingMemory );
    }

    /** Modify tuples from the right input.
     *
     *  @param trigger The root fact object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while modifying.
     */
    void modifyRightTuples(FactHandle trigger,
                           TupleSet newTuples,
                           WorkingMemoryImpl workingMemory) throws FactException
    {
        JoinMemory memory = workingMemory.getJoinMemory( this );

        memory.modifyRightTuples( trigger,
                                  newTuples,
                                  this,
                                  workingMemory );
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

    /** Return a string representation of this object.
     *
     * @return a String
     */
    public String toString()
    {
      return "[JoinNodeImpl: leftInput=" + leftInput + ",rightInput=" + rightInput + ",commonDeclarations=" + commonDeclarations + "]";
    }
}
