
package org.drools.spi;

import org.drools.WorkingMemory;

/** Action to be executed upon successful  match of a {@link Rule}.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public interface Action
{
    /** Execute the action for the supplied
     *  matching {@link Tuple}.
     *
     *  @param tuple The matching tuple.
     */
    void invoke(Tuple tuple,
                WorkingMemory workingMemory) throws ActionInvokationException;
}

