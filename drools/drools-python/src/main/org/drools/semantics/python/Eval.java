package org.drools.semantics.python;

/*
 $Id: Eval.java,v 1.9 2004-08-15 16:09:38 mproctor Exp $

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
import org.python.core.Py;
import org.python.util.PythonInterpreter;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.__builtin__;
import org.python.core.PyFunction;
import org.python.core.PyCode;
import org.python.core.PyString;

import org.drools.smf.SMFTestFrameWork;

import java.util.Hashtable;

/** Base class for Jython expression-based Python semantic components.
 *
 *  @see ExprCondition
 *  @see ExprExtractor
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *  @author <a href="mailto:christiaan@dacelo.nl">Christiaan ten Klooster</a>
 *
 *  @version $Id: Eval.java,v 1.9 2004-08-15 16:09:38 mproctor Exp $
 */
public class Eval
    extends Interp
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Required decls. */
    private Declaration[] decls;

    //  private PyFunction evalFunc;
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected Eval(String text,
                   Declaration[] availDecls)
        throws Exception
    {
        super( text,
               "eval" );

        this.decls = analyze( availDecls );

/*
        String evalString = //"from org.drools.smf import SMFTestFrameWork\n" +
                            //"from org.drools.smf import SMFTestFrameWork\n" +
                            "def droolsFunction(code, globals, locals):\n" +
                            "  return eval(code, globals, locals)\n";

        Interp interp = new Interp(evalString, "exec");
        PythonInterpreter pythonInterpreter = new PythonInterpreter();
        pythonInterpreter.exec(interp.getCode());
        evalFunc = (PyFunction) pythonInterpreter.get("droolsFunction", PyFunction.class);
*/

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
    public Object evaluate(Tuple tuple) throws Exception
    {
        PyDictionary dict = setUpDictionary( tuple );

        return evaluate( dict );
    }

    /** Evaluate.
     *
     *  @param locals The evaluation dictionary.
     *
     *  @return The result of evaluation.
     */
    protected Object evaluate(PyDictionary locals) throws Exception
    {
        PyDictionary globals = new PyDictionary( new Hashtable() );

        PyObject result = __builtin__.eval(getCode(), globals, locals);

        return result.__tojava__(Object.class);
    }


    /** Evaluate.
     *
     *  @return The result of evaluation.
     */
    protected Object evaluate() throws Exception
    {
        PyDictionary locals = new PyDictionary( new Hashtable() );

        return evaluate( locals );
    }

    /** Retrieve the array of <code>Declaration</code>s required
     *  by this condition to perform its duties.
     *
     *  @return The array of <code>Declarations</code> expected
     *          on incoming <code>Tuples</code>.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return this.decls;
    }

    protected Declaration[] analyze(Declaration[] availDecls)
        throws Exception
    {
        ExprAnalyzer analyzer = new ExprAnalyzer();

        return analyzer.analyze( getNode(),
                                 availDecls );
    }
}
