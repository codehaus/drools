package org.drools.semantics.java;

import org.drools.rule.Declaration;
import org.drools.smf.ConfigurationException;
import org.drools.spi.Tuple;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

public class Expr extends Interp
{
    private static final Declaration[] EMPTY_DECLS = new Declaration[0];

    private Declaration[] requiredDecls;

    protected Expr()
    {
        this.requiredDecls = EMPTY_DECLS;
    }

    public String getExpression()
    {
        return getText();
    }

    protected void setExpression(String expr)
    {
        setText( expr );
    }

    public void configure(String expr,
                          Declaration[] decls) throws ConfigurationException
    {
        ExprAnalyzer analyzer = new ExprAnalyzer();
        
        try
        {
            this.requiredDecls = analyzer.analyze( expr,
                                                   decls );
        }
        catch (Exception e)
        {
            throw new ConfigurationException( e );
        }

        setExpression( expr );
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredDecls;
    }
}
