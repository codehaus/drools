
package org.drools.semantic.java;

import org.drools.spi.FilterCondition;
import org.drools.spi.FilterException;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

/** {@link FilterCondition} using <a href="http://beanshell.org/">BeanShell</a>
 *  for evaluation.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class BeanShellFilterCondition implements FilterCondition
{
    /** The BeanShell expression. */
    private String        filterExpr;

    /** Variables referenced by the expression. */
    private Declaration[] requiredTupleMembers;

    /** The beanshell interpreter. */
    private Interpreter   interp;

    /** Construct.
     *
     *  <p>
     *  The expression used to construct this <b>must</b> produce
     *  a <b>boolean</b> result, else a {@link NonBooleanExpressionException}
     *  will be thrown at run-time.
     *  </p>
     *
     *  @param filterExpr The filtering expression.
     *  @param requiredTupleMembers Set of variables referenced.
     */
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

    /** Initialize the BeanShell interpreter. 
     */
    private void initializeInterpreter()
    {
        this.interp = new Interpreter();
    }

    /** Retrieve the BeanShell filtering expression.
     *
     *  @return The filtering expression.
     */
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
