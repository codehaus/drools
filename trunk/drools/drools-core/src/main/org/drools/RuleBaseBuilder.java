package org.drools;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.reteoo.Builder;

public class RuleBaseBuilder
{
    private Builder builder;

    public RuleBaseBuilder()
    {
        this.builder = new Builder();
    }

    public void addRuleSet(RuleSet ruleSet)
        throws RuleIntegrationException
    {
        this.builder.addRuleSet( ruleSet );
    }

    public RuleBase build()
    {
        return this.builder.buildRuleBase();
    }
}
