package org.drools.reteoo;

/*
 * $Id: JoinNode.java,v 1.45 2005-05-08 16:13:33 memelet Exp $
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.drools.AssertionException;
import org.drools.RetractionException;

/**
 * A two-input Rete-OO <i>join node </i>.
 *
 * @see org.drools.reteoo.TupleSource
 * @see org.drools.reteoo.TupleSink
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
class JoinNode extends TupleSource
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The left input <code>TupleSource</code>. */
    private final TupleSource leftInput;

    /** The right input <code>TupleSource</code>. */
    private final TupleSource rightInput;

    /** <code>Declarations</code> in both left and right input sources combined. */
    private final Set tupleDeclarations;

    /** <code>Declarations</code> common to both left and right input sources. */
    private final Set commonDeclarations;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param leftInput The left input <code>TupleSource</code>.
     * @param rightInput The right input <code>TupleSource</code>.
     */
    public JoinNode( TupleSource leftInput,
                     TupleSource rightInput )
    {
        this.leftInput = leftInput;
        this.rightInput = rightInput;
        this.tupleDeclarations = determineTupleDeclarations( );
        this.commonDeclarations = determineCommonDeclarations( );
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Set up the <code>Set</code> of common <code>Declarations</code>
     * across the two input <code>TupleSources</code>.
     */
    private Set determineCommonDeclarations()
    {
        Set commonDeclarations = new HashSet( this.leftInput.getTupleDeclarations( ) );

        commonDeclarations.retainAll( this.rightInput.getTupleDeclarations( ) );

        return commonDeclarations.isEmpty( ) ? Collections.EMPTY_SET : Collections.unmodifiableSet( commonDeclarations );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // org.drools.reteoo.JoinNode
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Retrieve the set of common <code>Declarations</code> across the two
     * input <code>TupleSources</code>.
     *
     * @return The <code>Set</code> of common <code>Declarations</code>.
     */
    public Set getCommonDeclarations()
    {
        return this.commonDeclarations;
    }

    /**
     * Propagate joined asserted tuples.
     *
     * @param joinedTuples The tuples to propagate.
     * @param workingMemory The working memory session.
     * @throws AssertionException If an errors occurs while asserting.
     */
    private void propagateAssertTuples( TupleSet joinedTuples,
                                        WorkingMemoryImpl workingMemory ) throws AssertionException
    {
        Iterator tupleIter = joinedTuples.iterator( );
        while ( tupleIter.hasNext( ) )
        {
            propagateAssertTuple( ( ReteTuple ) tupleIter.next( ),
                                  workingMemory );
        }
    }

    /**
     * Assert a new <code>Tuple</code> from the left input.
     *
     * @param tuple The <code>Tuple</code> being asserted.
     * @param workingMemory The working memory seesion.
     * @throws AssertionException If an error occurs while asserting.
     */
    void assertLeftTuple( ReteTuple tuple,
                          WorkingMemoryImpl workingMemory ) throws AssertionException
    {
        TupleSet joinedTuples = workingMemory.getJoinMemory( this ).addLeftTuple( tuple );

        if ( !joinedTuples.isEmpty( ) )
        {
            propagateAssertTuples( joinedTuples,
                                   workingMemory );
        }
    }

    /**
     * Assert a new <code>Tuple</code> from the right input.
     *
     * @param tuple The <code>Tuple</code> being asserted.
     * @param workingMemory The working memory seesion.
     * @throws AssertionException If an error occurs while asserting.
     */
    void assertRightTuple( ReteTuple tuple,
                           WorkingMemoryImpl workingMemory ) throws AssertionException
    {
        TupleSet joinedTuples = workingMemory.getJoinMemory( this ).addRightTuple( tuple );

        if ( !joinedTuples.isEmpty( ) )
        {
            propagateAssertTuples( joinedTuples,
                                   workingMemory );
        }
    }

    /**
     * Retract tuples.
     *
     * @param key The tuple key.
     * @param workingMemory The working memory seesion.
     * @throws RetractionException If an error occurs while retracting.
     */
    public void retractTuples( TupleKey key,
                               WorkingMemoryImpl workingMemory ) throws RetractionException
    {
        if ( workingMemory.getJoinMemory( this ).removeTuples( key ) )
        {
            propagateRetractTuples( key,
                                    workingMemory );
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // org.drools.reteoo.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Retrieve the <code>Set</code> of <code>Declaration</code>s. in the
     * propagated <code>Tuples</code>.
     *
     * @see Declaration
     *
     * @return The <code>Set</code> of <code>Declarations</code> in progated
     *         <code>Tuples</code>.
     */
    public Set getTupleDeclarations()
    {
        return this.tupleDeclarations;
    }

    public void attach()
    {
        leftInput.addTupleSink( new JoinNodeInput( this,
                                                   JoinNodeInput.LEFT ) );

        rightInput.addTupleSink( new JoinNodeInput( this,
                                                    JoinNodeInput.RIGHT ) );
    }

    private Set determineTupleDeclarations()
    {
        Set decls = new HashSet( leftInput.getTupleDeclarations( ) );

        decls.addAll( rightInput.getTupleDeclarations( ) );

        return Collections.unmodifiableSet( decls );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public String toString()
    {
        return "[JoinNode: common=" + this.commonDeclarations + "; decls=" + this.tupleDeclarations + "]";
    }

    public int hashCode()
    {
        return this.leftInput.hashCode( ) ^ this.rightInput.hashCode( );
    }

    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null || getClass( ) != object.getClass( ) )
        {
            return false;
        }

        JoinNode other = ( JoinNode ) object;

        return this.leftInput.equals( other.leftInput ) && this.rightInput.equals( other.rightInput );
    }
}
