package org.drools.semantics.python;

/*
 $Id: Exec.java,v 1.4 2002-10-11 15:01:10 ckl Exp $

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

import org.drools.spi.Tuple;

import org.drools.rule.Declaration;
import org.drools.smf.ConfigurationException;

import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.util.PythonInterpreter;

import java.util.Hashtable;

/** Base class for Jython statement-based Python semantic components.
 *
 *  @see BlockConsequence
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *  @author <a href="mailto:christiaan@dacelo.nl">Christiaan ten Klooster</a>
 *
 *  @version $Id: Exec.java,v 1.4 2002-10-11 15:01:10 ckl Exp $
 */
public class Exec extends Interp
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected Exec()
    {
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Execute.
     *
     *  @param tuple Tuple containing variable bindings.
     */
    public void execute(Tuple tuple) 
    {
        PyDictionary dict = setUpDictionary( tuple );
        
        execute( dict );
    }

    /** Execute.
     *
     *  @param locals The evaluation dictionary.
     */
    protected void execute(PyDictionary locals) 
    {
        PyDictionary globals = new PyDictionary( new Hashtable() );
        
        Py.runCode( getCode(),
                    locals,
                    globals );

    }

    /** Execute.
     */
    protected void execute()
    {
        PyDictionary locals = new PyDictionary( new Hashtable() );
        
        execute( locals );
    }
    
    /** Set the text to execute.
     *
     *  @param text The text.
     */
    protected void setText(String text)
    {
        setText( text,
                 "exec" );
    }

    /** Configure.
     *
     *  @param text Configuration text.
     *  @param availDecls Available declarations.
     *
     *  @throws ConfigurationException If an error occurs while
     *          attempting to perform configuration.
     */
    public void configure(String text,
                          Declaration[] availDecls) throws ConfigurationException
    {
        setText( text );
    }
}