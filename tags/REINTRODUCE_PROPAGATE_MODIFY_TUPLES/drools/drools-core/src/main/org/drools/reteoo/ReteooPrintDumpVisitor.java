package org.drools.reteoo;

/*
 * $Id: ReteooPrintDumpVisitor.java,v 1.6 2004-11-13 01:43:07 simon Exp $
 *
 * Copyright 2004-2004 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
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

import org.drools.RuleBase;
import org.drools.rule.Declaration;

import java.io.PrintStream;
import java.util.Iterator;

public class ReteooPrintDumpVisitor extends ReflectiveVisitor
{
    private PrintStream  out;

    private StringBuffer buffer;

    private int          depth;

    private String       indent;

    public ReteooPrintDumpVisitor(PrintStream out)
    {
        this.out = out;
        this.indent = "  ";
    }

    public ReteooPrintDumpVisitor(PrintStream out, String indent)
    {
        this.out = out;
        this.indent = indent;
    }

    public void visitRuleBase(RuleBase ruleBase)
    {
        buffer = new StringBuffer( );
        visit( ( ( RuleBaseImpl ) ruleBase ).getRete( ) );
        out.println( buffer.toString( ) );
    }

    public void visitRete(Rete rete)
    {
        buffer.append( "Rete" );
        buffer.append( newline );
        buffer.append( "----" );
        buffer.append( newline );
        Iterator it = rete.getObjectTypeNodeIterator( );
        int scopedDepth = depth;
        depth++;
        while ( it.hasNext( ) )
        {
            visit( it.next( ) );
        }
        depth = scopedDepth;
    }

    public void visitObjectTypeNode(ObjectTypeNode objectTypeNode)
    {
        String indent = getIndent( depth );
        buffer.append( indent ).append( "ObjectTypeNode" );
        buffer.append( newline );
        buffer.append( indent ).append( "--------------" );
        buffer.append( newline );
        buffer.append( indent ).append( "objectType: "
                       + objectTypeNode.getObjectType( ).toString( ) );
        buffer.append( newline );
        Iterator it = objectTypeNode.getParameterNodeIterator( );
        int scopedDepth = depth;
        depth++;
        while ( it.hasNext( ) )
        {
            visit( ( ParameterNode ) it.next( ) );
        }
        depth = scopedDepth;
    }

    public void visitParameterNode(ParameterNode parameterNode)
    {
        String indent = getIndent( depth );
        buffer.append( indent ).append( "ParameterNode" );
        buffer.append( newline );
        buffer.append( indent ).append( "-------------" );
        buffer.append( newline );
        int scopedDepth = depth;
        depth++;
        visit( parameterNode.getDeclaration( ) );
        buffer.append( indent ).append( "tupleSink:" );
        buffer.append( newline );
        visit( parameterNode.getTupleSink( ) );
        depth = scopedDepth;
    }

    public void visitDeclaration(Declaration declaration)
    {
        String indent = getIndent( depth );
        buffer.append( indent ).append( "Declaration" );
        buffer.append( newline );
        buffer.append( indent ).append( "-----------" );
        buffer.append( newline );
        buffer.append( indent ).append( "identifier: " );
        buffer.append( declaration.getIdentifier( ) );
        buffer.append( newline );
        buffer.append( indent ).append( "objectType: " );
        buffer.append( declaration.getObjectType( ) );
        buffer.append( newline );
    }

    public void visitConditionNode(ConditionNode conditionNode)
    {
        String indent = getIndent( depth );
        buffer.append( indent ).append( "ConditionNode" );
        buffer.append( newline );
        buffer.append( indent ).append( "-------------" );
        buffer.append( newline );
        buffer.append( indent ).append( "condition: " );
        buffer.append( conditionNode.toString( ) );
        buffer.append( newline );
        buffer.append( indent ).append( "tupleSink:" );
        buffer.append( newline );
        int scopedDepth = depth;
        depth++;
        visit( conditionNode.getTupleSink( ) );
        depth = scopedDepth;
    }

    public void visitExtractionNode(ExtractionNode extractionNode)
    {
        String indent = getIndent( depth );
        buffer.append( indent ).append( "ExtractionNode" );
        buffer.append( newline );
        buffer.append( indent ).append( "-------------" );
        buffer.append( newline );
        buffer.append( indent ).append( "extraction: " );
        buffer.append( extractionNode.toString( ) );
        buffer.append( newline );
        buffer.append( indent ).append( "tupleSink:" );
        buffer.append( newline );
        int scopedDepth = depth;
        depth++;
        visit( extractionNode.getTupleSink( ) );
        depth = scopedDepth;
    }

    public void visitJoinNodeInput(JoinNodeInput joinNodeInput)
    {
        String indent = getIndent( depth );
        buffer.append( indent ).append( "JoinNodeInput" );
        buffer.append( newline );
        buffer.append( indent ).append( "-------------" );
        buffer.append( newline );
        buffer.append( indent ).append( joinNodeInput.toString( ) );
        buffer.append( newline );
        int scopedDepth = depth;
        depth++;
        visit( joinNodeInput.getJoinNode( ).getTupleSink( ) );
        depth = scopedDepth;
    }

    public void visitTerminalNode(TerminalNode terminalNode)
    {
        String indent = getIndent( depth );
        buffer.append( indent ).append( "TerminalNode" );
        buffer.append( newline );
        buffer.append( indent ).append( "-------------" );
        buffer.append( newline );
        buffer.append( indent ).append( terminalNode.toString( ) );
        buffer.append( newline );
    }

    public void visitObject(Object object)
    {
        String indent = getIndent( depth );
        buffer.append( "no visitor implementation for : " + object.getClass( )
                       + " : " + object );
        buffer.append( newline );
    }

    public void visitNull()
    {
        String indent = getIndent( depth );
        buffer.append( "unable to visit null objects" );
        buffer.append( newline );
    }

    private String getIndent(int depth)
    {
        StringBuffer indentBuffer = new StringBuffer( );
        for ( int i = 0; i < depth; i++ )
        {
            indentBuffer.append( indent );
        }
        return indentBuffer.toString( );
    }
}