
package org.drools.semantic.java;

import org.drools.spi.FactExtractor;
import org.drools.spi.FactExtractionException;
import org.drools.spi.Tuple;
import org.drools.spi.Declaration;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

public class BeanShellFactExtractor implements FactExtractor
{
    private String expression;
    private Declaration[] requiredTupleMembers;

    private Interpreter interp;

    public BeanShellFactExtractor(String expression,
                                  Set requiredTupleMembers)
    {
        this.expression = expression;

        this.requiredTupleMembers = new Declaration[ requiredTupleMembers.size() ];

        Iterator declIter = requiredTupleMembers.iterator();
        int      i        = 0;

        while ( declIter.hasNext() )
        {
            this.requiredTupleMembers[ i ] = (Declaration) declIter.next();
            ++i;
        }

        initializeInterpreter();
    }

    private void initializeInterpreter()
    {
        this.interp = new Interpreter();
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredTupleMembers;
    }

    public String getExpression()
    {
        return this.expression;
    }

    public Object extractFact(Tuple tuple) throws FactExtractionException
    {
        Object result = null;

        try
        {
            BeanShellUtil.setUpInterpreter( this.interp,
                                            tuple );
            
            this.interp.eval( getExpression() );

            BeanShellUtil.cleanUpInterpreter( this.interp,
                                              tuple );
        }
        catch (EvalError e)
        {
            initializeInterpreter();
            throw new FactExtractionException( e );

        }

        return result;
    }
}
