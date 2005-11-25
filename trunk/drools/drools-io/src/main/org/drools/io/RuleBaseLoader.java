package org.drools.io;

/*
 * $Id: RuleBaseLoader.java,v 1.7 2005-11-25 02:09:03 mproctor Exp $
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.RuleSet;
import org.drools.smf.RuleSetCompiler;
import org.drools.spi.ConflictResolver;
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

    private RuleBaseBuilder builder = new RuleBaseBuilder();

    /**
     * Default constructor
     */
    public RuleBaseLoader()
    {
        this( DefaultConflictResolver.getInstance() );
    }

    /**
     * Default constructor
     */
    public RuleBaseLoader(ConflictResolver resolver)
    {
        this.builder.setConflictResolver( resolver );
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
    public void addFromRuleSetLoader(RuleSetLoader[] ruleSetLoaders) throws SAXException,
                                                                    IOException,
                                                                    IntegrationException
    {
        for ( int i = 0; i < ruleSetLoaders.length; i++ )
        {
            addFromRuleSetLoader( ruleSetLoaders[i] );
        }
    }

    /**
     * Loads a RuleBase from a RuleSetPackage using the given ConflictResolver
     *
     * @param ruleSetPackage
     * @param resolver
     * @return RuleBase
     */
    public void addFromRuleSetLoader(RuleSetLoader ruleSetLoader) throws SAXException,
                                                                 IOException,
                                                                 IntegrationException
    {
        Map map = ruleSetLoader.getRuleSets();
        Iterator it = map.values().iterator();
        RuleSet[] ruleSets = new RuleSet[map.size()];
        int i = 0;
        while ( it.hasNext() )
        {
            ruleSets[i] = ((RuleSetCompiler) it.next()).getRuleSet();
            i++;
        }
        addFromRuleSet( ruleSets );
    }

    /**
     * Loads a RuleBase from several URLS, merging them and using the specified
     * ConflictResolver
     *
     * @param urls
     * @param resolver
     * @return RuleBase
     */
    public void addFromUrl(URL[] urls) throws SAXException,
                                      IOException,
                                      IntegrationException
    {
        for ( int i = 0; i < urls.length; i++ )
        {
            addFromUrl( urls[i] );
        }
    }

    /**
     * Loads a RuleBase from a URL using the given ConflictResolver
     *
     * @param url
     * @param resolver
     * @return RuleBase
     */
    public void addFromUrl(URL url) throws SAXException,
                                   IOException,
                                   IntegrationException
    {
        if ( url.toExternalForm().toLowerCase().endsWith( ".ddj" ) )
        {
            //its url to drools deployment jar
            addFromRuleSet( getRuleSet(url) );
        }
        else
        {
            // the url should reference a RuleBase coniguration text file 
            // which contains a list of URLs to .ddj files or to more RuleBase
            // configurations
            InputStream config = url.openStream();
            BufferedReader in = new BufferedReader( new InputStreamReader( config ) );
            try
            {
                String line;

                while ( (line = in.readLine( )) != null )
                {
                    line = line.trim( );

                    if ( line.equals( "" ) || line.startsWith( "#" ) )
                    {
                        continue;
                    }

                    addFromUrl( new URL(line) );
                }
            }
            finally
            {
                in.close( );
            } 
        }
    }
    
    public void addFromByteArray(Object object)throws IOException,
    IntegrationException
    {
        if ( !(object instanceof byte[]) ) {
            //return IOException( "Object is not a byte[]" );
        }
    }

    public void addFromRuleSet(RuleSet ruleSet) throws SAXException,
                                               IOException,
                                               IntegrationException
    {
        this.builder.addRuleSet( ruleSet );
    }

    public void addFromRuleSet(RuleSet[] ruleSets) throws SAXException,
                                                  IOException,
                                                  IntegrationException
    {
        for ( int i = 0; i < ruleSets.length; i++ )
        {
            addFromRuleSet( ruleSets[i] );
        }
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
    
    public RuleBase buildRuleBase() {
       return  this.builder.build();
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
