
package org.drools.reteoo;

import org.drools.spi.Declaration;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
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
    private List leftTuples;

    /** <code>List</code> of <code>Tuples</code> received
     *  on the right input of the <code>JoinNode</code>.
     */
    private List rightTuples;

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
        this.leftTuples  = new LinkedList();
        this.rightTuples = new LinkedList();

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
        ReteTuple eachTuple = null;

        Iterator  tupleIter = leftTuples.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            if ( eachTuple.containsRootFactObject( object ) )
            {
                tupleIter.remove();
            }
        }

        tupleIter = rightTuples.iterator();
        
        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            if ( eachTuple.containsRootFactObject( object ) )
            {
                tupleIter.remove();
            }
        }
    }

    protected void modifyLeft(Object trigger,
                              TupleSet tuples)
    {
        Set origModified = new HashSet();
        Set newModified  = new HashSet();
        Set retracted    = new HashSet();

        Iterator  tupleIter = getLeftTupleIterator();
        ReteTuple eachTuple = null;
        ReteTuple newTuple  = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            if ( eachTuple.dependsOn( trigger ) )
            {
                newTuple = tuples.getTuple( eachTuple.getKeyColumns() );

                if ( newTuple == null )
                {
                    tupleIter.remove();
                    retracted.add( eachTuple );
                }
                else
                {
                    origModified.add( eachTuple );
                    newModified.add( newTuple );
                }
            }
        }

        List joinedRetracted = new ArrayList();

        tupleIter = retracted.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            joinedRetracted.addAll( attemptJoin( eachTuple,
                                                 getRightTupleIterator() ) );
        }

        List origJoined = new ArrayList();

        tupleIter = origModified.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            origJoined.addAll( attemptJoin( eachTuple,
                                            getRightTupleIterator() ) );
        }

        List newJoined = new ArrayList();

        tupleIter = newModified.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            newJoined.addAll( attemptJoin( eachTuple,
                                           getRightTupleIterator() ) );
        }

        /*
        tupleIter = origJoined.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();
        }
        */
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
    protected List addLeftTuple(ReteTuple tuple)
    {
        this.leftTuples.add( tuple );

        return attemptJoin( tuple,
                            getRightTupleIterator() );
    }

    /** Retrieve the <code>List</code> of <code>Tuples</code>
     *  held in the left side memory.
     *
     *  @return The <code>List</code> of <code>Tuples</code>
     *          help in the left side memory.
     */
    protected List getLeftTuples()
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
    protected List addRightTuple(ReteTuple tuple)
    {
        this.rightTuples.add( tuple );

        return attemptJoin( tuple,
                            getLeftTupleIterator() );
    }

    /** Retrieve the <code>List</code> of <code>Tuples</code>
     *  held in the right side memory.
     *
     *  @return The <code>List</code> of <code>Tuples</code>
     *          help in the right side memory.
     */
    protected List getRightTuples()
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
    protected List attemptJoin(ReteTuple tuple,
                               Iterator tupleIter)
    {
        List joinedTuples = Collections.EMPTY_LIST;

        ReteTuple eachTuple   = null;
        ReteTuple joinedTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();
            
            joinedTuple = attemptJoin( tuple,
                                       eachTuple );

            if ( joinedTuple != null )
            {
                if ( joinedTuples == Collections.EMPTY_LIST )
                {
                    joinedTuples = new ArrayList();
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
