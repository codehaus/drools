package org.drools.semantics.python;

/*
 $Id: Interp.java,v 1.7 2004-06-30 21:46:33 bob Exp $

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

import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.spi.ObjectType;
import org.drools.spi.Tuple;
import org.drools.spi.KnowledgeHelper;

import org.python.core.*;
import org.python.parser.ast.modType;
import org.python.util.PythonInterpreter;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;

/** Base class for Jython interpreter-based Python semantic components.
 *
 *  @see Eval
 *  @see Exec
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: Interp.java,v 1.7 2004-06-30 21:46:33 bob Exp $
 */
public class Interp
{
    // ------------------------------------------------------------
    //     Class Initialization
    // ------------------------------------------------------------

    /** Ensure jpython gets initialized.
     */
    static
    {
        // throw it away.  we only need it for setting up
        // system state.
        new PythonInterpreter();
    }

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Text. */
    private String text;

    /** The code. */
    private PyCode code;

    /** The AST node. */
    private modType node;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected Interp(String text,
                     String type)
    {
        this.text = text;
        this.node = (modType) parser.parse( text,
                                            type );
        this.code = Py.compile( this.node,
                                "<jython>");
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the text to evaluate.
     *
     *  @return The text to evaluate.
     */
    public String getText()
    {
        return this.text;
    }

    /** Retrieve the AST node.
     *
     *  @return The node.
     */
    protected modType getNode()
    {
        return this.node;
    }

    /** Retrieve the compiled code.
     *
     *  @return The code.
     */
    protected PyCode getCode()
    {
        return this.code;
    }

    /** Configure a <code>PyDictionary</code> using a <code>Tuple</code>
     *  for variable bindings.
     *
     *  @param tuple Tuple containing variable bindings.
     *
     *  @return The dictionary
     */
    protected PyDictionary setUpDictionary(Tuple tuple) 
    {
        Hashtable table = new Hashtable();

        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        ObjectType objectType = null;

        PyDictionary dict = new PyDictionary();

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            dict.setdefault( new PyString( eachDecl.getIdentifier().intern() ),
                             Py.java2py( tuple.get( eachDecl ) ) );
        }

        WorkingMemory workingMemory = tuple.getWorkingMemory();
        
        dict.setdefault( new PyString( "drools".intern() ),
                         Py.java2py( new KnowledgeHelper( tuple ) ) );
        
        Map appDataMap = workingMemory.getApplicationDataMap();

        for ( Iterator keyIter = appDataMap.keySet().iterator();
              keyIter.hasNext(); )
        {
            String key   = (String) keyIter.next();
            Object value = appDataMap.get( key );

            dict.setdefault( new PyString( key.intern() ),
                             Py.java2py( value ) );
        }

        return dict;
    }
}
