package org.drools.semantics.java;

/*
 $Id: ExprAnalyzer.java,v 1.5 2002-09-19 06:58:00 bob Exp $

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
import org.drools.semantics.java.parser.JavaLexer;
import org.drools.semantics.java.parser.JavaRecognizer;
import org.drools.semantics.java.parser.JavaTreeParser;
import org.drools.semantics.java.parser.JavaTokenTypes;

import antlr.TokenStreamException;
import antlr.RecognitionException;
import antlr.collections.AST;

import java.io.StringReader;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** Expression analyzer.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: ExprAnalyzer.java,v 1.5 2002-09-19 06:58:00 bob Exp $
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
        // intentionally left blank.
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
     *  @throws TokenStreamException If an error occurs in the lexer.
     *  @throws RecognitionException If an error occurs in the parser.
     *  @throws MissingDeclarationException If the expression requires
     *          a declaration not present in the available declarations.
     */
    public Declaration[] analyze(String expr,
                                 Declaration[] availDecls)
        throws TokenStreamException, RecognitionException, MissingDeclarationException
    {
        JavaLexer lexer = new JavaLexer( new StringReader( expr ) );
        JavaRecognizer parser = new JavaRecognizer( lexer );

        parser.ruleCondition();

        AST ast = parser.getAST();

        return analyze( expr,
                        availDecls,
                        ast );
    }

    /** Analyze an expression.
     *
     *  @param expr The expression to analyze.
     *  @param availDecls Total set of declarations available.
     *  @param ast The AST for the expression.
     *
     *  @return The array of declarations used by the expression.
     *
     *  @throws RecognitionException If an error occurs in the parser.
     *  @throws MissingDeclarationException If the expression requires
     */
    private Declaration[] analyze(String expr,
                                  Declaration[] availDecls,
                                  AST ast) throws RecognitionException, MissingDeclarationException
    {
        JavaTreeParser treeParser = new JavaTreeParser();

        treeParser.init();

        treeParser.exprCondition( ast );

        Set availDeclSet = new HashSet();

        for ( int i = 0 ; i < availDecls.length ; ++i )
        {
            availDeclSet.add( availDecls[i] );
        }

        Set refs = new HashSet( treeParser.getVariableReferences() );

        Set declSet = new HashSet();

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

        /*
        if ( ! refs.isEmpty() )
        {
            throw new MissingDeclarationException( expr,
                                                   (String) refs.iterator().next() );
        }
        */

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
