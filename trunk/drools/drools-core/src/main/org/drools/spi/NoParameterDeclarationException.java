
package org.drools.spi;

/** Validity exception indicating that a {@link Rule} does not
 *  contain any root object parameter {@link Declaration}s.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class NoParameterDeclarationException extends InvalidRuleException
{
    /** Construct.
     *
     *  @param rule The invalid <code>Rule</code>.
     */
    public NoParameterDeclarationException(Rule rule)
    {
        super( rule );
    }
}
