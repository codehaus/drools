package org.drools;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.reteoo.Builder;
import org.drools.reteoo.Rete;

public class RuleBaseBuilder
{
    private Rete rete;
    private Builder builder;

    public RuleBaseBuilder()
    {
    }

    public void addRule(Rule rule)
        throws RuleIntegrationException
    {
        init();
        this.builder.addRule( rule );
    }

    public void addRuleSet(RuleSet ruleSet)
        throws RuleIntegrationException
    {
        init();
        
        Rule[] rules = ruleSet.getRules();

        for ( int i = 0 ; i < rules.length ; ++i )
        {
            this.builder.addRule( rules[ i ] );
        }
    }

    public RuleBase build()
    {
        Rete rete= this.rete;

        this.rete    = null;
        this.builder = null;

        return new RuleBaseImpl( this.rete );
    }

    void init()
    {
        if ( this.rete == null )
        {
            this.rete    = new Rete();
            this.builder = new Builder( this.rete );
        }
    }
}
