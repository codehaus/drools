
package org.drools.spi;

import org.drools.DroolsException;

/** Indicates an attempt to add a {@link Rule} to a {@link RuleSet}
 *  that already contains a <code>Rule</code> with the same name.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class DuplicateRuleNameException extends RuleConstructionException
{
    private RuleSet ruleSet;
    private Rule    originalRule;
    private Rule    conflictingRule;

    /** Construct.
     *
     *  @param ruleSet The <code>RuleSet</code>.
     *  @param originalRule The <code>Rule</code> already in the <code>RuleSet</code>.
     *  @param conflictRule The new, conflicting <code>Rule</code>.
     */
    public DuplicateRuleNameException(RuleSet ruleSet,
                                      Rule originalRule,
                                      Rule conflictingRule)
    {
        this.ruleSet         = ruleSet;
        this.originalRule    = originalRule;
        this.conflictingRule = conflictingRule;
    }

    /** Retrieve the <code>RuleSet</code>.
     *
     *  @return The <code>RuleSet</code>.
     */
    public RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    /** Retrieve the original <code>Rule</code> in the <code>RuleSet</code>.
     *
     *  @return The <code>Rule</code>.
     */
    public Rule getOriginalRule()
    {
        return this.originalRule;
    }

    /** Retrieve the new conflicting <code>Rule</code>.
     *
     *  @return The <code>Rule</code>.
     */
    public Rule getConflictingRule()
    {
        return this.conflictingRule;
    }
}
