
package org.drools.spi;

public class InvalidRuleException extends RuleConstructionException
{
    private Rule rule;

    public InvalidRuleException(Rule rule)
    {
        this.rule = rule;
    }

    public Rule getRule()
    {
        return this.rule;
    }
}
