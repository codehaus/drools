
package org.drools.semantic.java.io;

import org.drools.DroolsException;

/** Indicates an error during the reading, parsing, or
 *  construction of a RuleSet from a Java XML ruleset
 *  definition.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class RuleSetReaderException extends DroolsException
{
    /** Construct with a root cause.
     */
    public RuleSetReaderException(Throwable rootCause)
    {
        super( rootCause );
    }
}
