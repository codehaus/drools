package org.drools.semantics.java;

import org.drools.rule.Declaration;
import org.drools.semantics.java.parser.JavaLexer;
import org.drools.semantics.java.parser.JavaRecognizer;
import org.drools.semantics.java.parser.JavaTreeParser;
import org.drools.semantics.java.parser.JavaTokenTypes;

import antlr.TokenStreamException;
import antlr.RecognitionException;
import antlr.collections.AST;

import java.io.StringReader;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class ExprAnalyzer
{
    public ExprAnalyzer()
    {

    }

    public Declaration[] analyze(String expr,
                                 Declaration[] availDecls) throws TokenStreamException, RecognitionException, MissingDeclarationException
    {
        JavaLexer lexer = new JavaLexer( new StringReader( expr ) );
        JavaRecognizer parser = new JavaRecognizer( lexer );

        parser.ruleCondition();

        AST ast = parser.getAST();

        return analyze( expr,
                        availDecls,
                        ast );
    }

    private Declaration[] analyze(String expr,
                                  Declaration[] availDecls,
                                  AST ast) throws RecognitionException, MissingDeclarationException
    {
        JavaTreeParser treeParser = new JavaTreeParser();

        treeParser.init();

        if ( ast.getType() == JavaTokenTypes.ASSIGN )
        {
            treeParser.assignmentCondition( ast );
        }
        else
        {
            treeParser.exprCondition( ast );
        }

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

        if ( ! refs.isEmpty() )
        {
            throw new MissingDeclarationException( expr,
                                                   (String) refs.iterator().next() );
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
