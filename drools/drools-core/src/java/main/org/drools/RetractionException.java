
package org.drools;

/** Indicates an error while retracting a <i>root fact object</i>
 *  from a {@link WorkingMemory}.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class RetractionException extends DroolsException
{
    /** Construct.
     */
    public RetractionException()
    {
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause of this exception.
     */
    public RetractionException(Throwable rootCause)
    {
        super( rootCause );
    }
}
