package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.FactException;
import org.drools.AssertionException;
import org.drools.RetractionException;

import java.util.Set;

/** A source of <code>ReteTuple</code>s for a <code>TupleSink</code>.
 *
 *  <p>
 *  Nodes that propagate <code>Tuples</code> extend this class.
 *  </p>
 *
 *  @see TupleSource
 *  @see ReteTuple
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
abstract class TupleSource
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The destination for <code>Tuples</code>. */
    private TupleSink tupleSink;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    TupleSource()
    {
        // intentionally left blank.
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the <code>TupleSink</code> that receives <code>Tuples</code>
     *  propagated from this <code>TupleSource</code>.
     *
     *  @param tupleSink The <code>TupleSink</code> to receive
     *         propagated <code>Tuples</code>.
     */
    protected void setTupleSink(TupleSink tupleSink)
    {
        this.tupleSink = tupleSink;
    }

    /** Propagate the assertion of a <code>Tuple</code>
     *  to this node's <code>TupleSink</code>.
     *
     *  @param tuple The <code>Tuple</code> to propagate.
     *  @param workingMemory the working memory session.
     *
     *  @throws AssertionException If an errors occurs while
     *          attempting assertion.
     */
    protected void propagateAssertTuple(ReteTuple tuple,
                                        WorkingMemoryImpl workingMemory)
        throws AssertionException
    {
        TupleSink sink = getTupleSink();

        if ( sink != null )
        {
            sink.assertTuple( tuple,
                              workingMemory );
        }
    }

    /** Propagate the retration of a <code>Tuple</code>
     *  to this node's <code>TupleSink</code>.
     *
     *  @param key The tuple key.
     *  @param workingMemory The working memory session.
     *
     *  @throws RetractionException If an error occurs while
     *          attempting retraction
     *
     */
    protected void propagateRetractTuples(TupleKey key,
                                          WorkingMemoryImpl workingMemory)
        throws RetractionException
    {
        TupleSink sink = getTupleSink();

        if ( sink != null )
        {
            sink.retractTuples( key,
                                workingMemory );
        }
    }

    /** Propagate the modification of <code>Tuple</code>s
     *  to this node's <code>TupleSink</code>.
     *
     *  @param trigger The modification trigger object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while
     *          attempting modification.
     */
    protected void propagateModifyTuples(FactHandle trigger,
                                         TupleSet newTuples,
                                         WorkingMemoryImpl workingMemory)
        throws FactException
    {
        TupleSink sink = getTupleSink();

        if ( sink != null )
        {
            sink.modifyTuples( trigger,
                               newTuples,
                               workingMemory );
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>TupleSink</code> that receives
     *  propagated <code>Tuples</code>.
     *
     *  @return The <code>TupleSink</code> that receives
     *          propagated <code>Tuples</code>.
     */
    public TupleSink getTupleSink()
    {
        return this.tupleSink;
    }

    public abstract Set getTupleDeclarations();
}
