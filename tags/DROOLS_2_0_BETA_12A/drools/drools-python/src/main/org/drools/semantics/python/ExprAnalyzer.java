package org.drools.semantics.python;

/*
 $Id: ExprAnalyzer.java,v 1.2 2003-03-25 19:47:32 tdiesler Exp $

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
import org.python.parser.ast.modType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** Analyzes python expressions for all mentioned variables.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: ExprAnalyzer.java,v 1.2 2003-03-25 19:47:32 tdiesler Exp $
 */
public class ExprAnalyzer
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public ExprAnalyzer()
    {
        // intentionally left blank
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Analyze an expression.
     *
     *  @param expr The expression to analyze.
     *  @param availDecls Total set of declarations available.
     *
     *  @return The array of declarations used by the expression.
     *
     *  @throws Exception If an error occurs while attempting
     *          to analyze the expression.
     */

    public Declaration[] analyze(modType expr,
                                 Declaration[] availDecls) throws Exception
    {
        ExprVisitor visitor = new ExprVisitor();

        Set availDeclSet = new HashSet();

        for ( int i = 0 ; i < availDecls.length ; ++i )
        {
            availDeclSet.add( availDecls[i] );
        }

        Set declSet = new HashSet();

        Set refs = (Set) visitor.eval_input( expr );

        Iterator    declIter = availDeclSet.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            if ( refs.contains( eachDecl.getIdentifier() ) )
            {
                declSet.add( eachDecl );
                declIter.remove();
                refs.remove( eachDecl.getIdentifier() );
            }
        }

        Declaration[] decls = new Declaration[ declSet.size() ];

        declIter = declSet.iterator();
        eachDecl = null;

        int i = 0;

        while ( declIter.hasNext() )
        {
            decls[ i++ ] = (Declaration) declIter.next();
        }

        return decls;

    }
}
