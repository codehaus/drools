
package org.drools.reteoo;

import org.drools.spi.Tuple;
import org.drools.spi.Declaration;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
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
        this.leftTuples  = new ArrayList();
        this.rightTuples = new ArrayList();

        this.joinDeclarations = node.getCommonDeclarations();
    }

    /** Add a {@link Tuple} received from the <code>JoinNode's</code>
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
    protected List addLeftTuple(Tuple tuple)
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

    /** Add a {@link Tuple} received from the <code>JoinNode's</code>
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
    protected List addRightTuple(Tuple tuple)
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
    protected List attemptJoin(Tuple tuple,
                               Iterator tupleIter)
    {
        List joinedTuples = Collections.EMPTY_LIST;

        Tuple eachTuple   = null;
        Tuple joinedTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (Tuple) tupleIter.next();

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

    protected Tuple attemptJoin(Tuple left,
                                Tuple right)
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

        Tuple joinedTuple = new JoinTuple( left,
                                           right );

        return joinedTuple;

    }
}
