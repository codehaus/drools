package org.drools.semantics.python;

/*
 $Id: Interp.java,v 1.8 2004-07-10 00:06:37 dbarnett Exp $

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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.ObjectType;
import org.drools.spi.Tuple;

import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyDictionary;
import org.python.core.PyString;
import org.python.core.parser;
import org.python.parser.ast.modType;
import org.python.util.PythonInterpreter;

/** Base class for Jython interpreter-based Python semantic components.
 *
 *  @see Eval
 *  @see Exec
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: Interp.java,v 1.8 2004-07-10 00:06:37 dbarnett Exp $
 */
public class Interp
{
    /** The line separator system property ("\n" on UNIX). */
    private static final String LINE_SEPARATOR =
        System.getProperty("line.separator");
    
    // ------------------------------------------------------------
    //     Class Initialization
    // ------------------------------------------------------------

    /** Ensure jpython gets initialized.
     */
    static
    {
        // throw it away.  we only need it for setting up
        // system state.
        new PythonInterpreter();
    }

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Text. */
    private String text;

    /** The code. */
    private PyCode code;

    /** The AST node. */
    private modType node;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected Interp(String text,
                     String type)
    {
        this.text = stripOuterIndention(text);
        this.node = (modType) parser.parse( this.text,
                                            type );
        this.code = Py.compile( this.node,
                                "<jython>");
    }
    
    /**
     * Trims leading indention from the block of text. Since Python relies on
     * indention as part of its syntax, any XML indention introduced needs to be
     * stripped out.  For example, this:
     * <pre>
     * |   &lt;python:consequence&gt;
     * |       if hello == 'Hello':
     * |           print "Hi"
     * |       else:
     * |           print "Bye"
     * |   &lt;/python:consequence&gt;
     * </pre>
     * is transformed into:
     * <pre>
     * |   &lt;python:consequence&gt;
     * |if hello == 'Hello':
     * |    print "Hi"
     * |else:
     * |    print "Bye"
     * |   &lt;/python:consequence&gt;
     * </pre>
     * 
     * @param text the block of text to be stripped
     * @return the block of text stripped of its leading indention
     */
    private static String stripOuterIndention(String text)
    {
        try
        {
            if (null == text)
            {
                return null;
            }
            
            BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    new ByteArrayInputStream(text.getBytes())));
            
            StringBuffer unindentedText = new StringBuffer(text.length());
            
            int lineNo = 0;
            try
            {
                String indent = null;
                for (String line = br.readLine(); null != line; line = br.readLine())
                {
                    lineNo++;
                    if ("".equals(line.trim()))
                    {
                        // Blank lines are passed through unmodified
                        unindentedText.append(line + LINE_SEPARATOR);
                        continue;
                    }
                    
                    if (null == indent)
                    {
                        // The first non-bank line determines
                        //   the outer indention level
                        indent = line.substring(0, line.indexOf(line.trim()));
                    }
                    
                    if (   (line.length() < indent.length())
                        || (!line.matches("^" + indent + ".*")) )
                    {
                        // This can catch some poorly indented Python syntax
                        throw new RuntimeException(
                            "Bad Text Indention: Line " + lineNo + ": |" +
                            formatForException(line) + "|" + LINE_SEPARATOR +
                            formatForException(text));
                    }
                    
                    // Remove the outer most indention from the line
                    unindentedText.append(
                        line.replaceFirst("^" + indent, "") + LINE_SEPARATOR);
                }
            } 
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            
            // Remove extraneous trailing LINE_SEPARATOR
            unindentedText.deleteCharAt(unindentedText.length() - 1);
            
            return unindentedText.toString();
        }
        catch (Exception e)
        {
            // [TODO]
            // The whole point of this try/catch block is to ensure that exceptions
            // make it out to the user; it seems something is swallowing everything
            // except RuntimeExceptions.
            if (e instanceof RuntimeException)
            {
                throw (RuntimeException) e;
            }
            
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Helper method to format the text block for display in error messages.
     * Since Python syntax errors can easily occur due to bad indention,
     * this method replaces all tabs with "{{tab}}" and all spaces with ".".
     * 
     * @param text the text to be formatted
     * @return the text with all tabs and spaces replaced for easier viewing
     */
    private static String formatForException(String text) {
        return text.replaceAll("\t", "{{tab}}").replace(' ', '.');
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the text to evaluate.
     *
     *  @return The text to evaluate.
     */
    public String getText()
    {
        return this.text;
    }

    /** Retrieve the AST node.
     *
     *  @return The node.
     */
    protected modType getNode()
    {
        return this.node;
    }

    /** Retrieve the compiled code.
     *
     *  @return The code.
     */
    protected PyCode getCode()
    {
        return this.code;
    }

    /** Configure a <code>PyDictionary</code> using a <code>Tuple</code>
     *  for variable bindings.
     *
     *  @param tuple Tuple containing variable bindings.
     *
     *  @return The dictionary
     */
    protected PyDictionary setUpDictionary(Tuple tuple) 
    {
        Hashtable table = new Hashtable();

        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        ObjectType objectType = null;

        PyDictionary dict = new PyDictionary();

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            dict.setdefault( new PyString( eachDecl.getIdentifier().intern() ),
                             Py.java2py( tuple.get( eachDecl ) ) );
        }

        WorkingMemory workingMemory = tuple.getWorkingMemory();
        
        dict.setdefault( new PyString( "drools".intern() ),
                         Py.java2py( new KnowledgeHelper( tuple ) ) );
        
        Map appDataMap = workingMemory.getApplicationDataMap();

        for ( Iterator keyIter = appDataMap.keySet().iterator();
              keyIter.hasNext(); )
        {
            String key   = (String) keyIter.next();
            Object value = appDataMap.get( key );

            dict.setdefault( new PyString( key.intern() ),
                             Py.java2py( value ) );
        }

        return dict;
    }
}
