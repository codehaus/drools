package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.FactException;

/** Receiver of propagated <code>ReteTuple</code>s from a <code>TupleSource</code>.
 *
 *  @see TupleSource
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
interface TupleSink
{
    /** Assert a new <code>Tuple</code>.
     *
     *  @param tuple The <code>Tuple</code> being asserted.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws AssertionException If an error occurs while asserting.
     */
    void assertTuple(ReteTuple tuple,
                     WorkingMemoryImpl workingMemory) throws AssertionException;

    /** Retract tuples.
     *
     *  @param key The tuple key.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws RetractionException If an error occurs while retracting.
     */
    void retractTuples(TupleKey key,
                       WorkingMemoryImpl workingMemory) throws RetractionException;

    /** Modify tuples.
     *
     *  @param trigger The root fact object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while modifying.
     */
    void modifyTuples(FactHandle trigger,
                      TupleSet newTuples,
                      WorkingMemoryImpl workingMemory) throws FactException;
}
