package org.drools.semantics.java;

import org.drools.DroolsException;
import org.drools.rule.Rule;

public class CompilationException
    extends DroolsException
{
    private final Rule rule;
    private final String text;
    private final int lineNumber;
    private final int columnNumber;
    private final String errorMessage;

    public CompilationException(Rule rule,
                                String text,
                                int lineNumber,
                                int columnNumber,
                                String errorMessage)
    {
        this.rule = rule;
        this.text = text;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.errorMessage = errorMessage;
    }

    public Rule getRule()
    {
        return this.rule;
    }

    public String getText()
    {
        return this.text;
    }

    public int getLineNumber()
    {
        return this.lineNumber;
    }

    public int getColumNumber()
    {
        return this.columnNumber;
    }

    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    public String getMessage()
    {
        return getRule().getName() + ":" + getLineNumber() + ":" + getColumNumber() + ":\n    " + getErrorMessage();
    }
}
