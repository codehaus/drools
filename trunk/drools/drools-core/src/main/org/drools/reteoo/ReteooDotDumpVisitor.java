package org.drools.reteoo;

/*
 * $Id: ReteooDotDumpVisitor.java,v 1.11 2005-02-02 00:23:22 mproctor Exp $
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.drools.rule.Declaration;

/**
 * Produces a graph in GraphViz DOT format.
 *
 * @see http://www.research.att.com/sw/tools/graphviz/
 * @see http://www.pixelglow.com/graphviz/
 *
 * @author Andy Barnett
 */
public class ReteooDotDumpVisitor extends ReflectiveVisitor
{
    /** String displayed for Null values. */
    private static final String NULL_STRING = "<NULL>";

    /** Amount of indention for Node and Edge lines. */
    private static final String INDENT = "    ";

    /** The PrintStream where the DOT output will be written. */
    private final PrintStream out;

    /**
     * Keeps track of visited JoinNode DOT IDs. This mapping allows the visitor
     * to recognize JoinNodes it has already visited and as a consequence link
     * existing nodes back together. This is vital to the Visitor being able to
     * link two JoinNodeInputs together through their common JoinNode.
     */
    private Set visitedNodes = new HashSet( );

    /** Counter used to produce distinct DOT IDs for Null Nodes. */
    private int nullDotId;

    /**
     * Constructor.
     */
    public ReteooDotDumpVisitor(PrintStream outPrintStream)
    {
        out = outPrintStream;
    }

    /**
     * Default visitor if an unknown object is visited.
     */
    public void visitObject(Object object)
    {
        makeNode( object,
                  "Unknown Object",
                  "object: " + object + newline + "class: " + object.getClass( ) );
    }

    /**
     * Null visitor if a NULL object gets visited. Unique String objects are
     * generated to ensure every NULL object is distinct.
     */
    public void visitNull()
    {
        makeNode( "NULL" + nullDotId++,
                  NULL_STRING );
    }

    /**
     * RuleBaseImpl visits its Rete.
     */
    public void visitRuleBaseImpl(RuleBaseImpl ruleBase)
    {
        visit( ruleBase.getRete( ) );
    }

    /**
     * Rete visits each of its ObjectTypeNodes.
     */
    public void visitRete(Rete rete)
    {
        makeNode( rete,
                  "RETE-OO" );
        for ( Iterator i = rete.getObjectTypeNodeIterator( ); i.hasNext( ); )
        {
            Object nextNode = i.next( );
            makeEdge( rete,
                      nextNode );
            visitNode( nextNode );
        }
    }

    /**
     * ObjectTypeNode displays its objectType and then visits each of its
     * ParameterNodes.
     */
    public void visitObjectTypeNode(ObjectTypeNode node)
    {
        makeNode( node,
                  "ObjectTypeNode",
                  "objectType: " + node.getObjectType( ) );
        for ( Iterator i = node.getParameterNodeIterator( ); i.hasNext( ); )
        {
            Object nextNode = i.next( );
            makeEdge( node,
                      nextNode );
            visitNode( nextNode );
        }
    }

    /**
     * ParameterNode displays its declaration and then visits its TupleSink.
     */
    public void visitParameterNode(ParameterNode node)
    {
        makeTupleSourceNode( node, "ParameterNode", "TupleSource", "decl: " + format( node.getDeclaration( ) ) );
    }

    private void makeTupleSourceNode( TupleSource node, String nodeType, String tupleType, String label )
    {
        makeNode( node,
                  nodeType,
                  tupleType,
                  label );
        for ( Iterator i = node.getTupleSinks( ).iterator(); i.hasNext(); )
        {
            Object nextNode = i.next();
            makeEdge( node,
                      nextNode );
            visitNode( nextNode );
        }
    }

    /**
     * ConditionNode displays its condition and tuple Declarations and then
     * visits its TupleSink.
     */
    public void visitConditionNode(ConditionNode node)
    {
        makeTupleSourceNode( node,
                  "ConditionNode",
                  "TupleSource/TupleSink",
                  "condition: " + node.getCondition( ) + newline + format( node.getTupleDeclarations( ),
                                                                           "tuple" ) );
    }

    /**
     * JoinNodeInput displays its side (LEFT/RIGHT) and then visits its
     * JoinNode.
     */
    public void visitJoinNodeInput(JoinNodeInput node)
    {
        makeNode( node,
                  "JoinNodeInput",
                  "TupleSink",
                  node.getSide( ) == JoinNodeInput.LEFT ? "LEFT" : "RIGHT" );
        Object nextNode = node.getJoinNode( );
        makeEdge( node,
                  nextNode );
        visitNode( nextNode );
    }

    /**
     * JoinNode displays its common Declarations and tuple Declarations and then
     * visits its TupleSink.
     */
    public void visitJoinNode(JoinNode node)
    {
        makeTupleSourceNode( node,
                  "JoinNode",
                  "TupleSource",
                  format( node.getCommonDeclarations( ),
                          "common" ) + newline + format( node.getTupleDeclarations( ),
                                                         "tuple" ) );
    }

    /**
     * TerminalNode displays its rule.
     */
    public void visitTerminalNode(TerminalNode node)
    {
        makeNode( node,
                  "TerminalNode",
                  "TupleSink",
                  "rule: " + node.getRule( ).getName( ) );
    }

    /**
     * Helper method to ensure nodes are not visited more than once.
     */
    private void visitNode(Object node)
    {
        if ( !visitedNodes.contains( dotId( node ) ) )
        {
            visitedNodes.add( dotId( node ) );
            visit( node );
        }
    }

    /**
     * Helper method for makeNode().
     */
    private void makeNode(Object object,
                          String type,
                          String label)
    {
        makeNode( object,
                  type,
                  null,
                  label );
    }

    /**
     * Helper method for makeNode().
     */
    private void makeNode(Object object,
                          String nodeType,
                          String tupleType,
                          String label)
    {
        makeNode( object,
                  nodeType + "@" + dotId( object ) + newline + (null == tupleType ? "" : "(" + tupleType + ")" + newline ) + label );
    }

    /**
     * Outputs a DOT node line: "ID" [label="..."];
     */
    private void makeNode(Object object,
                          String label)
    {
        out.println( INDENT + "\"" + dotId( object ) + "\" " +
            "[" + getStyle( object ) + ", label=\"" + format( label ) + "\"];" );
    }

    /**
     * Outputs a DOT edge line: "FROM_ID" -> "TO_ID";
     */
    private void makeEdge(Object fromNode,
                          Object toNode)
    {
        out.println( INDENT + "\"" + dotId( fromNode ) + "\" -> \"" + dotId( toNode ) + "\";" );
    }

    /**
     * The identity hashCode for the given object is used as its unique DOT
     * identifier.
     */
    private static String dotId(Object object)
    {
        return Integer.toHexString( System.identityHashCode( object ) ).toUpperCase( );
    }

    /**
     * Formats a Set of Declarations for display.
     */
    private String format(Set declarationSet,
                          String declString)
    {
        if ( declarationSet.isEmpty( ) )
        {
            return "No " + declString + " declarations";
        }

        Declaration[] declarations = (Declaration[]) declarationSet.toArray( new Declaration[]{} );

        StringBuffer label = new StringBuffer( );
        int i = 0;
        for ( int max = declarations.length - 1; i < max; i++ )
        {
            label.append( declString + "Decl: " + format( declarations[i] ) + newline );
        }
        label.append( declString + "Decl: " + format( declarations[i] ) );

        return label.toString( );
    }

    /**
     * Formats a single Declaration for display.
     */
    private static String format(Declaration declaration)
    {
        return null == declaration ? NULL_STRING : declaration.getIdentifier( ) + " (" + declaration.getObjectType( ) + ")";
    }

    /**
     * Formats text for compatibility with GraphViz DOT.
     * Converting line-breaks into '\' + 'n'.
     * Escapes double-quotes.
     */
    private static String format(String label)
    {
        if ( null == label )
        {
            return NULL_STRING;
        }

        BufferedReader br = new BufferedReader( new InputStreamReader( new ByteArrayInputStream( label.getBytes( ) ) ) );

        StringBuffer buffer = new StringBuffer( );
        try
        {
            boolean firstLine = true;
            for ( String line = br.readLine( ); null != line; line = br.readLine( ) )
            {
                if ( line.trim( ).length( ) == 0 )
                {
                    continue;
                }
                if ( firstLine )
                {
                    firstLine = false;
                }
                else
                {
                    buffer.append( "\\n" );
                }
                
                if ( -1 == line.indexOf( '"' ) )
                {
                    buffer.append( line );
                }
                else
                {
                    for ( int i = 0, max = line.length( ); i < max; ++i )
                    {
                        char c = line.charAt( i );
                        if ( '"' == c )
                        {
                            buffer.append( '\\' + c );
                        }
                        else
                        {
                            buffer.append( c );
                        }
                    }
                }
            }
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Error formatting '" + label + "': " + e.getMessage( ) );
        }

        return buffer.toString( );
    }

    /**
     * Returns the desired Node shape for the given object.
     * 
     * @param node The Rete graph Node to be formatted
     * 
     * @return a DOT style-string for the given Node
     */
    private String getStyle( Object node )
    {
        String style;
        
        if ( node instanceof Rete )
        {
            // Preferably this would be a "rectangle" with rounded sides
            // but GraphViz doesn't support "rounded" & "filled" on one node.
            style = "style=\"filled\", shape=\"ellipse\"";
        }
        else if ( node instanceof ObjectTypeNode )
        {
            style = "style=\"filled\", fillcolor=\"cyan4\", shape=\"rectangle\"";
        }
        else if ( node instanceof ParameterNode )
        {
            style = "style=\"filled\", fillcolor=\"cyan3\", shape=\"rectangle\"";
        }
        else if ( node instanceof ConditionNode )
        {
            style = "style=\"filled\", fillcolor=\"yellow3\", shape=\"diamond\"";
        }
        else if ( node instanceof JoinNodeInput )
        {
            style = "style=\"filled\", fillcolor=\"chartreuse\", shape=\"invtriangle\"";
        }
        else if ( node instanceof JoinNode )
        {
            style = "style=\"filled\", fillcolor=\"green\", shape=\"house\"";
        }
        else if ( node instanceof TerminalNode )
        {
            // Preferably this would be a "rectangle" with rounded sides
            // but GraphViz doesn't support "rounded" & "filled" on one node.
            style = "style=\"filled\", shape=\"ellipse\"";
        }
        else
        {
            // Preferably this would be a "rectangle" with rounded sides
            // but GraphViz doesn't support "rounded" & "filled" on one node.
            style = "style=\"filled\", fillcolor=\"yellow3\", shape=\"ellipse\"";
        }
        
        return style;
    }
}
