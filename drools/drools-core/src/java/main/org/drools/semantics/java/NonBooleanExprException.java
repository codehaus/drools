package org.drools.semantics.java;

import org.drools.spi.ConditionException;

public class NonBooleanExprException extends ConditionException
{
    private String expr;

    public NonBooleanExprException(String expr)
    {
        this.expr = expr;
    }

    public String getExpression()
    {
        return this.expr;
    }

    public String getMessage()
    {
        return "Not a boolean expression: " + getExpression();
    }
}
