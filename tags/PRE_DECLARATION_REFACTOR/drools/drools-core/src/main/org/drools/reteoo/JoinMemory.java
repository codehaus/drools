package org.drools.reteoo;

/*
 * $Id: JoinMemory.java,v 1.32 2004-11-09 09:03:35 simon Exp $
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
     * Retract an object from this memory.
     *
     * @param handle The handle for the fact to retract.
     */
    protected void retractObject( FactHandle handle )
    {
        try
        {
            Iterator tupleIter = leftTuples.iterator();

            while ( tupleIter.hasNext() )
            {
                if ( ( ( ReteTuple ) tupleIter.next() ).dependsOn( handle ) )
                {
                    tupleIter.remove();
                }
            }

            tupleIter = rightTuples.iterator();

            while ( tupleIter.hasNext() )
            {
                if ( ( ( ReteTuple ) tupleIter.next() ).dependsOn( handle ) )
                {
                    tupleIter.remove();
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Retract tuples from this memory.
     *
     * @param keys The keys for the tuples to be removed.
     */
    protected void retractTuples( Set keys )
    {
        Iterator keyIter = keys.iterator();

        while ( keyIter.hasNext() )
        {
            retractTuples( ( TupleKey ) keyIter.next() );
        }
    }

    /**
     * Retract tuples from this memory.
     *
     * @param key The key for the tuples to be removed.
     */
    protected void retractTuples( TupleKey key )
    {
        retractTuples( key, getLeftTuples().iterator() );

        retractTuples( key, getRightTuples().iterator() );
    }

    /**
     * Retract tuples from this memory.
     *
     * @param key       The key for the tuples to be removed.
     * @param tupleIter Iterator of tuples to be removed.
     */
    private void retractTuples( TupleKey key, Iterator tupleIter )
    {

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
    protected Set addLeftTuple( ReteTuple tuple )
    {
        this.leftTuples.addTuple( tuple );

        return attemptJoin( tuple, getRightTupleIterator() );
    }

    /**
     * Retrieve the <code>List</code> of <code>Tuples</code> held in the left side memory.
     *
     * @return The <code>List</code> of <code>Tuples</code> help in the left side memory.
     */
    protected TupleSet getLeftTuples()
    {
        return this.leftTuples;
    }

    /**
     * Retrieve an <code>Iterator</code> over the <code>Tuples</code> held in the left side memory.
     *
     * @return An <code>Iterator</code> over the <code>Tuples</code> help in the left side memory.
     */
    protected Iterator getLeftTupleIterator()
    {
        return this.leftTuples.iterator();
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
    protected Set addRightTuple( ReteTuple tuple )
    {
        this.rightTuples.addTuple( tuple );

        return attemptJoin( tuple, getLeftTupleIterator() );
    }

    /**
     * Retrieve the <code>List</code> of <code>Tuples</code> held in the right side memory.
     *
     * @return The <code>List</code> of <code>Tuples</code> help in the right side memory.
     */
    protected TupleSet getRightTuples()
    {
        return this.rightTuples;
    }

    /**
     * Retrieve an <code>Iterator</code> over the <code>Tuples</code> held in the right side memory.
     *
     * @return An <code>Iterator</code> over the <code>Tuples</code> help in the right side memory.
     */
    protected Iterator getRightTupleIterator()
    {
        return this.rightTuples.iterator();
    }

    /**
     * Retrieve an <code>Iterator</code> over the common <code>Declarations</code> used to join <code>Tuples</code> from
     * the left and right side memories.
     *
     * @return An <code>Iterator</code> of common join <code>Declarations</code>.
     */
    protected Iterator getJoinDeclarationIterator()
    {
        return this.joinDeclarations.iterator();
    }

    /**
     * Attempt to join the <code>tuple</code> against the tuples available through the <code>tupleIterator</code>.
     *
     * @param tuple     The <code>Tuple</code> to attempt joining.
     * @param tupleIter The <code>Iterator</code> over <code>Tuples</code> to attempt joining to the <code>tuple</code>
     *                  parameter.
     * @return A possibly empty <code>List</code> of joined <code>Tuples</code>.
     */
    protected Set attemptJoin( ReteTuple tuple, Iterator tupleIter )
    {
        Set joinedTuples = Collections.EMPTY_SET;

        ReteTuple eachTuple;
        ReteTuple joinedTuple;

        while ( tupleIter.hasNext() )
        {
            eachTuple = ( ReteTuple ) tupleIter.next();

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

    /**
     * Produce debug string.
     *
     * @return The debug string.
     */
    protected ReteTuple attemptJoin( ReteTuple left, ReteTuple right )
    {
        Iterator declIter = getJoinDeclarationIterator();
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

        return new JoinTuple( left, right );

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
    boolean checkExtractorJoinsOk( Declaration decl, ReteTuple left, ReteTuple right )
    {
        Set leftTargetDecls = left.getDeclarations();
        Set rightTargetDecls = right.getDeclarations();

        if ( leftTargetDecls != null && leftTargetDecls.contains( decl ) )
        {
            if ( !rightTargetDecls.contains( decl ) || !left.get( decl ).equals( right.get( decl ) ) )
            {
                return false;
            }
        }
        else if ( rightTargetDecls != null && rightTargetDecls.contains( decl ) )
        {
            if ( !leftTargetDecls.contains( decl ) || !right.get( decl ).equals( left.get( decl ) ) )
            {
                return false;
            }
        }
        return true;
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