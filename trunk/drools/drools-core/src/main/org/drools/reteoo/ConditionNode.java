package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.FactException;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.spi.Condition;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** Node which filters <code>ReteTuple</code>s.
 *
 *  <p>
 *  Using a semantic <code>Condition</code>, this node
 *  may allow or disallow <code>Tuples</code> to proceed
 *  further through the Rete-OO network.
 *  </p>
 *
 *  @see ConditionNode
 *  @see Condition
 *  @see ReteTuple
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class ConditionNode
    extends TupleSource
    implements TupleSink
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The semantic <code>Condition</code>. */
    private Condition condition;

    /** The source of incoming <code>Tuples</code>. */
    private TupleSource tupleSource;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param tupleSource The source of incoming <code>Tuples</code>.
     *  @param condition The semantic <code>Condition</code>.
     */
    ConditionNode(TupleSource tupleSource,
                  Condition condition)
    {
        this.condition   = condition;
        this.tupleSource = tupleSource;

        if ( tupleSource != null )
        {
            this.tupleSource.setTupleSink( this );
        }
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the <code>Condition</code> associated
     *  with this node.
     *
     *  @return The <code>Condition</code>.
     */
    public Condition getCondition()
    {
        return this.condition;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.impl.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>Set</code> of <code>Declaration</code>s
     *  in the propagated <code>Tuples</code>.
     *
     *  @return The <code>Set</code> of <code>Declarations</code>
     *          in progated <code>Tuples</code>.
     */
    public Set getTupleDeclarations()
    {
        return this.tupleSource.getTupleDeclarations();
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
                            WorkingMemoryImpl workingMemory)
        throws AssertionException
    {
        if ( getCondition().isAllowed( tuple ) )
        {
            propagateAssertTuple( tuple,
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
                              WorkingMemoryImpl workingMemory)
        throws RetractionException
    {
        propagateRetractTuples( key,
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
                             WorkingMemoryImpl workingMemory)
        throws FactException
    {
        Set retractedKeys = new HashSet();

        Iterator  tupleIter = newTuples.iterator();
        ReteTuple eachTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            if ( ! getCondition().isAllowed( eachTuple ) )
            {
                tupleIter.remove();
                retractedKeys.add( eachTuple.getKey() );
            }
        }

        Iterator keyIter = retractedKeys.iterator();
        TupleKey eachKey = null;

        while ( keyIter.hasNext() )
        {
            eachKey = (TupleKey) keyIter.next();

            propagateRetractTuples( eachKey,
                                    workingMemory );
        }

        propagateModifyTuples( trigger,
                               newTuples,
                               workingMemory );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Produce a debug string.
     *
     *  @return The debug string.
     */
    public String toString()
    {
        return "[ConditionNodeImpl: cond=" + this.condition + "]";
    }
}
