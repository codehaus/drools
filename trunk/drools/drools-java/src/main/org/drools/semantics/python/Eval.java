package org.drools.semantics.python;

/*
 $Id: Eval.java,v 1.1 2002-08-27 04:27:07 bob Exp $

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
import org.drools.spi.ObjectType;

import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.__builtin__;
import org.python.util.PythonInterpreter;

import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;

/** Base class for Jython expression-based Python semantic components.
 *
 *  @see ExprCondition
 *  @see ExprExtractor
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: Eval.java,v 1.1 2002-08-27 04:27:07 bob Exp $
 */
public class Eval extends Interp
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Interpreted text. */
    private String text;

    /** BeanShell interpreter. */
    private PythonInterpreter interp;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected Eval()
    {
        // intentionally left blank
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Evaluate.
     *
     *  @param tuple Tuple containing variable bindings.
     *
     *  @return The result of evaluation.
     */
    public Object evaluate(Tuple tuple) 
    {
        PyDictionary dict = setUpDictionary( tuple );
        
        return evaluate( dict );
    }

    /** Evaluate.
     *
     *  @param dict The evaluation dictionary.
     *
     *  @return The result of evaluation.
     */
    protected Object evaluate(PyDictionary locals) 
    {
        PyDictionary globals = new PyDictionary( new Hashtable() );

        PyObject result =  __builtin__.eval( getCode(),
                                             locals,
                                             globals );

        return Py.tojava( result,
                          Object.class );
    }

    /** Evaluate.
     *
     *  @return The result of evaluation.
     */
    protected Object evaluate()
    {
        PyDictionary locals = new PyDictionary( new Hashtable() );

        return evaluate( locals );
    }

    public void setText(String expr)
    {
        setText( expr,
                 "eval" );
    }
}
