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

    public Declaration[] analyze(Declaration[] availDecls,
                                 String expr) throws TokenStreamException, RecognitionException
    {
        JavaLexer lexer = new JavaLexer( new StringReader( expr ) );
        JavaRecognizer parser = new JavaRecognizer( lexer );

        parser.ruleCondition();

        AST ast = parser.getAST();

        return analyze( availDecls,
                        expr,
                        ast );
    }

    private Declaration[] analyze(Declaration[] availDecls,
                                  String expr,
                                  AST ast) throws RecognitionException
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

        Set refs = new HashSet( treeParser.getVariableReferences() );
        Set declSet = new HashSet();

        for ( int i = 0 ; i < availDecls.length ; ++i )
        {
            if ( refs.contains( availDecls[i].getIdentifier() ) )
            {
                declSet.add( availDecls[i] );
            }
        }

        Declaration[] decls = new Declaration[ declSet.size() ];

        Iterator    declIter = declSet.iterator();
        Declaration eachDecl = null;

        int i = 0;

        while ( declIter.hasNext() )
        {
            decls[ i++ ] = (Declaration) declIter.next();
        }

        return decls;
    }
}
