package org.drools.semantics.python;

/*
 * $Id: PythonInterp.java,v 1.7.2.2 2005-04-30 13:49:43 mproctor Exp $
 *
 * Copyright 2002-2004 (C) The Werken Company. All Rights Reserved.
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.Functions;
import org.drools.spi.ObjectType;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.Tuple;
import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyDictionary;
import org.python.core.PyModule;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.core.parser;
import org.python.parser.ast.modType;

/**
 * Base class for Jython interpreter-based Python semantic components.
 * 
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
public class PythonInterp
{
    /** The line separator system property ("\n" on UNIX). */
    private static final String LINE_SEPARATOR = System.getProperty( "line.separator" );

    // ------------------------------------------------------------
    // Class Initialization
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The rule. */
    private final Rule          rule;

    /** Text. */
    private final String        text;

    /** Original Text */
    private final String        origininalText;

    /** The code. */
    private final PyCode        code;

    /** The AST node. */
    private final modType       node;

    private PyDictionary        globals;

    /**
     * Initialise Jython's PySystemState
     */
    static
    {
        PySystemState.initialize( );

        PySystemState systemState = Py.getSystemState( );
        if ( systemState == null )
        {
            systemState = new PySystemState( );
        }
        Py.setSystemState( systemState );
    }

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    protected PythonInterp(String text,
                           Rule rule,
                           String type)
    {
        this.rule = rule;
        this.origininalText = text;
        StringBuffer globalText = new StringBuffer( );

        Iterator it = rule.getImporter( ).getImports( PythonImportEntry.class ).iterator( );

        while ( it.hasNext( ) )
        {
            globalText.append( it.next( ) );
            globalText.append( ";" );
            globalText.append( LINE_SEPARATOR );
        }

        globalText.append( "def q(cond,on_true,on_false):\n" );
        globalText.append( "  if cond:\n" );
        globalText.append( "    return on_true\n" );
        globalText.append( "  else:\n" );
        globalText.append( "    return on_false\n" );
        Functions functions = rule.getRuleSet( ).getFunctions( "python" );
        if ( functions != null )
        {
            globalText.append( stripOuterIndention( functions.getText( ) ) );
        }

        if ( this.globals == null )
        {
            this.globals = getGlobals( globalText.toString( ) );
        }

        this.text = stripOuterIndention( text );

        try
        {
            this.node = (modType) parser.parse( this.text,
                                                type );
            this.code = Py.compile( this.node,
                                    "<jython>" );
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
            throw new RuntimeException( e.getLocalizedMessage( ) );
        }
    }

    /**
     * Parses a python script and returns the globals It is used to be able to
     * inject imports and functions into code when being executed by
     * Py.runCode(...)
     * 
     * @param text
     * @return PyDictionary globals
     */
    public PyDictionary getGlobals(String text)
    {
        PyModule module = new PyModule( "main",
                                        new PyDictionary( ) );

        PyObject locals = module.__dict__;

        Py.exec( Py.compile_flags( text,
                                   "<string>",
                                   "exec",
                                   null ),
                 locals,
                 locals );

        return (PyDictionary) locals;
    }

    /**
     * Trims leading indention from the block of text. Since Python relies on
     * indention as part of its syntax, any XML indention introduced needs to be
     * stripped out. For example, this:
     * 
     * <pre>
     * 
     *  
     *  
     *    |   &lt;python:consequence&gt;
     *    |       if hello == 'Hello':
     *    |           print &quot;Hi&quot;
     *    |       else:
     *    |           print &quot;Bye&quot;
     *    |   &lt;/python:consequence&gt;
     *  
     *   
     *  
     * </pre>
     * 
     * is transformed into:
     * 
     * <pre>
     * 
     *  
     *  
     *    |   &lt;python:consequence&gt;
     *    |if hello == 'Hello':
     *    |    print &quot;Hi&quot;
     *    |else:
     *    |    print &quot;Bye&quot;
     *    |   &lt;/python:consequence&gt;
     *  
     *   
     *  
     * </pre>
     * 
     * @param text
     *            the block of text to be stripped
     * @return the block of text stripped of its leading indention
     */
    protected static String stripOuterIndention(String text)
    {
        try
        {
            if ( null == text )
            {
                return null;
            }

            BufferedReader br = new BufferedReader( new InputStreamReader( new ByteArrayInputStream( text.getBytes( ) ) ) );

            StringBuffer unindentedText = new StringBuffer( text.length( ) );

            int lineNo = 0;
            try
            {
                String indent = null;
                for ( String line = br.readLine( ); null != line; line = br.readLine( ) )
                {
                    lineNo++;
                    if ( "".equals( line.trim( ) ) )
                    {
                        // Blank lines are passed through unmodified
                        unindentedText.append( line + LINE_SEPARATOR );
                        continue;
                    }

                    if ( null == indent )
                    {
                        // The first non-bank line determines
                        // the outer indention level
                        indent = line.substring( 0,
                                                 line.indexOf( line.trim( ) ) );
                    }

                    if ( line.length( ) < indent.length( ) || !line.startsWith( indent ) )
                    {
                        // This can catch some poorly indented Python syntax
                        throw new RuntimeException( "Bad Text Indention: Line " + lineNo + ": |" + formatForException( line ) + "|" + LINE_SEPARATOR + formatForException( text ) );
                    }

                    // Remove the outer most indention from the line
                    if ( line.startsWith( indent ) )
                    {
                        unindentedText.append( line.substring( indent.length( ) ) );
                    }
                    unindentedText.append( LINE_SEPARATOR );
                }
            }
            catch ( IOException e )
            {
                throw new RuntimeException( e.getMessage( ) );
            }

            // Remove extraneous trailing LINE_SEPARATOR
            if ( unindentedText.length( ) > 0 )
            {
                unindentedText.deleteCharAt( unindentedText.length( ) - 1 );
            }

            return unindentedText.toString( );
        }
        catch ( Exception e )
        {
            // [TODO]
            // The whole point of this try/catch block is to ensure that
            // exceptions make it out to the user; it seems something is
            // swallowing everything except RuntimeExceptions.
            if ( e instanceof RuntimeException )
            {
                throw (RuntimeException) e;
            }

            throw new RuntimeException( e.getMessage( ) );
        }
    }

    /**
     * Helper method to format the text block for display in error messages.
     * Since Python syntax errors can easily occur due to bad indention, this
     * method replaces all tabs with "{{tab}}" and all spaces with ".".
     * 
     * @param text
     *            the text to be formatted
     * @return the text with all tabs and spaces replaced for easier viewing
     */
    private static String formatForException(String text)
    {
        StringBuffer sbuf = new StringBuffer( text.length( ) * 2 );
        for ( int i = 0, max = text.length( ); i < max; i++ )
        {
            final char nextChar = text.charAt( i );
            if ( '\t' == nextChar )
            {
                sbuf.append( "{{tab}}" );
            }
            else
            {
                sbuf.append( nextChar );
            }
        }

        return sbuf.toString( );
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the text to evaluate.
     * 
     * @return The text to evaluate.
     */
    public String getText()
    {
        return this.origininalText;
    }

    protected Rule getRule()
    {
        return this.rule;
    }

    /**
     * Retrieve the compiled code.
     * 
     * @return The code.
     */
    protected PyCode getCode()
    {
        return this.code;
    }

    /**
     * Retrieve the AST node.
     * 
     * @return The node.
     */
    protected modType getNode()
    {
        return this.node;
    }

    protected PyDictionary getGlobals()
    {
        return this.globals;
    }

    /**
     * Configure a <code>PyDictionary</code> using a <code>Tuple</code> for
     * variable bindings.
     * 
     * @param tuple
     *            Tuple containing variable bindings.
     * 
     * @return The dictionary
     */
    protected PyDictionary setUpDictionary(Tuple tuple,
                                           Iterator declIter) throws Exception
    {
        Declaration eachDecl;

        ObjectType objectType;
        String type;
        Class clazz;
        int nestedClassPosition;
        int dotPosition;

        PyDictionary dict = new PyDictionary( );

        // dict.setdefault( new PyString( "q" ), qFunc ); //add tenerary
        // function

        RuleBaseContext ruleBaseContext = rule.getRuleSet( ).getRuleBaseContext( );
        ClassLoader cl = (ClassLoader) ruleBaseContext.get( "smf-classLoader" );
        if ( cl == null )
        {
            cl = Thread.currentThread( ).getContextClassLoader( );
            ruleBaseContext.put( "smf-classLoader",
                                 cl );
        }

        if ( cl == null )
        {
            cl = getClass( ).getClassLoader( );
            ruleBaseContext.put( "smf-classLoader",
                                 cl );
        }

        while ( declIter.hasNext( ) )
        {
            eachDecl = (Declaration) declIter.next( );

            dict.setdefault( new PyString( eachDecl.getIdentifier( ).intern( ) ),
                             Py.java2py( tuple.get( eachDecl ) ) );

            objectType = eachDecl.getObjectType( );

            if ( objectType instanceof ClassObjectType )
            {
                clazz = ((ClassObjectType) objectType).getType( );
                type = clazz.getName( );

                nestedClassPosition = type.indexOf( '$' );

                if ( nestedClassPosition != -1 )
                {
                    type = type.substring( 0,
                                           nestedClassPosition );
                    clazz = cl.loadClass( type );
                }

                if ( type.indexOf( "java.lang" ) == -1 )
                {
                    dotPosition = type.lastIndexOf( '.' );
                    if ( dotPosition != -1 )
                    {
                        type = type.substring( dotPosition + 1 );
                    }
                    dict.setdefault( new PyString( type.intern( ) ),
                                     Py.java2py( clazz ) );
                }
            }

            WorkingMemory workingMemory = tuple.getWorkingMemory( );

            dict.setdefault( new PyString( "drools".intern( ) ),
                             Py.java2py( new DefaultKnowledgeHelper( this.rule,
                                                                     tuple ) ) );

            Map appDataMap = workingMemory.getApplicationDataMap( );

            for ( Iterator keyIter = appDataMap.keySet( ).iterator( ); keyIter.hasNext( ); )
            {
                String key = (String) keyIter.next( );
                Object value = appDataMap.get( key );

                dict.setdefault( new PyString( key.intern( ) ),
                                 Py.java2py( value ) );

                clazz = value.getClass( );
                type = clazz.getName( );

                nestedClassPosition = type.indexOf( '$' );

                if ( nestedClassPosition != -1 )
                {
                    type = type.substring( 0,
                                           nestedClassPosition );
                    clazz = cl.loadClass( type );
                }

                if ( type.indexOf( "java.lang" ) == -1 )
                {
                    dotPosition = type.lastIndexOf( '.' );
                    if ( dotPosition != -1 )
                    {
                        type = type.substring( dotPosition + 1 );
                    }
                    dict.setdefault( new PyString( type.intern( ) ),
                                     Py.java2py( clazz ) );
                }
            }
        }

        return dict;
    }
}
