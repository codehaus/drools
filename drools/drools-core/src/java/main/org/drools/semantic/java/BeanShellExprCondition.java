
package org.drools.semantic.java;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

/** {@link ConditionException} using <a href="http://beanshell.org/">BeanShell</a>
 *  for evaluation.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class BeanShellExprCondition implements Condition
{
    /** The BeanShell expression. */
    private String        condExpr;

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
     *  @param condExpr The condition expression.
     *  @param requiredTupleMembers Set of variables referenced.
     */
    public BeanShellExprCondition(String condExpr,
                                  Set requiredTupleMembers)
    {
        this.condExpr           = condExpr;
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


    public String toString()
    {
        return this.condExpr;
    }
    
    /** Initialize the BeanShell interpreter. 
     */
    private void initializeInterpreter()
    {
        this.interp = new Interpreter();
    }

    /** Retrieve the BeanShell condition expression.
     *
     *  @return The condition expression.
     */
    public String getCondExpr()
    {
        return this.condExpr;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredTupleMembers;
    }

    public boolean isAllowed(Tuple tuple) throws ConditionException
    {
        System.err.println( this.condExpr + " --> " + tuple );

        boolean result = false;

        try
        {
            BeanShellUtil.setUpInterpreter( this.interp,
                                            tuple );

            Object resultObj = this.interp.eval( getCondExpr() );
            
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
            throw new ConditionException( e );
        }

        return result;
    }

    
}
