
package org.drools.spi;

/** Indicates an attempt to add a parameter declaration to a
 *  {@link Rule} after adding a {@link Condition} to that
 *  <code>Rule</code>.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class DeclarationAlreadyCompleteException extends RuleConstructionException
{
    /** The troubled rule. */
    private Rule rule;

    /** Construct.
     *
     *  @param rule The Rule involved in the error.
     */
    public DeclarationAlreadyCompleteException(Rule rule)
    {
        this.rule = rule;
    }

    /** Retrieve the implicated <code>Rule</code>.
     *
     *  @return The rule.
     */
    public Rule getRule()
    {
        return this.rule;
    }
}
