package org.drools.semantics.java;

import org.drools.DroolsException;

public class MissingDeclarationException extends DroolsException
{
    private String expr;
    private String id;

    public MissingDeclarationException(String expr,
                                       String id)
    {
        this.expr = expr;
        this.id = id;
    }

    public String getExpression()
    {
        return this.expr;
    }

    public String getIdentifier()
    {
        return this.id;
    }

    public String getMessage()
    {
        return "No declaration for \"" + getIdentifier() + "\" in: " + getExpression();
    }
}
