
package org.drools.spi;

import org.drools.DroolsException;

public class DuplicateRuleNameException extends RuleConstructionException
{
    private RuleSet ruleSet;
    private Rule    originalRule;
    private Rule    conflictingRule;

    public DuplicateRuleNameException(RuleSet ruleSet,
                                      Rule originalRule,
                                      Rule conflictingRule)
    {
        this.ruleSet         = ruleSet;
        this.originalRule    = originalRule;
        this.conflictingRule = conflictingRule;
    }

    public RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    public Rule getOriginalRule()
    {
        return this.originalRule;
    }

    public Rule getConflictingRule()
    {
        return this.conflictingRule;
    }
}
