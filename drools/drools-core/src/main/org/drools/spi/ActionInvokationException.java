
package org.drools.spi;

import org.drools.AssertionException;

/** Indicates an error during an {@link Action} invokation.
 *
 *  @see Action
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ActionInvokationException extends AssertionException
{
    /** Construct.
     */
    public ActionInvokationException()
    {
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause of this exception.
     */
    public ActionInvokationException(Throwable rootCause)
    {
        super( rootCause );
    }
}
