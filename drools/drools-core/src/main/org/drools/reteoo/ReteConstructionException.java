
package org.drools.reteoo;

/** Indicates an error while integrating a
 *  {@link org.drools.spi.Rule} into the Rete-OO network.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ReteConstructionException extends ReteException
{
    /** Construct.
     */
    public ReteConstructionException()
    {
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause of this exception.
     */
    public ReteConstructionException(Throwable rootCause)
    {
        super( rootCause );
    }
}
