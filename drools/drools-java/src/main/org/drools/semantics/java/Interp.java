package org.drools.semantics.java;

/*
 $Id: Interp.java,v 1.14 2004-06-30 21:46:32 bob Exp $

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

import bsh.EvalError;
import bsh.UtilEvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import org.drools.rule.Declaration;
import org.drools.spi.ObjectType;
import org.drools.spi.Tuple;
import org.drools.spi.KnowledgeHelper;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;

/** Base class for BeanShell interpreter-based Java semantic components.
 *
 *  @see ExprCondition
 *  @see ExprExtractor
 *  @see BlockConsequence
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: Interp.java,v 1.14 2004-06-30 21:46:32 bob Exp $
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

    protected Interpreter getInterpreter()
    {
        return this.interp;
    }

    /** Evaluate.
     *
     *  @param tuple Tuple containing variable bindings.
     *
     *  @return The result of evaluation.
     *
     *  @throws EvalError If an error occurs while attempting
     *          to evaluate.
     */
    public Object evaluate(Tuple tuple) throws EvalError, UtilEvalError
    {
        NameSpace ns = setUpNameSpace( tuple );

        return evaluate( ns );
    }

    /** Evaluate.
     *
     *  @param ns The evaluation namespace.
     *
     *  @return The result of evaluation.
     *
     *  @throws EvalError If an error occurs while attempting
     *          to evaluate.
     */
    protected Object evaluate(NameSpace ns) throws EvalError
    {
        return this.interp.eval( getText(),
                                 ns );
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

    /** Configure a <code>NameSpace</code> using a <code>Tuple</code>
     *  for variable bindings.
     *
     *  @param tuple Tuple containing variable bindings.
     *
     *  @return The namespace
     *
     *  @throws EvalError If an error occurs while attempting
     *          to bind variables.
     */
    protected NameSpace setUpNameSpace(Tuple tuple) throws UtilEvalError, EvalError
    {
        return setUpNameSpace( tuple,
                               null );
    }

    protected NameSpace setUpNameSpace(Tuple tuple,
                                       NameSpace parent) throws UtilEvalError, EvalError
    {
        NameSpace ns = new NameSpace( parent, interp.getClassManager(),  "" );

        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        ObjectType objectType = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            ns.setVariable( eachDecl.getIdentifier().intern(),
                            tuple.get( eachDecl ), false );

            objectType = eachDecl.getObjectType();

            if ( objectType instanceof ClassObjectType )
            {
                ns.importClass( ((ClassObjectType)objectType).getType().getName() );
            }
        }

        ns.setVariable( "drools".intern(),
                        new KnowledgeHelper( tuple ),
                        false );

        Map appData = tuple.getWorkingMemory().getApplicationDataMap();

        for (Iterator iterator = appData.entrySet().iterator(); iterator.hasNext();)
        {
            Map.Entry entry = (Map.Entry) iterator.next();

            String key = (String) entry.getKey();

            ns.setVariable(key.intern(),
                           entry.getValue(),
                           false);
        }

        // evaluate( ns );

        return ns;
    }

    public String toString()
    {
        return "[[ " + this.text + " ]]";
    }
}
