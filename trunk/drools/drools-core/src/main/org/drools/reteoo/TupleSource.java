
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;

import org.drools.spi.Tuple;
import org.drools.spi.Declaration;

import java.util.Set;

/** A source of {@link Tuple}s for a {@link TupleSink}.
 *
 *  <p>
 *  Nodes that propagate <code>Tuples</code> extend this class.
 *  </p>
 *
 *  @see TupleSink
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public abstract class TupleSource
{
    /** The destination for <code>Tuples</code>. */
    private TupleSink tupleSink;

    /** Retrieve the <code>Set</code> of {@link Declaration}s
     *  in the propagated <code>Tuples</code>.
     *
     *  @return The <code>Set</code> of <code>Declarations</code>
     *          in progated <code>Tuples</code>.
     */
    protected abstract Set getTupleDeclarations();

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

    /** Retrieve the <code>TupleSink</code> that receives
     *  propagated <code>Tuples</code>.
     *
     *  @return The <code>TupleSink</code> that receives
     *          propagated <code>Tuples</code>.
     */
    protected TupleSink getTupleSink()
    {
        return this.tupleSink;
    }

    /** Propagate the assertiaion of a <code>Tuple</code>
     *  the this node's <code>TupleSink</code>.
     *
     *  @param tuple The <code>Tuple</code> to propagate.
     *  @param workingMemory the working memory session.
     */
    protected void propagateAssertTuple(Tuple tuple,
                                        WorkingMemory workingMemory) throws AssertionException
    {
        System.err.println( this + " -> propagateTuple(" + tuple + ")" );
        TupleSink sink = getTupleSink();

        if ( sink != null )
        {
            sink.assertTuple( this,
                              tuple,
                              workingMemory );
        }
    }

    /** Propagate the retraction of a <code>Tuple</code>
     *  to this node's <code>TupleSink</code>.
     *
     *  @param tuple The <code>Tuple</code> to propagate.
     *  @param workingMemory the working memory session.
     */
    protected void propagateRetractObject(Object object,
                                         WorkingMemory workingMemory) throws RetractionException
    {
        TupleSink sink = getTupleSink();

        if ( sink != null )
        {
            sink.retractObject( this,
                                object,
                                workingMemory );
        }
    }
}
