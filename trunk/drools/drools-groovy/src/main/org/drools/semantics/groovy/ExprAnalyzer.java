package org.drools.semantics.groovy;

/*
 * $Id: ExprAnalyzer.java,v 1.3 2004-09-17 00:36:28 mproctor Exp $
 * 
 * Copyright 2002 (C) The Werken Company. All Rights Reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 * 
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 * 
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 * 
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *  
 */

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.control.SourceUnit;
import org.drools.rule.Declaration;

/**
 * Analyzes python expressions for all mentioned variables.
 * 
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 * @author <a href="mailto:ckl@dacelo.nl">Christiaan ten Klooster </a>
 * 
 * @version $Id: ExprAnalyzer.java,v 1.3 2004-09-17 00:36:28 mproctor Exp $
 */
public class ExprAnalyzer
{

    public ExprAnalyzer()
    {
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /**
     * Analyze an expression.
     * 
     * @param expr The expression to analyze.
     * @param availDecls Total set of declarations available.
     * 
     * @return The array of declarations used by the expression.
     * 
     * @throws Exception If an error occurs while attempting to analyze the
     *         expression.
     */
    public Declaration[] analyze(String text, Declaration[] availDecls) throws Exception
    {
        Set availDeclSet = new HashSet( );
        for ( int i = 0; i < availDecls.length; ++i )
        {
            availDeclSet.add( availDecls[i] );
        }

        SourceUnit unit = SourceUnit.create( "groovy.script", text );
        unit.parse( );
        unit.convert( );
        ModuleNode module = unit.getAST( );

        ClassNode classNode = ( ClassNode ) module.getClasses( ).get( 0 );
        List methods = classNode.getDeclaredMethods( "run" );
        MethodNode method = ( MethodNode ) methods.get( 0 );
        ASTNode expr = ( BlockStatement ) method.getCode( );

        ExprVisitor visitor = new ExprVisitor( );
        expr.visit( visitor );
        Set refs = visitor.getVariables( );

        Set declSet = new HashSet( );
        for ( Iterator declIter = availDeclSet.iterator( ); declIter.hasNext( ); )
        {
            Declaration eachDecl = ( Declaration ) declIter.next( );

            if ( refs.contains( eachDecl.getIdentifier( ) ) )
            {
                declSet.add( eachDecl );
                declIter.remove( );
                refs.remove( eachDecl.getIdentifier( ) );
            }
        }

        Declaration[] decls = new Declaration[declSet.size( )];
        int i = 0;
        for ( Iterator declIter = declSet.iterator( ); declIter.hasNext( ); )
        {
            decls[i++] = ( Declaration ) declIter.next( );
        }
        return decls;
    }
}