package org.drools.semantics.java;

/*
 $Id: Interp.java,v 1.4 2002-08-19 00:31:42 bob Exp $

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
import org.drools.spi.Tuple;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

/** Base class for BeanShell interpreter-based Java semantic components.
 *
 *  @see ExprCondition
 *  @see ExprExtractor
 *  @see BlockConsequence
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: Interp.java,v 1.4 2002-08-19 00:31:42 bob Exp $
 */
public class Interp
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Interpreted text. */
    private String text;

    /** BeanShell interpreter. */
    private Interpreter interp;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected Interp()
    {
        this.interp = new Interpreter();
        this.text = null;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Evaluate.
     *
     *  @param tuple Tuple containing variable bindings.
     *
     *  @return The result of evaluation.
     *
     *  @throws EvalError If an error occurs while attempting
     *          to evaluate.
     */
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

    /** Evaluate.
     *
     *  @return The result of evaluation.
     *
     *  @throws EvalError If an error occurs while attempting
     *          to evaluate.
     */
    protected Object evaluate() throws EvalError
    {
        return this.interp.eval( getText() );
    }

    /** Retrieve the text to evaluate.
     *
     *  @return The text to evaluate.
     */
    public String getText()
    {
        return this.text;
    }

    /** Set the text to evaluate.
     *
     *  @param text The text.
     */
    protected void setText(String text)
    {
        this.text = text;
    }

    /** Configure the interpreter using a <code>Tuple</code>
     *  for variable bindings.
     *
     *  @param tuple Tuple containing variable bindings.
     *
     *  @throws EvalError If an error occurs while attempting
     *          to bind variables.
     */
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

    /** Set a variable for the interpreter.
     *
     *  @param name The variable name.
     *  @param value The variable value.
     *
     *  @throws EvalError If an error occurs while attempting
     *          to bind the variable.
     */
    protected void setVariable(String name,
                               Object value) throws EvalError
    {
        this.interp.set( name,
                         value );
    }

    /** Unconfigure the interpreter using a <code>Tuple</code>
     *  for variable bindings.
     *
     *  @param tuple Tuple containing variable bindings.
     *
     *  @throws EvalError If an error occurs while attempting
     *          to unbind variables.
     */
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

    /** Set a variable for the interpreter.
     *
     *  @param name The variable name.
     *
     *  @throws EvalError If an error occurs while attempting
     *          to unbind the variable.
     */
    protected void unsetVariable(String name) throws EvalError
    {
        this.interp.unset( name );
    }
}
