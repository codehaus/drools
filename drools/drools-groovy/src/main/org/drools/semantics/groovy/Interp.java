package org.drools.semantics.groovy;

/*
 $Id: Interp.java,v 1.1 2003-12-09 19:54:06 jstrachan Exp $

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

import groovy.lang.Script;
import groovy.lang.ScriptContext;

import java.util.Iterator;
import java.util.Set;

import org.codehaus.groovy.ast.ASTNode;
import org.drools.rule.Declaration;
import org.drools.spi.Tuple;

/** Base class for Groovy based semantic components.
 *
 *  @see Eval
 *  @see Exec
 *
 *  @author <a href="mailto:james@coredevelopers.net">James Strachan</a>  
 *
 *  @version $Id: Interp.java,v 1.1 2003-12-09 19:54:06 jstrachan Exp $
 */
public class Interp {
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Text. */
    private String text;
    private Script code;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected Interp(String text, String type) {
        this.text = text;

        /** @todo compile the code to some Object... */
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the text to evaluate.
     *
     *  @return The text to evaluate.
     */
    public String getText() {
        return this.text;
    }

    protected Script getCode() {
        return code;
    }
    
    protected ASTNode getNode() {
        return getCode().getMetaClass().getClassNode().getMethod("run").getCode();
    }

    /** Configure a <code>ScriptContext</code> using a <code>Tuple</code>
     *  for variable bindings.
     *
     *  @param tuple Tuple containing variable bindings.
     *
     *  @return The dictionary
     */
    protected ScriptContext setUpDictionary(Tuple tuple) {
        Set decls = tuple.getDeclarations();
        ScriptContext dict = new ScriptContext();

        for (Iterator declIter = decls.iterator(); declIter.hasNext();) {
            Declaration eachDecl = (Declaration) declIter.next();

            dict.setVariable(eachDecl.getIdentifier().intern(), tuple.get(eachDecl));
        }
        return dict;
    }
}
