
package org.drools.reteoo;

import org.drools.DroolsException;

/** Base exception for Rete-OO errors.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ReteException extends DroolsException
{
    /** Construct.
     */
    public ReteException()
    {
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause of this exception.
     */
    public ReteException(Throwable rootCause)
    {
        super( rootCause );
    }
}
