package org.drools.semantics.java;

import org.drools.rule.Declaration;
import org.drools.spi.Tuple;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

public class Interp
{
    private String text;

    private Interpreter interp;

    protected Interp()
    {
        this.interp = new Interpreter();
        this.text = null;
    }

    public Object evaluate(Tuple tuple) throws EvalError
    {
        try
        {
            setUpInterpreter( tuple );

            return evaluate();
        }
        finally
        {
            cleanUpInterpreter( tuple );
        }
    }

    protected Object evaluate() throws EvalError
    {
        return this.interp.eval( getText() );
    }

    public String getText()
    {
        return this.text;
    }

    protected void setText(String text)
    {
        this.text = text;
    }

    protected void setUpInterpreter(Tuple tuple) throws EvalError
    {
        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            setVariable( eachDecl.getIdentifier(),
                         tuple.get( eachDecl ) );
        }
    }

    protected void setVariable(String name,
                               Object value) throws EvalError
    {
        this.interp.set( name,
                         value );
    }

    protected void cleanUpInterpreter(Tuple tuple) throws EvalError
    {
        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            unsetVariable( eachDecl.getIdentifier() );
        }
    }

    protected void unsetVariable(String name) throws EvalError
    {
        this.interp.unset( name );
    }
}
