package org.drools;

/** Indicates an error while retracting a <i>root fact object</i>
 *  from a <code>WorkingMemory</code>.
 *
 *  @see WorkingMemory#retractObject
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class RetractionException extends FactException
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public RetractionException()
    {
        // intentionally left blank
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
