package org.drools;

/** Indicates an error while asserting a new
 *  <i>root fact object</i> into a <code>WorkingMemory</code>.
 *
 *  @see WorkingMemory#assertObject
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class AssertionException
    extends FactException
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public AssertionException()
    {
        // intentionally left blank
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
