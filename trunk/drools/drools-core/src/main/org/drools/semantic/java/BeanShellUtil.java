package org.drools.semantic.java;

/*
 $Id: BeanShellUtil.java,v 1.4 2002-08-02 14:10:33 bob Exp $

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

/** Utilities for merging the Tuple into a form usable
 *  by <a href="http://beanshell.org/">BeanShell.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class BeanShellUtil
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Purely static methods.
     */
    private BeanShellUtil()
    {
        // intentionall left blank.
    }
    
    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set up the interpreter with bindings to the
     *  members of the tuple.
     *
     *  @param interp The interpreter to configure.
     *  @param tuple The tuple to map into the interpreter's namespace.
     *
     *  @throws EvalError If an error occurs while attempting to
     *                    set up the BeanShell interpreter.
     */
    public static void setUpInterpreter(Interpreter interp,
                                        Tuple tuple) throws EvalError
    {
        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            interp.set( eachDecl.getIdentifier(),
                        tuple.get( eachDecl ) );
        }
    }

    /** Clean up the interpreter's bindings to the
     *  members of the tuple.
     *
     *  @param interp The interpreter to clean up.
     *  @param tuple The tuple to unmap from the interpreter's namespace.
     *
     *  @throws EvalError If an error occurs while attempting to
     *                    clean up the BeanShell interpreter.
     */
    public static void cleanUpInterpreter(Interpreter interp,
                                          Tuple tuple) throws EvalError
    {
        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            interp.unset( eachDecl.getIdentifier() );
        }
    }
}
