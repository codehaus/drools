package org.drools.reteoo;

/*
 $Id: JoinMemory.java,v 1.19 2003-12-05 04:26:23 bob Exp $

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
import org.drools.rule.Declaration;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;

/** Memory for left and right inputs of a <code>JoinNode</code>.
 *
 *  @see ReteTuple
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class JoinMemory
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Left-side tuples. */
    private TupleSet leftTuples;

    /** Right-side tuples. */
    private TupleSet rightTuples;

    /** Join column declarations. */
    private Set joinDeclarations;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param node The <code>JoinNode</code> this memory
     *         is for.
     */
    JoinMemory(JoinNode node)
    {
        this.leftTuples  = new TupleSet();
        this.rightTuples = new TupleSet();

        this.joinDeclarations = node.getCommonDeclarations();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retract an object from this memory.
     *
     *  @param handle The handle for the fact to retract.
     */
    protected void retractObject(FactHandle handle)
    {
        try
        {
            ReteTuple eachTuple = null;
            
            Iterator  tupleIter = leftTuples.iterator();
            
            while ( tupleIter.hasNext() )
            {
                eachTuple = (ReteTuple) tupleIter.next();
                
                if ( eachTuple.dependsOn( handle ) )
                {
                    tupleIter.remove();
                }
            }
            
            tupleIter = rightTuples.iterator();
            
            while ( tupleIter.hasNext() )
            {
                eachTuple = (ReteTuple) tupleIter.next();
                
                if ( eachTuple.dependsOn( handle ) )
                {
                    tupleIter.remove();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /** Retract tuples from this memory.
     *
     *  @param keys The keys for the tuples to be removed.
     */
    protected void retractTuples(Set keys)
    {
        Iterator keyIter = keys.iterator();
        TupleKey eachKey = null;

        while ( keyIter.hasNext() )
        {
            eachKey = (TupleKey) keyIter.next();

            retractTuples( eachKey );
        }
    }

    /** Retract tuples from this memory.
     *
     *  @param key The key for the tuples to be removed.
     */
    protected void retractTuples(TupleKey key)
    {
        retractTuples( key,
                       getLeftTuples().iterator() );

        retractTuples( key,
                       getRightTuples().iterator() );
    }

    /** Retract tuples from this memory.
     *
     *  @param key The key for the tuples to be removed.
     *  @param tupleIter Iterator of tuples to be removed.
     */
    private void retractTuples(TupleKey key,
                               Iterator tupleIter)
    {
        ReteTuple eachTuple = null;
        
        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();
            
            if ( eachTuple.getKey().containsAll( key ) )
            {
                tupleIter.remove();
            }
        }
    }

    /** Modify tuples on the left-side.
     *
     *  @param trigger Triggering object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param joinNode This memory's join node.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException if an error occurs during modification.
     */
    protected void modifyLeftTuples(FactHandle trigger,
                                    TupleSet newTuples,
                                    JoinNode joinNode,
                                    WorkingMemoryImpl workingMemory) throws FactException
    {
        modifyTuples( trigger,
                      newTuples,
                      getLeftTuples(),
                      getRightTuples(),
                      joinNode,
                      workingMemory  );
    }

    /** Modify tuples on the right-side.
     *
     *  @param trigger Triggering object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param joinNode This memory's join node.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException if an error occurs during modification.
     */
    protected void modifyRightTuples(FactHandle trigger,
                                     TupleSet newTuples,
                                     JoinNode joinNode,
                                     WorkingMemoryImpl workingMemory) throws FactException
    {
        modifyTuples( trigger,
                      newTuples,
                      getRightTuples(),
                      getLeftTuples(),
                      joinNode,
                      workingMemory );
    }

    /** Modify tuples 
     *
     *  @param trigger Triggering object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param thisSideTuples The tuples on the side that's receiving
     *         the modifications.
     *  @param thatSideTuples The tuples on the side that's <b>not</b>
     *         receiving the modifications.
     *  @param joinNode This memory's join node.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException if an error occurs during modification.
     */
    protected void modifyTuples(FactHandle trigger,
                                TupleSet newTuples,
                                TupleSet thisSideTuples,
                                TupleSet thatSideTuples,
                                JoinNode joinNode,
                                WorkingMemoryImpl workingMemory) throws FactException
    {
        Set origModified   = new HashSet();
        Set newModified    = new HashSet();

        for ( Iterator tupleIter = thisSideTuples.iterator();
              tupleIter.hasNext(); )
        {
            ReteTuple origTuple = (ReteTuple) tupleIter.next();

            if ( origTuple.dependsOn( trigger ) )
            {
                ReteTuple newTuple  = newTuples.getTuple( origTuple.getKey() );
                
                if ( newTuple == null )
                {
                    tupleIter.remove();
                }
                else
                {
                    newModified.add( newTuple );
                    origModified.add( origTuple );
                }
            }
        }

        newModified.addAll( newTuples.getTuples() );

        TupleSet origJoined = new TupleSet();

        for ( Iterator tupleIter = origModified.iterator();
              tupleIter.hasNext(); )
        {
            ReteTuple eachTuple = (ReteTuple) tupleIter.next();

            origJoined.addAllTuples( attemptJoin( eachTuple,
                                                  thatSideTuples.iterator() ) );
        }

        TupleSet newJoined = new TupleSet();

        for ( Iterator tupleIter = newModified.iterator();
              tupleIter.hasNext(); )
        {
            ReteTuple eachTuple = (ReteTuple) tupleIter.next();
            
            newJoined.addAllTuples( attemptJoin( eachTuple,
                                                 thatSideTuples.iterator() ) );
        }

        thisSideTuples.addAllTuples( newTuples );

        joinNode.propagateModifyTuples( trigger,
                                        newJoined,
                                        workingMemory );
    }

    /** Propagate retractions.
     *
     *  @param trigger The retracted trigger object.
     *  @param retractedKeys Keys to the retracted tuples.
     *  @param joinNode This memory's join node.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException if an error occurs during modification.
     */
    private void propagateRetractTuples(Object trigger,
                                        Set retractedKeys,
                                        JoinNode joinNode, 
                                        WorkingMemoryImpl workingMemory) throws FactException
    {
        Iterator keyIter = retractedKeys.iterator();
        TupleKey eachKey = null;

        while ( keyIter.hasNext() )
        {
            eachKey = (TupleKey) keyIter.next();

            joinNode.propagateRetractTuples( eachKey,
                                             workingMemory );
        }
        
    }

    /** Add a <code>ReteTuple</code> received from the <code>JoinNode's</code>
     *  left input to the left side of this memory, and attempt
     *  to join to existing <code>Tuples</code> in the right
     *  side.
     *
     *  @see JoinNode
     *  @see ReteTuple
     *
     *  @param tuple The <code>Tuple</code> to add to the left
     *         side memory.
     *
     *  @return A <code>List</code> of <code>Tuples</code> successfully
     *          created by joining the incoming <code>tuple</code>
     *          against existing <code>Tuples</code> on the right
     *          side memory.
     */
    protected Set addLeftTuple(JoinNode node,
                               ReteTuple tuple)
    {
        this.leftTuples.addTuple( tuple );


        Set joined = attemptJoin( tuple,
                                  getRightTupleIterator() );

        return joined;
    }

    /** Retrieve the <code>List</code> of <code>Tuples</code>
     *  held in the left side memory.
     *
     *  @return The <code>List</code> of <code>Tuples</code>
     *          help in the left side memory.
     */
    protected TupleSet getLeftTuples()
    {
        return this.leftTuples;
    }

    /** Retrieve an <code>Iterator</code> over the <code>Tuples</code>
     *  held in the left side memory.
     *
     *  @return An <code>Iterator</code> over the <code>Tuples</code>
     *          help in the left side memory.
     */
    protected Iterator getLeftTupleIterator()
    {
        return this.leftTuples.iterator();
    }

    /** Add a <code>ReteTuple</code> received from the <code>JoinNode's</code>
     *  right input to the right side of this memory, and attempt
     *  to join to existing <code>Tuples</code> in the left
     *  side.
     *
     *  @see JoinNode
     *  @see ReteTuple
     *
     *  @param tuple The <code>Tuple</code> to add to the right
     *         side memory.
     *
     *  @return A <code>List</code> of <code>Tuples</code> successfully
     *          created by joining the incoming <code>tuple</code>
     *          against existing <code>Tuples</code> on the left
     *          side memory.
     */
    protected Set addRightTuple(JoinNode node,
                                ReteTuple tuple)
    {
        this.rightTuples.addTuple( tuple );

        return attemptJoin( tuple,
                            getLeftTupleIterator() );
    }

    /** Retrieve the <code>List</code> of <code>Tuples</code>
     *  held in the right side memory.
     *
     *  @return The <code>List</code> of <code>Tuples</code>
     *          help in the right side memory.
     */
    protected TupleSet getRightTuples()
    {
        return this.rightTuples;
    }

    /** Retrieve an <code>Iterator</code> over the <code>Tuples</code>
     *  held in the right side memory.
     *
     *  @return An <code>Iterator</code> over the <code>Tuples</code>
     *          help in the right side memory.
     */
    protected Iterator getRightTupleIterator()
    {
        return this.rightTuples.iterator();
    }

    /** Retrieve an <code>Iterator</code> over the common
     *  <code>Declarations</code> used to join <code>Tuples</code>
     *  from the left and right side memories.
     *
     *  @return An <code>Iterator</code> of common join
     *          <code>Declarations</code>.
     */
    protected Iterator getJoinDeclarationIterator()
    {
        return this.joinDeclarations.iterator();
    }

    /** Attempt to join the <code>tuple</code> against the
     *  tuples available through the <code>tupleIterator</code>.
     *
     *  @param tuple The <code>Tuple</code> to attempt joining.
     *  @param tupleIter The <code>Iterator</code> over
     *         <code>Tuples</code> to attempt joining to the
     *         <code>tuple</code> parameter.
     *
     *  @return A possibly empty <code>List</code> of joined
     *         <code>Tuples</code>.
     */
    protected Set attemptJoin(ReteTuple tuple,
                              Iterator tupleIter)
    {
        Set joinedTuples = Collections.EMPTY_SET;

        ReteTuple eachTuple   = null;
        ReteTuple joinedTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();
            
            joinedTuple = attemptJoin( tuple,
                                       eachTuple );

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

    /** Produce debug string.
     *
     *  @return The debug string.
     */ 
    protected ReteTuple attemptJoin(ReteTuple left,
                                    ReteTuple right)
    {
        Iterator    declIter = getJoinDeclarationIterator();
        Declaration eachDecl = null;

        FactHandle leftHandle  = null;
        FactHandle rightHandle = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            leftHandle  = left.getKey().get( eachDecl );
            rightHandle = right.getKey().get( eachDecl );

            if ( leftHandle == null
                 &&
                 rightHandle == null )
            {
                continue;
            }

            if ( leftHandle == null
                 ||
                 rightHandle == null )
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

        ReteTuple joinedTuple = new JoinTuple( left,
                                               right );

        return joinedTuple;

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Produce debug string.
     *
     *  @return The debug string.
     */ 
    public String toString()
    {
        return "[JoinMemory \n\tleft=" + this.leftTuples + "\n\tright=" + this.rightTuples + "]";
    }

}
