
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.FactException;

/** Receiver of propagated {@link ReteTuple}s from a {@link TupleSource}.
 *
 *  @see TupleSource
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public interface TupleSink
{
    /** Assert a new <code>Tuple</code>.
     *
     *  @param inputSource The source of the <code>Tuple</code>.
     *  @param tuple The <code>Tuple</code> being asserted.
     *  @param workingMemory The working memory seesion.
     */
    void assertTuple(TupleSource inputSource,
                     ReteTuple tuple,
                     WorkingMemory workingMemory) throws AssertionException;

    void retractTuples(TupleKey key,
                       WorkingMemory workingMemory) throws RetractionException;

}
