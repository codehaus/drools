package org.drools.reteoo;

import org.drools.DroolsException;

/** Base exception for Rete-OO errors.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class ReteException extends DroolsException
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public ReteException()
    {
        // intentionall left blank
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
