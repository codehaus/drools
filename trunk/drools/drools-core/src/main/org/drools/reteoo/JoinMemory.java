package org.drools.reteoo;

/*
 * $Id: JoinMemory.java,v 1.46 2004-11-29 06:28:56 simon Exp $
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

import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.rule.Declaration;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * Memory for left and right inputs of a <code>JoinNode</code>.
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 * @see ReteTuple
 */
class JoinMemory
    implements
    Serializable
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** Left-side tuples. */
    private final TupleSet leftTuples;

    /** Right-side tuples. */
    private final TupleSet rightTuples;

    /** Tuple column declarations. */
    private final Set tupleDeclarations;

    /** Join column declarations. */
    private final Set commonDeclarations;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param tupleDeclarations
     * @param commonDeclarations
     */
    JoinMemory(Set tupleDeclarations, Set commonDeclarations)
    {
        this.leftTuples = new TupleSet( );
        this.rightTuples = new TupleSet( );
        this.tupleDeclarations = tupleDeclarations;
        this.commonDeclarations = commonDeclarations;
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Retract tuples from this memory.
     *
     * @param key The key for the tuples to be removed.
     * @return <code>true</code> if at least one tuple was removed; <code>false</code> otherwise.
     */
    boolean retractTuples(TupleKey key)
    {
        return this.leftTuples.removeAllTuples( key )
               || this.rightTuples.removeAllTuples( key );
    }

    /**
     * Add a <code>ReteTuple</code> received from the <code>JoinNode's</code>
     * left input to the left side of this memory, and attempt to join to
     * existing <code>Tuples</code> in the right side.
     *
     * @param tuple The <code>Tuple</code> to add to the left side memory.
     * @return A <code>Set</code> of <code>Tuples</code> successfully
     *         created by joining the incoming <code>tuple</code> against
     *         existing <code>Tuples</code> on the right side memory.
     * @see JoinNode
     * @see ReteTuple
     */
    TupleSet addLeftTuple(ReteTuple tuple)
    {
        this.leftTuples.addTuple( tuple );

        return attemptJoin( tuple,
                            this.rightTuples );
    }

    /**
     * Add a <code>ReteTuple</code> received from the <code>JoinNode's</code>
     * right input to the right side of this memory, and attempt to join to
     * existing <code>Tuples</code> in the left side.
     *
     * @param tuple The <code>Tuple</code> to add to the right side memory.
     * @return A <code>Set</code> of <code>Tuples</code> successfully
     *         created by joining the incoming <code>tuple</code> against
     *         existing <code>Tuples</code> on the left side memory.
     * @see JoinNode
     * @see ReteTuple
     */
    TupleSet addRightTuple(ReteTuple tuple)
    {
        this.rightTuples.addTuple( tuple );

        return attemptJoin( tuple,
                            this.leftTuples );
    }

    /**
     * Retrieve the <code>List</code> of <code>Tuples</code> held in the
     * left side memory.
     *
     * @return The <code>List</code> of <code>Tuples</code> help in the left
     *         side memory.
     */
    protected TupleSet getLeftTuples()
    {
        return this.leftTuples;
    }

    /**
     * Retrieve the <code>List</code> of <code>Tuples</code> held in the
     * right side memory.
     *
     * @return The <code>List</code> of <code>Tuples</code> help in the
     *         right side memory.
     */
    protected TupleSet getRightTuples()
    {
        return this.rightTuples;
    }

    /**
     * Modify tuples on the left-side.
     *
     * @param trigger Triggering object handle.
     * @param workingMemory The working memory session.
     * @return The newly joined tuples.
     * @throws FactException if an error occurs during modification.
     */

    protected TupleSet modifyLeftTuples(FactHandle trigger,
                                        TupleSet newTuples,
                                        WorkingMemoryImpl workingMemory) throws FactException
    {
        return modifyTuples( trigger,
                             newTuples,
                             getLeftTuples( ),
                             getRightTuples( ),
                             workingMemory );
    }

    /**
     * Modify tuples on the right-side.
     *
     * @param trigger
     *            Triggering object handle.
     * @param workingMemory
     *            The working memory session.
     * @return The newly joined tuples.
     * @throws FactException
     *             if an error occurs during modification.
     */
    protected TupleSet modifyRightTuples(FactHandle trigger,
                                         TupleSet newTuples,
                                         WorkingMemoryImpl workingMemory) throws FactException
    {
        return modifyTuples( trigger,
                             newTuples,
                             getRightTuples( ),
                             getLeftTuples( ),
                             workingMemory );
    }

    /**
     * Modify tuples
     *
     * @param trigger Triggering object handle.
     * @param thisSideTuples The tuples on the side that's receiving the modifications.
     * @param thatSideTuples The tuples on the side that's <b>not </b> receiving the modifications.
     * @param workingMemory The working memory session.
     * @return The newly joined tuples.
     * @throws FactException if an error occurs during modification.
     */
    protected TupleSet modifyTuples( FactHandle trigger,
                                     TupleSet newTuples,
                                     TupleSet thisSideTuples,
                                     TupleSet thatSideTuples,
                                     WorkingMemoryImpl workingMemory ) throws FactException
    {
        ReteTuple tuple;
        TupleSet newJoined = new TupleSet( );

        Iterator tupleIter = thisSideTuples.iterator( );

        while ( tupleIter.hasNext( ) )
        {
            tuple = (ReteTuple) tupleIter.next( );

            if ( tuple.dependsOn( trigger ) )
            {
                attemptJoin( tuple,
                             thatSideTuples,
                             newJoined );
            }
        }

        return newJoined;

    }

    /**
     * Attempt to join a given <code>tuple</code> against all tuples in a specified <code>TupleSet</code>.
     *
     * @param tuple The <code>Tuple</code> to attempt joining.
     * @param tupleSet The Tuples to attempt joining to the parameter.
     * @return The newly joined tuples.
     */
    private TupleSet attemptJoin( ReteTuple tuple,
                                  TupleSet tupleSet )
    {
        TupleSet newJoined = new TupleSet( );

        attemptJoin( tuple,
                     tupleSet,
                     newJoined );

        return newJoined;
    }

    /**
     * Attempt to join a given <code>tuple</code> against all tuples in a specified <code>TupleSet</code>.
     *
     * @param tuple The <code>Tuple</code> to attempt joining.
     * @param tupleSet The Tuples to attempt joining to the parameter.
     * @param newJoined The <code>TupleSet</code> into which newly joined tuples will be added.
     */
    private void attemptJoin( ReteTuple tuple,
                              TupleSet tupleSet,
                              TupleSet newJoined )
    {
        Iterator tupleIter = tupleSet.iterator( );
        while ( tupleIter.hasNext( ) )
        {
            attemptJoin( tuple,
                         ( ReteTuple ) tupleIter.next( ),
                         newJoined );
        }
    }

    private void attemptJoin( ReteTuple left,
                              ReteTuple right,
                              TupleSet newJoined )
    {
        Iterator declIter = this.commonDeclarations.iterator( );
        Declaration eachDecl;

        while ( declIter.hasNext( ) )
        {
            eachDecl = (Declaration) declIter.next( );

            if ( !left.getKey( ).get( eachDecl ).equals( right.getKey( ).get( eachDecl ) ) )
            {
                return;
            }
        }

        newJoined.addTuple( new ReteTuple( left,
                                           right ) );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Produce debug string.
     *
     * @return The debug string.
     */
    public String toString()
    {
        return "[JoinMemory \n\tleft=" + this.leftTuples + "\n\tright=" + this.rightTuples + "]";
    }

    public void dump()
    {
        System.err.println( "----" );
        ReteTuple tuple;
        Set tuples = this.leftTuples.getTuples( );
        Iterator it1 = tuples.iterator( );
        while ( it1.hasNext( ) )
        {
            tuple = (ReteTuple) it1.next( );
            System.err.println( "tuple" );
            Set decls = this.tupleDeclarations;
            Iterator it2 = decls.iterator( );
            while ( it2.hasNext( ) )
            {
                Declaration decl = (Declaration) it2.next( );
                Object object = tuple.get( decl );
                TupleKey key = tuple.getKey( );
                System.err.println( "dump memory leftTuples:" + key.get( decl ) + ":" + decl + ":" + object );
            }
        }

        tuples = this.rightTuples.getTuples( );
        it1 = tuples.iterator( );
        while ( it1.hasNext( ) )
        {
            tuple = (ReteTuple) it1.next( );
            Set decls = this.tupleDeclarations;
            Iterator it2 = decls.iterator( );
            while ( it2.hasNext( ) )
            {
                Declaration decl = (Declaration) it2.next( );
                Object object = tuple.get( decl );
                TupleKey key = tuple.getKey( );
                System.err.println( "dump memory rightTuples:" + key.get( decl ) + ":" + decl + ":" + object );
            }
        }
        System.err.println( "----" );
    }
}