
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;

import org.drools.spi.Tuple;

/** Receiver of propagated {@link Tuple}s from a {@link TupleSource}.
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
                     Tuple tuple,
                     WorkingMemory workingMemory) throws AssertionException;

    /** Retract a fact <code>Object</code>.
     *
     *  @param inputSource the source of the <code>Object</code>.
     *  @param object The <code>Object</code> being retracted.
     *  @param workingMemory the working memory session.
     */
    void retractObject(TupleSource inputSource,
                       Object object,
                       WorkingMemory workingMemory) throws RetractionException;
}