
package org.drools.spi;

import org.drools.AssertionException;

/** Indicates an error while applying a {@link FilterCondition}.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class FilterException extends AssertionException
{
    /** Construct.
     */
    public FilterException()
    {
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause of this exception.
     */
    public FilterException(Throwable rootCause)
    {
        super( rootCause );
    }
}
