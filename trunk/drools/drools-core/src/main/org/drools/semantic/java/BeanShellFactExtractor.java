package org.drools.semantic.java;

/*
 $Id: BeanShellFactExtractor.java,v 1.5 2002-08-02 14:10:33 bob Exp $

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
import org.drools.spi.FactExtractor;
import org.drools.spi.FactExtractionException;
import org.drools.spi.Tuple;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

/** A <code>FactExtractor</code> using <a href="http://beanshell.org/">BeanShell</a>
 *  to perform run-time fact extraction.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class BeanShellFactExtractor implements FactExtractor
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The expression to extract the fact. */
    private String        expression;

    /** The members referenced by the expression. */
    private Declaration[] requiredTupleMembers;

    /** The BeanShell interpreter. */
    private Interpreter interp;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

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

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Initialize the BeanShell interpreter.
     */
    private void initializeInterpreter()
    {
        this.interp = new Interpreter();
    }

    /** Retrieve the fact-extraction BeanShell expression.
     *
     *  @return The fact-extraction expression.
     */
    public String getExpression()
    {
        return this.expression;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.spi.FactExtractor
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the array of <code>Declaration</code>s required
     *  by this <code>FactExtractor</code> to perform its duties.
     *
     *  @return The array of <code>Declarations</code> expected
     *          on incoming <code>Tuple</code>s.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredTupleMembers;
    }

    /** Extract a new fact from the incoming <code>Tuple</code>
     *
     *  @param tuple The source data tuple.
     *
     *  @return The newly extract fact object.
     *
     *  @throws FactExtractionException if an error occurs during
     *          fact extraction activities.
     */
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
