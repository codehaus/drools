
package org.drools.spi;

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
    void invoke(Tuple tuple) throws ActionInvokationException;
}

