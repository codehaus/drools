
package org.drools;

/** Indicates an error while notifying a {@link WorkingMemory}
 *  of modified <i>root fact object</i>.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ModificationException extends FactException
{
    /** Construct.
     */
    public ModificationException()
    {
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause of this exception.
     */
    public ModificationException(Throwable rootCause)
    {
        super( rootCause );
    }

}
