
package org.drools.semantic.java.io;

import org.drools.semantic.java.JavaObjectType;
import org.drools.semantic.java.BeanShellFactExtractor;
import org.drools.semantic.java.BeanShellFilterCondition;

import org.drools.semantic.java.parser.JavaLexer;
import org.drools.semantic.java.parser.JavaRecognizer;
import org.drools.semantic.java.parser.JavaTreeParser;
import org.drools.semantic.java.parser.JavaTokenTypes;

import org.drools.spi.Declaration;
import org.drools.spi.AssignmentCondition;

import org.dom4j.Element;
import org.dom4j.ElementPath;

import antlr.TokenStreamException;
import antlr.RecognitionException;
import antlr.collections.AST;

import java.io.StringReader;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

class CondHandler extends BaseRuleSetHandler
{
    CondHandler(RuleSetReader reader)
    {
        super( reader );
    }

    public void onEnd(ElementPath path)
    {
        Element elem = path.getCurrent();

        String expr = elem.getTextTrim();

        JavaLexer lexer = new JavaLexer( new StringReader( expr ) );
        JavaRecognizer parser = new JavaRecognizer( lexer );

        try
        {
            parser.ruleCondition();

            AST ast = parser.getAST();

            analyze( expr,
                     ast );
        }
        catch (TokenStreamException e)
        {
            e.printStackTrace();
        }
        catch (RecognitionException e)
        {
            e.printStackTrace();
        }
    }

    private void analyze(String expr,
                         AST ast) throws RecognitionException
    {
        if ( ast.getType() == JavaTokenTypes.ASSIGN )
        {
            integrateAssignmentCondition( expr,
                                          ast );
        }
        else
        {
            integrateFilterCondition( expr,
                                      ast );
        }
    }

    private void integrateFilterCondition(String expr,
                                          AST ast) throws RecognitionException
    {
        JavaTreeParser treeParser = new JavaTreeParser();

        treeParser.init();

        treeParser.filterCondition( ast );

        List refs = treeParser.getVariableReferences();

        Set         decls    = new HashSet();
        Declaration eachDecl = null;

        Iterator    refIter = refs.iterator();
        String      eachRef = null;

        while ( refIter.hasNext() )
        {
            eachRef = (String) refIter.next();

            eachDecl = getReader().getDeclaration( eachRef );

            if ( eachDecl != null )
            {
                decls.add( eachDecl );
            }
        }

        BeanShellFilterCondition filterCond = new BeanShellFilterCondition( expr,
                                                                            decls );

        getReader().getCurrentRule().addFilterCondition( filterCond );
    }

    private void integrateAssignmentCondition(String expr,
                                              AST ast) throws RecognitionException
    {
        JavaTreeParser treeParser = new JavaTreeParser();

        treeParser.init();

        treeParser.assignmentCondition( ast );

        List refs = treeParser.getVariableReferences();

        String      identifier = (String) refs.get( 0 );
        Declaration targetDecl = getReader().getDeclaration( identifier );

        Set         rhsRefs  = new HashSet();
        String      eachRef  = null;
        Declaration eachDecl = null;

        for ( int i = 1 ; i < refs.size() ; ++i )
        {
            eachRef = (String) refs.get( i );
            eachDecl = getReader().getDeclaration( eachRef );

            if ( eachDecl != null )
            {
                rhsRefs.add( eachDecl );
            }
        }

        BeanShellFactExtractor extractor = new BeanShellFactExtractor( expr,
                                                                       rhsRefs );

        AssignmentCondition assignCond = new AssignmentCondition( targetDecl,
                                                                  extractor );

        getReader().getCurrentRule().addAssignmentCondition( assignCond );
    }
}

