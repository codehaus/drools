
package org.drools;

/** Indicates an error while asserting a new
 *  <i>root fact object</i> into a {@link WorkingMemory}.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class AssertionException extends DroolsException
{
    /** Construct.
     */
    public AssertionException()
    {
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause of this exception.
     */
    public AssertionException(Throwable rootCause)
    {
        super( rootCause );
    }
}
