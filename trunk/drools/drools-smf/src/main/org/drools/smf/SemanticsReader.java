package org.drools.smf;

/*
 * $Id: SemanticsReader.java,v 1.9 2004-12-14 21:00:29 mproctor Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Loader of <code>SemanticModule</code> s from a set of properties.
 * 
 * <p>
 * The required properties are:
 * 
 * <ul>
 * <li><code>module.uri=<i>uri</i></code>: To denote the URI of the
 * module.</li>
 * <li><code><i>tagname</i>=<i>classname</i></code>. For each semantic
 * component to associate a tag with the component.
 * </ul>
 * </p>
 * 
 * <p>
 * Instances of <code>SemanticsReader</code> are re-entrant and thread-safe.
 * The singleton may be used simultaneously by multiple threads.
 * </p>
 * 
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * 
 * @version $Id: SemanticsReader.java,v 1.9 2004-12-14 21:00:29 mproctor Exp $
 */
public class SemanticsReader
{
    // ----------------------------------------------------------------------
    // Class members
    // ----------------------------------------------------------------------

    /** Singleton instance. */
    private static final SemanticsReader INSTANCE = new SemanticsReader( );

    // ----------------------------------------------------------------------
    // Class methods
    // ----------------------------------------------------------------------

    /**
     * Retrieve the singleton instance.
     * 
     * @return The singleton instance.
     */
    public static SemanticsReader getInstance()
    {
        return INSTANCE;
    }

    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------

    /**
     * Construct.
     */
    public SemanticsReader()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    // Instance methods
    // ----------------------------------------------------------------------

    /**
     * Read a semantic module descriptor from a <code>URL</code>.
     * 
     * @param url The descriptor URL.
     * 
     * @return The loaded semantic module.
     * @throws IOException
     * @throws SemanticsReaderException
     */
    public SemanticModule read(URL url) throws IOException,
                                       SemanticsReaderException
    {
        InputStream in = url.openStream( );

        try
        {
            return read( in );
        }
        finally
        {
            in.close( );
        }
    }

    /**
     * Read a semantic module descriptor from an <code>InputStream</code>.
     * 
     * @param in
     *            The descriptor input stream.
     * 
     * @return The loaded semantic module.
     * 
     * @throws IOException
     *             If an error occurs while loading the module.
     * @throws SemanticsReaderException
     *             If an error occurs while loading the module.
     */
    public SemanticModule read(InputStream in) throws IOException,
                                              SemanticsReaderException
    {
        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );

        if ( cl == null )
        {
            cl = SemanticsReader.class.getClassLoader( );
        }

        Properties props = new Properties( );

        props.load( in );

        String uri = props.getProperty( "module.uri" );

        if ( uri == null || uri.trim( ).equals( "" ) )
        {
            throw new SemanticsReaderException( "module.uri must be specified" );
        }

        SimpleSemanticModule module = new SimpleSemanticModule( uri.trim( ) );

        for ( Enumeration propNames = props.propertyNames( ); propNames.hasMoreElements( ); )
        {
            String key = (String) propNames.nextElement( );

            if ( key.equals( "module.uri" ) )
            {
                continue;
            }

            String className = props.getProperty( key );

            Class factoryClass;
            try
            {
                factoryClass = cl.loadClass( className );
            }
            catch ( ClassNotFoundException e )
            {
                throw new SemanticsReaderException( e );
            }
            if ( key.indexOf( "(" ) < 0 || key.indexOf( ")" ) < 0 )
            {
                throw new SemanticsReaderException( "invalid key: " + key );
            }

            String type = parseType( key );

            if ( type == null || type.equals( "" ) )
            {
                throw new SemanticsReaderException( "no type specified" );
            }

            String componentName = parseName( key );

            if ( componentName == null || componentName.equals( "" ) )
            {
                throw new SemanticsReaderException( "no component name specified" );
            }

            try
            {
                if ( "Rule".equals( type ) )
                {
                    RuleFactory factory = (RuleFactory) factoryClass.newInstance( );

                    module.addRuleFactory( componentName,
                                           factory );
                }
                else if ( "ObjectType".equals( type ) )
                {
                    ObjectTypeFactory factory = (ObjectTypeFactory) factoryClass.newInstance( );

                    module.addObjectTypeFactory( componentName,
                                                 factory );
                }
                else if ( "Condition".equals( type ) )
                {
                    ConditionFactory factory = (ConditionFactory) factoryClass.newInstance( );

                    module.addConditionFactory( componentName,
                                                factory );
                }
                else if ( "Consequence".equals( type ) )
                {
                    ConsequenceFactory factory = (ConsequenceFactory) factoryClass.newInstance( );

                    module.addConsequenceFactory( componentName,
                                                  factory );
                }
                else if ( "Duration".equals( type ) )
                {
                    DurationFactory factory = (DurationFactory) factoryClass.newInstance( );

                    module.addDurationFactory( componentName,
                                               factory );
                }
                else if ( "ImportEntry".equals( type ) )
                {
                    ImportEntryFactory factory = (ImportEntryFactory) factoryClass.newInstance( );

                    module.addImportEntryFactory( componentName,
                                                  factory );
                }
                else if ( "ApplicationData".equals( type ) )
                {
                    ApplicationDataFactory factory = (ApplicationDataFactory) factoryClass.newInstance( );

                    module.addApplicationDataFactory( componentName,
                                                      factory );
                }
                else if ( "Functions".equals( type ) )
                {
                    FunctionsFactory factory = (FunctionsFactory) factoryClass.newInstance( );

                    module.addFunctionsFactory( componentName,
                                                factory );
                }
                else
                {
                    throw new SemanticsReaderException( "unknown type '" + type + "'" );
                }
            }
            catch ( InstantiationException e )
            {
                throw new SemanticsReaderException( e );
            }
            catch ( IllegalAccessException e )
            {
                throw new SemanticsReaderException( e );
            }
        }

        return module;
    }

    protected String parseType(String key)
    {
        int leftParen = key.indexOf( "(" );

        if ( leftParen < 0 )
        {
            return null;
        }

        return key.substring( 0,
                              leftParen ).trim( );
    }

    protected String parseName(String key)
    {
        int leftParen = key.indexOf( "(" );
        int rightParen = key.indexOf( ")" );

        if ( leftParen < 0 || rightParen < 0 )
        {
            return null;
        }

        return key.substring( leftParen + 1,
                              rightParen ).trim( );
    }
}
