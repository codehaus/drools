
package org.drools.semantic.java;

import org.drools.spi.FilterCondition;
import org.drools.spi.FilterException;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

public class BeanShellFilterCondition implements FilterCondition
{
    private String        filterExpr;
    private Declaration[] requiredTupleMembers;

    private Interpreter   interp;

    public BeanShellFilterCondition(String filterExpr,
                                    Set requiredTupleMembers)
    {
        this.filterExpr           = filterExpr;
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

    public String getFilterExpr()
    {
        return this.filterExpr;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredTupleMembers;
    }

    public boolean isAllowed(Tuple tuple) throws FilterException
    {
        boolean result = false;

        try
        {
            BeanShellUtil.setUpInterpreter( this.interp,
                                            tuple );

            Object resultObj = this.interp.eval( getFilterExpr() );
            
            if ( resultObj instanceof Boolean )
            {
                result = ((Boolean)resultObj).booleanValue();
            }
            else
            {
                try
                {
                    BeanShellUtil.cleanUpInterpreter( this.interp,
                                                      tuple );
                }
                catch (EvalError e)
                {
                    initializeInterpreter();
                    // no further throws, since we have a higher-priority
                    // exception regarding the non-Boolean return from bsh.
                }

                throw new NonBooleanExpressionException();
            }
        }
        catch (EvalError e)
        {
            initializeInterpreter();
            throw new FilterException( e );
        }

        return result;
    }

    
}
