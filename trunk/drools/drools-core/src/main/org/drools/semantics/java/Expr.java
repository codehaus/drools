package org.drools.semantics.java;

import org.drools.rule.Declaration;
import org.drools.spi.Tuple;
import org.drools.spi.ConfigurationException;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

public class Expr
{
    private Declaration[] requiredDecls;
    private String expr;

    private Interpreter interp;

    protected Expr()
    {
        this.requiredDecls = null;
        this.expr          = null;
    }

    public Object evaluate(Tuple tuple) throws EvalError
    {
        try
        {
            setUpInterpreter( tuple );

            return this.interp.eval( getExpression() );
        }
        finally
        {
            cleanUpInterpreter( tuple );
        }
    }

    public String getExpression()
    {
        return this.expr;
    }

    protected void setUpInterpreter(Tuple tuple) throws EvalError
    {
        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            this.interp.set( eachDecl.getIdentifier(),
                             tuple.get( eachDecl ) );
        }
    }

    protected void cleanUpInterpreter(Tuple tuple) throws EvalError
    {
        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            this.interp.unset( eachDecl.getIdentifier() );
        }
    }

    public void configure(String expr,
                          Declaration[] decls) throws ConfigurationException
    {
        ExprAnalyzer analyzer = new ExprAnalyzer();
        
        try
        {
            this.requiredDecls = analyzer.analyze( decls,
                                                   expr );
        }
        catch (Exception e)
        {
            throw new ConfigurationException( e );
        }

        this.expr = expr;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredDecls;
    }
}
