package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.FactException;

/** Input <code>TupleSinkImpl</code> for a <code>JoinNodeImpl</code>.
 *
 *  @see JoinNode
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class JoinNodeInput
    implements TupleSink
{
    // ------------------------------------------------------------
    //     Constants
    // ------------------------------------------------------------

    /** Left-side input. */
    static final int LEFT  = 41;

    /** Right-side input. */
    static final int RIGHT = 42;
    
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Join node. */
    private JoinNode joinNode;

    /** Side. */
    private int side;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param joinNode Join node.
     *  @param side Side marker.
     */
    JoinNodeInput(JoinNode joinNode,
                  int side)
    {
        this.joinNode = joinNode;
        this.side     = side;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the side marker.
     *
     *  @return The side marker.
     */
    int getSide()
    {
        return this.side;
    }

    /** Retrieve the destination join node.
     *
     *  @return The join node.
     */
    JoinNode getJoinNode()
    {
        return this.joinNode;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.impl.TupleSink
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Assert a new <code>Tuple</code>.
     *
     *  @param tuple The <code>Tuple</code> being asserted.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws AssertionException If an error occurs while asserting.
     */
    public void assertTuple(ReteTuple tuple,
                            WorkingMemoryImpl workingMemory) throws AssertionException
    {
        if ( this.side == LEFT )
        {
            getJoinNode().assertLeftTuple( tuple,
                                           workingMemory  );
        }
        else
        {
            getJoinNode().assertRightTuple( tuple,
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
                              WorkingMemoryImpl workingMemory) throws RetractionException
    {
        getJoinNode().retractTuples( key,
                                     workingMemory );
    }

    /** Modify tuples.
     *
     *  @param trigger The root fact object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while modifying.
     */
    public void modifyTuples(FactHandle trigger,
                             TupleSet newTuples,
                             WorkingMemoryImpl workingMemory) throws FactException
    {
        if ( this.side == LEFT )
        {
            getJoinNode().modifyLeftTuples( trigger,
                                            newTuples,
                                            workingMemory );
        }
        else
        {
            getJoinNode().modifyRightTuples( trigger,
                                             newTuples,
                                             workingMemory );
        }
    }
}
