
package org.drools.spi;

/** Extracts new facts from existing facts.
 *
 *  @see AssignmentCondition
 *  @see Tuple
 *  @see Declaration
 *
 *  @author <a href="mailto:bob@werken.com"</a>
 */
public interface FactExtractor
{

    /** Retrieve the array of {@link Declaration}s required
     *  by this <code>FactExtractor</code> to perform its duties.
     *
     *  @return The array of <code>Declarations</code> expected
     *          on incoming {@link Tuple}s.
     */
    Declaration[] getRequiredTupleMembers();

    /** Extract a new fact from the incoming <code>Tuple</code>
     *
     *  @return The newly extract fact object.
     *
     *  @throws FactExtractionException if an error occurs during
     *          fact extraction activities.
     */
    Object extractFact(Tuple tuple) throws FactExtractionException;
}
