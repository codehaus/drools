package org.drools;

/** Base exception indicating an error in manipulating facts.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class FactException extends DroolsException
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public FactException()
    {
        // intentionally left blank.
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause.
     */
    public FactException(Throwable rootCause)
    {
        super( rootCause );
    }
        
}
