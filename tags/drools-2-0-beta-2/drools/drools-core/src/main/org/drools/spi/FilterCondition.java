
package org.drools.spi;

/** A {@link Condition} that filters facts.
 *
 *  @see Tuple
 *  
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public interface FilterCondition extends Condition
{
    Declaration[] getRequiredTupleMembers();

    /** Determine if the supplied {@link Tuple} is allowed
     *  by this filter.
     *
     *  @param tuple The <code>Tuple</code> to test.
     *
     *  @return <code>true</code> if the <code>Tuple</code>
     *          passes this filter, else <code>false</code>.
     *
     *  @throws FilterException if an error occurs during filtering.
     */
    boolean isAllowed(Tuple tuple) throws FilterException;
}

