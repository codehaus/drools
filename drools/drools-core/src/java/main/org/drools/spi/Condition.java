
package org.drools.spi;

/** A test of facts for a {@link Rule}.
 *
 *  @see Tuple
 *  @see Declaration
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public interface Condition
{
    /** Retrieve the array of {@link Declaration}s required
     *  by this condition to perform its duties.
     *
     *  @return The array of <code>Declarations</code> expected
     *          on incoming {@link Tuple}s.
     */
    Declaration[] getRequiredTupleMembers();
}
