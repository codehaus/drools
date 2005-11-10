package org.drools.io;

/*
 * $Id: RuleBaseLoader.java,v 1.6 2005-11-10 05:29:04 mproctor Exp $
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
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.RuleSet;
import org.drools.smf.RuleSetPackage;
import org.drools.spi.ConflictResolver;
import org.drools.spi.RuleBaseContext;
import org.xml.sax.SAXException;

/**
 * Convenience methods for loading a <code>RuleBase</code>.
 *
 * <p>
 * The <code>RuleBaseLoader</code> provides convenience methods for loading
 * <code>RuleBase</code> s from streams. <code>RuleBaseLoader</code> is
 * thread-safe and as such may be used to build multiple <code>RuleBase</code> s
 * simultaneously by multiple threads.
 * </p>
 *
 * @see RuleSet
 * @see RuleBase
 * @see RuleSetReader
 * @see RuleBaseBuilder
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * TODO: Would rather this wasn't a bunch of static methods.
 */
public final class RuleBaseLoader
{
    /**
     * Default constructor - marked private to prevent instantiation.
     */
    private RuleBaseLoader( )
    {
        throw new UnsupportedOperationException( );
    }
    
    /**
     * Loads a RuleBase from a RuleSetPackage using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * loadFromUrl(URL url, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param ruleSetPackage
     * @return RuleBase
     */
    public static RuleBase loadFromRuleSetPackage( RuleSetPackage ruleSetPackage )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromRuleSetPackage( ruleSetPackage, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Loads a RuleBase from a RuleSetPackage using the given ConflictResolver
     *
     * @param ruleSetPackage
     * @param resolver
     * @return RuleBase
     */
    public static RuleBase loadFromRuleSetPackage( RuleSetPackage ruleSetPackage, ConflictResolver resolver )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromUrl( new URL[]{ ruleSetPackage.getBinJar() }, resolver );
    }        
    
    /**
     * Loads a RuleBase using several URLs, using the DefaultConflictResolver.
     *
     * This is a convenience method and calls public static RuleBase
     * loadFromUrl(URL[] url, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param urls
     * @return RuleBase
     */
    public static RuleBase loadFromRuleSetPackage( RuleSetPackage[] ruleSetPackages )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromRuleSetPackage( ruleSetPackages, DefaultConflictResolver.getInstance( ) );
    }      
    
    /**
     * Loads a RuleBase from a RuleSetPackage using the given ConflictResolver
     *
     * @param ruleSetPackage
     * @param resolver
     * @return RuleBase
     */
    public static RuleBase loadFromRuleSetPackage( RuleSetPackage[] ruleSetPackage, ConflictResolver resolver )
        throws SAXException,
               IOException,
               IntegrationException
    {
        URL[] urls = new URL[ ruleSetPackage.length ];
        for ( int i = 0; i < ruleSetPackage.length; i++ )
        {
            urls[i] = ruleSetPackage[i].getBinJar();
        }
        return loadFromUrl( urls, resolver );
    }    

     

    /**
     * Loads a RuleBase from a URL using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * loadFromUrl(URL url, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param url
     * @return RuleBase
     */
    public static RuleBase loadFromUrl( URL url )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromUrl( url, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Loads a RuleBase from a URL using the given ConflictResolver
     *
     * @param url
     * @param resolver
     * @return RuleBase
     */
    public static RuleBase loadFromUrl( URL url, ConflictResolver resolver )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromUrl( new URL[]{url}, resolver );
    }   

    /**
     * Loads a RuleBase using several URLs, using the DefaultConflictResolver.
     *
     * This is a convenience method and calls public static RuleBase
     * loadFromUrl(URL[] url, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param urls
     * @return RuleBase
     */
    public static RuleBase loadFromUrl( URL[] urls )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromUrl( urls, DefaultConflictResolver.getInstance( ) );
    }          

    /**
     * Loads a RuleBase from several URLS, merging them and using the specified
     * ConflictResolver
     *
     * @param urls
     * @param resolver
     * @return RuleBase
     */
    public static RuleBase loadFromUrl( URL[] urls, ConflictResolver resolver )
        throws SAXException,
               IOException,
               IntegrationException
    {
        RuleBaseBuilder builder = new RuleBaseBuilder( );
        builder.setConflictResolver( resolver );

        for ( int i = 0; i < urls.length; ++i )
        {
            RuleSet ruleSet = getRuleSet( urls[i] );
            builder.addRuleSet( ruleSet );
        }

        return builder.build( );
    }

    private static RuleSet getRuleSet( URL url ) throws IntegrationException, IOException
    {
        URLClassLoader classLoader = new URLClassLoader( new URL[]{ url },
                                                         RuleBaseLoader.class.getClassLoader() );

        Properties props = new Properties();
        props.load( classLoader.getResourceAsStream( "rule-set.conf" ) );
        
        if (props.get( "name" ) == null)
        {
            throw new IntegrationException( "Rule Set jar does not contain a rule-set.conf file." );
        }
        
        InputStream is = null; 
        ObjectInput in = null;
        RuleSet ruleSet = null;
        try
        {
            is = classLoader.getResourceAsStream( (String) props.get( "name" ) );
        
            in = new ObjectInputStreamWithLoader( is,
                                                  classLoader );
            ruleSet = (RuleSet) in.readObject();
        }
        catch (ClassNotFoundException e)
        {
            throw new IntegrationException( "Rule Set jar is not correctly formed." );
        }
        finally
        {
            if ( is != null )
            {
                is.close();
            }
            if ( in != null )
            {
                in.close();
            }
        }
        return ruleSet;
           
    }
    
    private static class ObjectInputStreamWithLoader extends ObjectInputStream
    {
        private final ClassLoader classLoader;

        public ObjectInputStreamWithLoader(InputStream in,
                                           ClassLoader classLoader) throws IOException
        {
            super( in );
            this.classLoader = classLoader;
            enableResolveObject( true );
        }

        protected Class resolveClass(ObjectStreamClass desc) throws IOException,
                                                            ClassNotFoundException
        {
            if ( this.classLoader == null )
            {
                return super.resolveClass( desc );
            }
            else
            {
                String name = desc.getName();
                return this.classLoader.loadClass( name );
            }
        }
    }    
    
}
