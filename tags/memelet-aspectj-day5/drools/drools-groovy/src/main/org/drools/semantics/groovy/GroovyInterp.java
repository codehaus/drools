package org.drools.semantics.groovy;

/*
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

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.Script;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.Functions;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.Tuple;

/**
 * Base class for Groovy based semantic components.
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan </a>
 * @author <a href="mailto:ckl@dacelo.nl">Christiaan ten Klooster </a>
 */
public class GroovyInterp
    implements
    Serializable
{
    private static final String LINE_SEPARATOR = System.getProperty( "line.separator" );

    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** Text. */
    private final String        originalText;
    
    private String              text;

    /** The rule. */
    private final Rule          rule;

    private transient Script    code;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    protected GroovyInterp(String text,
                           Rule rule)
    {
        this.rule = rule;
        this.originalText = text;
        try
        {
            StringBuffer newText = new StringBuffer( );

            Iterator it = rule.getImporter( ).getImports( ).iterator( );
            while ( it.hasNext( ) )
            {
                newText.append( "import " );
                newText.append( it.next( ) );
                newText.append( ";" );
                newText.append( LINE_SEPARATOR );
            }

            Functions functions = this.rule.getRuleSet( ).getFunctions( "groovy" );
            if ( functions != null )
            {
                newText.append( functions.getText( ) );
            }

            if ( this instanceof GroovyCondition )
            {
                text = "return (" + text + ")";
            }
            newText.append( text );
            this.text = newText.toString();
            this.code = buildScript( );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e.getLocalizedMessage( ) );
        }
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
        return this.originalText;
    }

    protected Script getCode()
    {
        if ( this.code == null )
        {
            try
            {
                this.code = buildScript( );
            }
            catch( Exception e )
            {
                throw new RuntimeException( e.getLocalizedMessage( ) );
            }
        }
        return this.code;
    }

    protected Rule getRule()
    {
        return this.rule;
    }

    /**
     * Configure a <code>ScriptContext</code> using a <code>Tuple</code> for
     * variable bindings.
     * 
     * @param tuple
     *            Tuple containing variable bindings.
     * 
     * @return The dictionary
     */
    protected Binding setUpDictionary(Tuple tuple,
                                      Iterator declIter)
    {
        Binding dict = new Binding( );
        Declaration eachDecl;
        while ( declIter.hasNext( ) )
        {
            eachDecl = (Declaration) declIter.next( );

            dict.setVariable( eachDecl.getIdentifier( ).intern( ),
                              tuple.get( eachDecl ) );
        }

        WorkingMemory workingMemory = tuple.getWorkingMemory( );

        dict.setVariable( "drools".intern( ),
                          new DefaultKnowledgeHelper( this.rule,
                                                      tuple ) );

        Map appDataMap = workingMemory.getApplicationDataMap( );

        for ( Iterator keyIter = appDataMap.keySet( ).iterator( ); keyIter.hasNext( ); )
        {
            String key = (String) keyIter.next( );
            Object value = appDataMap.get( key );

            dict.setVariable( key,
                              value );
        }

        return dict;
    }

    private Script buildScript() throws Exception
    {
        GroovyCodeSource codeSource = new GroovyCodeSource( this.text,
                                                            "groovy.script",
                                                            "groovy.script" );

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

        GroovyClassLoader loader = new GroovyClassLoader( cl );

        Class clazz = loader.parseClass( codeSource );

        return (Script) clazz.newInstance( );
    }

}