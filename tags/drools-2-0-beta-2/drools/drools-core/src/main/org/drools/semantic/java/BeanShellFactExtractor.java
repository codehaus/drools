
package org.drools.semantic.java;

import org.drools.spi.FactExtractor;
import org.drools.spi.FactExtractionException;
import org.drools.spi.Tuple;
import org.drools.spi.Declaration;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

/** A {@link FactExtractor} using <a href="http://beanshell.org/">BeanShell</a>
 *  to perform run-time fact extraction.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class BeanShellFactExtractor implements FactExtractor
{
    /** The expression to extract the fact. */
    private String        expression;

    /** The members referenced by the expression. */
    private Declaration[] requiredTupleMembers;

    /** The BeanShell interpreter. */
    private Interpreter interp;

    /** Construct.
     *
     *  @param expression The fact extraction expression.
     *  @param requiredTupleMembers Set of variables referenced.
     */
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

    /** Initialize the BeanShell interpreter.
     */
    private void initializeInterpreter()
    {
        this.interp = new Interpreter();
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredTupleMembers;
    }

    /** Retrieve the fact-extraction BeanShell expression.
     *
     *  @return The fact-extraction expression.
     */
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
            
            result = this.interp.eval( getExpression() );

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
