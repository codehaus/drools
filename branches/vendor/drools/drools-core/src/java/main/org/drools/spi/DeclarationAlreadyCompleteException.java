
package org.drools.spi;

public class DeclarationAlreadyCompleteException extends RuleConstructionException
{
    private Rule rule;

    public DeclarationAlreadyCompleteException(Rule rule)
    {
        this.rule = rule;
    }

    public Rule getRule()
    {
        return this.rule;
    }
}
