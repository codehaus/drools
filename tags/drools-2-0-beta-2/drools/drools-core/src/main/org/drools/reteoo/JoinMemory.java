
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.FactException;
import org.drools.spi.Declaration;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;

/** Memory for left and right inputs of a {@link JoinNode}.
 *
 *  @see JoinNode
 *  @see org.drools.spi.Tuple
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class JoinMemory
{
    /** <code>List</code> of <code>Tuples</code> received
     *  on the left input of the <code>JoinNode</code>.
     */
    private TupleSet leftTuples;

    /** <code>List</code> of <code>Tuples</code> received
     *  on the right input of the <code>JoinNode</code>.
     */
    private TupleSet rightTuples;

    /** The <code>Set</code> of common <code>Declarations</code>
     *  to join against left and right memories.
     */
    private Set joinDeclarations;

    /** Construct.
     *
     *  @param node The <code>JoinNode</code> this memory
     *         is for.
     */
    public JoinMemory(JoinNode node)
    {
        this.leftTuples  = new TupleSet();
        this.rightTuples = new TupleSet();

        this.joinDeclarations = node.getCommonDeclarations();
    }

    /** Produce output suitable for debugging.
     */ 
    public String toString()
    {
        return "[JoinMemory \n\tleft=" + this.leftTuples + "\n\tright=" + this.rightTuples + "]";
    }

    protected void retractObject(Object object)
    {
        try
        {
            ReteTuple eachTuple = null;
            
            Iterator  tupleIter = leftTuples.iterator();
            
            while ( tupleIter.hasNext() )
            {
                eachTuple = (ReteTuple) tupleIter.next();
                
                if ( eachTuple.dependsOn( object ) )
                {
                    tupleIter.remove();
                }
            }
            
            tupleIter = rightTuples.iterator();
            
            while ( tupleIter.hasNext() )
            {
                eachTuple = (ReteTuple) tupleIter.next();
                
                if ( eachTuple.dependsOn( object ) )
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

    protected void retractTuples(TupleKey key)
    {
        retractTuples( key,
                       getLeftTuples().iterator() );

        retractTuples( key,
                       getRightTuples().iterator() );
    }

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

    protected void modifyLeftTuples(Object trigger,
                                    TupleSet newTuples,
                                    JoinNode joinNode,
                                    WorkingMemory workingMemory) throws FactException
    {
        modifyTuples( trigger,
                      newTuples,
                      getLeftTuples(),
                      getRightTuples(),
                      joinNode,
                      workingMemory  );
    }

    protected void modifyRightTuples(Object trigger,
                                     TupleSet newTuples,
                                     JoinNode joinNode,
                                     WorkingMemory workingMemory) throws FactException
    {
        modifyTuples( trigger,
                      newTuples,
                      getRightTuples(),
                      getLeftTuples(),
                      joinNode,
                      workingMemory );
    }

    protected void modifyTuples(Object trigger,
                                TupleSet newTuples,
                                TupleSet thisSideTuples,
                                TupleSet thatSideTuples,
                                JoinNode joinNode,
                                WorkingMemory workingMemory) throws FactException
    {
        Set retractedKeys = new HashSet();

        Set origModified = new HashSet();
        Set newModified = new HashSet();
        
        ReteTuple origTuple = null;
        ReteTuple newTuple  = null;
        
        Iterator  tupleIter = thisSideTuples.iterator();

        while ( tupleIter.hasNext() )
        {
            origTuple = (ReteTuple) tupleIter.next();

            if ( origTuple.dependsOn( trigger ) )
            {
                newTuple = newTuples.getTuple( origTuple.getKey() );

                if ( newTuple == null )
                {
                    retractedKeys.add( origTuple.getKey() );
                }
                else
                {
                    newModified.add( newTuple );
                    origModified.add( origTuple );
                }
            }
        }

        ReteTuple eachTuple = null;

        TupleSet origJoined = new TupleSet();

        tupleIter = origModified.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            origJoined.addAllTuples( attemptJoin( eachTuple,
                                                  getRightTupleIterator() ) );
        }

        TupleSet newJoined = new TupleSet();

        tupleIter = newModified.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            newJoined.addAllTuples( attemptJoin( eachTuple,
                                                 getRightTupleIterator() ) );
        }

        tupleIter = origJoined.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            if ( ! newJoined.containsTuple( eachTuple.getKey() ) )
            {
                retractedKeys.add( eachTuple.getKey() );
            }
        }

        propagateRetractTuples( trigger,
                                retractedKeys,
                                joinNode,
                                workingMemory );
                                

        // propagateRetractedKeys
        // propagateNewJoined.
    }

    private void propagateRetractTuples(Object trigger,
                                        Set retractedKeys,
                                        JoinNode joinNode, 
                                        WorkingMemory workingMemory) throws FactException
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

    /** Add a {@link ReteTuple} received from the <code>JoinNode's</code>
     *  left input to the left side of this memory, and attempt
     *  to join to existing <code>Tuples</code> in the right
     *  side.
     *
     *  @param tuple The <code>Tuple</code> to add to the left
     *         side memory.
     *
     *  @return A <code>List</code> of <code>Tuples</code> successfully
     *          created by joining the incoming <code>tuple</code>
     *          against existing <code>Tuples</code> on the right
     *          side memory.
     */
    protected Set addLeftTuple(ReteTuple tuple)
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

    /** Add a {@link ReteTuple} received from the <code>JoinNode's</code>
     *  right input to the right side of this memory, and attempt
     *  to join to existing <code>Tuples</code> in the left
     *  side.
     *
     *  @param tuple The <code>Tuple</code> to add to the right
     *         side memory.
     *
     *  @return A <code>List</code> of <code>Tuples</code> successfully
     *          created by joining the incoming <code>tuple</code>
     *          against existing <code>Tuples</code> on the left
     *          side memory.
     */
    protected Set addRightTuple(ReteTuple tuple)
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
     *  @param tupleIterator The <code>Iterator</code> over
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

    /** Attempt to join two <code>Tuples</code>.
     *
     *  @param left The left-side <code>Tuple</code>.
     *  @param right The right-side <code>Tuple</code>.
     *
     *  @return A newly joined <code>Tuple</code> if a join
     *          is possible, else <code>null</code>.
     */
    protected ReteTuple attemptJoin(ReteTuple left,
                                    ReteTuple right)
    {
        
        Iterator    declIter = getJoinDeclarationIterator();
        Declaration eachDecl = null;

        Object leftValue  = null;
        Object rightValue = null;

        while ( declIter.hasNext() )
        {

            eachDecl = (Declaration) declIter.next();

            leftValue  = left.get( eachDecl );
            rightValue = right.get( eachDecl );

            if ( leftValue == null
                 &&
                 rightValue == null )
            {
                continue;
            }

            if ( leftValue == null
                 ||
                 rightValue == null )
            {
                return null;
            }

            if ( leftValue.equals( rightValue ) )
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
}
