
package org.drools.spi;

import org.drools.AssertionException;

/** Indicates an error while extracting a fact from
 *  a <i>root fact object</i>.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class FactExtractionException extends AssertionException
{
    /** Construct.
     */
    public FactExtractionException()
    {
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause of this exception.
     */
    public FactExtractionException(Throwable rootCause)
    {
        super( rootCause );
    }
}
