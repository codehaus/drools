package org.drools.reteoo;

/*
 * $Id: JoinMemory.java,v 1.34 2004-11-16 09:10:49 simon Exp $
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
import org.drools.rule.Declaration;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Memory for left and right inputs of a <code>JoinNode</code>.
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 * @see ReteTuple
 */
class JoinMemory implements Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Left-side tuples. */
    private final TupleSet leftTuples;

    /** Right-side tuples. */
    private final TupleSet rightTuples;

    /** Join column declarations. */
    private final Set joinDeclarations;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param commonDeclarations
     */
    JoinMemory( Set commonDeclarations )
    {
        this.leftTuples = new TupleSet();
        this.rightTuples = new TupleSet();
        this.joinDeclarations = commonDeclarations;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /**
     * Retract tuples from this memory.
     *
     * @param key The key for the tuples to be removed.
     */
    void retractTuples( TupleKey key )
    {
        retractTuples( key, this.leftTuples );

        retractTuples( key, this.rightTuples );
    }

    /**
     * Retract tuples from this memory.
     *
     * @param key       The key for the tuples to be removed.
     * @param tupleSet  The tuples to be removed.
     */
    private void retractTuples( TupleKey key, TupleSet tupleSet )
    {
        Iterator tupleIter = tupleSet.iterator( );
        while ( tupleIter.hasNext() )
        {
            if ( ( ( ReteTuple ) tupleIter.next() ).getKey().containsAll( key ) )
            {
                tupleIter.remove();
            }
        }
    }

    /**
     * Add a <code>ReteTuple</code> received from the <code>JoinNode's</code> left input to the left side of this
     * memory, and attempt to join to existing <code>Tuples</code> in the right side.
     *
     * @param tuple The <code>Tuple</code> to add to the left side memory.
     * @return A <code>List</code> of <code>Tuples</code> successfully created by joining the incoming
     *         <code>tuple</code> against existing <code>Tuples</code> on the right side memory.
     * @see JoinNode
     * @see ReteTuple
     */
    Set addLeftTuple( ReteTuple tuple )
    {
        this.leftTuples.addTuple( tuple );

        return attemptJoin( tuple, this.rightTuples );
    }

    /**
     * Add a <code>ReteTuple</code> received from the <code>JoinNode's</code> right input to the right side of this
     * memory, and attempt to join to existing <code>Tuples</code> in the left side.
     *
     * @param tuple The <code>Tuple</code> to add to the right side memory.
     * @return A <code>List</code> of <code>Tuples</code> successfully created by joining the incoming
     *         <code>tuple</code> against existing <code>Tuples</code> on the left side memory.
     * @see JoinNode
     * @see ReteTuple
     */
    Set addRightTuple( ReteTuple tuple )
    {
        this.rightTuples.addTuple( tuple );

        return attemptJoin( tuple, this.leftTuples );
    }

    /**
     * Attempt to join the <code>tuple</code> against the tuples available through the <code>tupleIterator</code>.
     *
     * @param tuple     The <code>Tuple</code> to attempt joining.
     * @param tupleSet  The Tuples to attempt joining to the parameter.
     * @return A possibly empty <code>Set</code> of joined <code>Tuples</code>.
     */
    private Set attemptJoin( ReteTuple tuple, TupleSet tupleSet )
    {
        Set joinedTuples = Collections.EMPTY_SET;

        ReteTuple eachTuple;
        ReteTuple joinedTuple;

        Iterator tupleIter = tupleSet.iterator( );
        while ( tupleIter.hasNext() )
        {
            eachTuple = ( ReteTuple ) tupleIter.next( );

            joinedTuple = attemptJoin( tuple, eachTuple );

            if ( joinedTuple != null )
            {
                if ( joinedTuples == Collections.EMPTY_SET )
                {
                    joinedTuples = new HashSet();
                }

                joinedTuples.add( joinedTuple );
            }
        }

        return joinedTuples;
    }

    private ReteTuple attemptJoin( ReteTuple left, ReteTuple right )
    {
        Iterator declIter = this.joinDeclarations.iterator( );
        Declaration eachDecl;

        FactHandle leftHandle;
        FactHandle rightHandle;

        while ( declIter.hasNext() )
        {
            eachDecl = ( Declaration ) declIter.next();

            if ( !checkExtractorJoinsOk( eachDecl, left, right ) )
            {
                return null;
            }

            leftHandle = left.getKey().get( eachDecl );
            rightHandle = right.getKey().get( eachDecl );

            if ( leftHandle == null && rightHandle == null )
            {
                continue;
            }

            if ( leftHandle == null || rightHandle == null )
            {
                return null;
            }

            if ( leftHandle.equals( rightHandle ) )
            {
                continue;
            }
            else
            {
                return null;
            }
        }

        return new ReteTuple( left, right );

    }

    /**
     * For targets shared by extractors the extracted fact should be the same. If the given declaration is a
     * targetDeclaration for one it must be for the other, as its a common declaration.
     *
     * @param decl
     * @param left
     * @param right
     * @return
     */
    private boolean checkExtractorJoinsOk( Declaration decl, ReteTuple left, ReteTuple right )
    {
        Object leftValue = left.get( decl );
        Object rightValue = right.get( decl );

        if ( leftValue == null )
        {
            return rightValue == null;
        }

        return leftValue.equals( rightValue );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Produce debug string.
     *
     * @return The debug string.
     */
    public String toString()
    {
        return "[JoinMemory \n\tleft=" + this.leftTuples + "\n\tright="
                + this.rightTuples + "]";
    }
}