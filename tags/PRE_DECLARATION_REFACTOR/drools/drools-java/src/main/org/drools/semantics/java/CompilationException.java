package org.drools.semantics.java;

import org.drools.DroolsException;
import org.drools.rule.Rule;

public class CompilationException
    extends DroolsException
{
    private Rule rule;
    private String text;
    private int lineNumber;
    private int columnNumber;
    private String errorMessage;

    public CompilationException(Rule rule,
                                String text,
                                int lineNumber,
                                int columnNumber,
                                String errorMessage)
    {
        this.rule = rule;
        this.text = text;
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
