package org.drools.semantic.java;

/*
 $Id: BeanShellExprCondition.java,v 1.3 2002-08-17 05:49:22 bob Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

/** <code>Condition</code> using <a href="http://beanshell.org/">BeanShell</a>
 *  for evaluation.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class BeanShellExprCondition implements Condition
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The BeanShell expression. */
    private String        condExpr;

    /** Variables referenced by the expression. */
    private Declaration[] requiredTupleMembers;

    /** The beanshell interpreter. */
    private Interpreter   interp;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

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

    /** Construct, partially.
     *
     *  @see #configure
     */
    public BeanShellExprCondition()
    {

    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.spi.Condition
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the array of <code>Declaration</code>s required
     *  by this condition to perform its duties.
     *
     *  @return The array of <code>Declarations</code> expected
     *          on incoming <code>Tuples</code>.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredTupleMembers;
    }

    /** Determine if the supplied <code>Tuple</code> is allowed
     *  by this condition.
     *
     *  @param tuple The <code>Tuple</code> to test.
     *
     *  @return <code>true</code> if the <code>Tuple</code>
     *          passes this condition, else <code>false</code>.
     *
     *  @throws ConditionException if an error occurs during filtering.
     */
    public boolean isAllowed(Tuple tuple) throws ConditionException
    {
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

                throw new NonBooleanExpressionException( this.condExpr );
            }
        }
        catch (EvalError e)
        {
            initializeInterpreter();
            throw new ConditionException( e );
        }

        return result;
    }

    public void configure(Rule rule,
                          String configInfo)
    {
        
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Produce a debug string.
     *
     *  @return The debug string.
     */
    public String toString()
    {
        return this.condExpr;
    }
}
