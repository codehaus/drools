
package org.drools.spi;

/** Validity exception indicating that a {@link Rule} does not
 *  contain any {@link Condition}s.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class NoConditionException extends InvalidRuleException
{
    /** Construct.
     *
     *  @param rule The invalid <code>Rule</code>.
     */
    public NoConditionException(Rule rule)
    {
        super( rule );
    }
}
