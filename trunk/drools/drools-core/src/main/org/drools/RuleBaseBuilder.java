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

    public void addRule(Rule rule)
        throws RuleIntegrationException
    {
        this.builder.addRule( rule );
    }

    public void addRuleSet(RuleSet ruleSet)
        throws RuleIntegrationException
    {
        Rule[] rules = ruleSet.getRules();

        for ( int i = 0 ; i < rules.length ; ++i )
        {
            this.builder.addRule( rules[ i ] );
        }
    }

    public RuleBase build()
    {
        return this.builder.buildRuleBase();
    }
}
