
package org.drools.spi;

/** Indicates an error regarding the semantic validity of a rule.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class InvalidRuleException extends RuleConstructionException
{
    /** The invalid rule. */
    private Rule rule;

    /** Construct.
     *
     *  @param rule The invalid <code>Rule</code>.
     */
    public InvalidRuleException(Rule rule)
    {
        this.rule = rule;
    }

    /** Retrieve the invalid <code>Rule</code>.
     *
     *  @return The invalid <code>Rule</code>.
     */
    public Rule getRule()
    {
        return this.rule;
    }
}
