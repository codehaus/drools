package org.drools.io;

/*
 * $Id: RuleBaseLoader.java,v 1.9 2005-12-16 00:58:20 michaelneale Exp $
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.Reader;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

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
    
    public void addFromByteArray(byte[][] bytes) throws IntegrationException, IOException, SAXException
    {
        for(int i = 0; i < bytes.length; i++)
        {
            addFromByteArray(bytes[i]);
        }
    }
    
    /**
     * Creates a JarInputStream and defines each .class in custom classLoader
     * The RuleSet is serialised out using the custom classLoader
     * 
     * @param bytes
     * @throws IOException
     * @throws IntegrationException
     * @throws SAXException
     */
    public void addFromByteArray(byte[] bytes)throws IOException,
                                                        IntegrationException, SAXException
    {
        ByteArrayInputStream jarInputStream = new ByteArrayInputStream( bytes );
        JarInputStream jos = new JarInputStream( jarInputStream );
        
        ByteArrayClassLoader classLoader = new ByteArrayClassLoader();
        byte[] ruleSetBytes = null;
        for ( JarEntry entry = jos.getNextJarEntry( ); entry != null; entry = jos.getNextJarEntry( ) )
        {
            String name = entry.getName();            
            if ( !name.endsWith(".conf") )
            {
                //We should only read in .class files and the serialised RuleSet
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int byteCount;
                while ( (byteCount = jos.read( data,
                                     0,
                                     1024 )) > -1 )
                {                    
                    bos.write(data, 0, byteCount);
                }
                
                if ( name.endsWith( ".class") )
                {
                    name = entry.getName().replace('/', '.').substring(0, name.length() - 6);
                    classLoader.addByteArray( name, bos.toByteArray() );
                }
                else
                {
                    ruleSetBytes = bos.toByteArray();
                }
            }            
        }
        
        ByteArrayInputStream bis = new ByteArrayInputStream( ruleSetBytes );
        ObjectInput in = new ObjectInputStreamWithLoader( bis,
                                                          classLoader );
        try
        {
            RuleSet ruleSet = (RuleSet) in.readObject();
            addFromRuleSet( ruleSet );
        }
        catch ( ClassNotFoundException e )
        {
            throw new IntegrationException( "Rule Set jar is not correctly formed." );
        }
        catch ( IOException e )
        {
            throw new IntegrationException( "Rule Set jar is not correctly formed." );
        }  
        finally
        {
            if ( bis != null )
            {
                bis.close();
            }
            if ( in != null )
            {
                in.close();
            }
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
    
    class ByteArrayClassLoader extends ClassLoader
    {
        public void addByteArray(String name, byte[] bytes)
        {
            defineClass(name, bytes, 0, bytes.length);
        }
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

    
    //-------------------------------------------------------
    // Added old static api methods back in for compatability
    //-------------------------------------------------------
    
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
      RuleSetLoader ruleSetLoader = new RuleSetLoader();
      ruleSetLoader.addFromUrl(urls);
      
      RuleBaseLoader ruleBaseLoader = new RuleBaseLoader(resolver);
      ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
      
      return ruleBaseLoader.buildRuleBase();      
    }

    /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * loadFromInputStream(InputStream in, ConflictResolver resolver) passing
     * the DefaultConflictResolver
     *
     * @param in
     * @return ruleBase
     */
    public static RuleBase loadFromInputStream( InputStream in )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromInputStream( in, DefaultConflictResolver.getInstance( ) );

    }

    /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     *
     * @param in
     * @param resolver
     * @return ruleBase
     */
    public static RuleBase loadFromInputStream( InputStream in,
                                                ConflictResolver resolver )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromInputStream( new InputStream[]{in}, resolver );
    }

    /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * loadFromInputStream(InputStream[] ins, ConflictResolver resolver)
     * passing the DefaultConflictResolver
     *
     * @param ins
     * @return ruleBase
     */
    public static RuleBase loadFromInputStream( InputStream[] ins )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromInputStream( ins, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     *
     * @param ins
     * @param resolver
     * @return ruleBase
     */
    public static RuleBase loadFromInputStream( InputStream[] ins,
                                                ConflictResolver resolver )
        throws SAXException,
               IOException,
               IntegrationException
    {
        RuleSetLoader ruleSetLoader = new RuleSetLoader();
        ruleSetLoader.addFromInputStream(ins);
        
        RuleBaseLoader ruleBaseLoader = new RuleBaseLoader(resolver);
        ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
        
        return ruleBaseLoader.buildRuleBase();                  
    }

    /**
     * Loads a RuleBase from a Reader using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * loadFromReader(Reader in, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param in
     * @return ruleBase
     */
    public static RuleBase loadFromReader( Reader in )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromReader( in, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Loads a RuleBase from a Reader using the given ConflictResolver
     *
     * @param in
     * @param resolver
     * @return ruleBase
     */
    public static RuleBase loadFromReader( Reader in, ConflictResolver resolver )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromReader( new Reader[]{in}, resolver );
    }

    /**
     * Loads a RuleBase from a Reader using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * loadFromReader(Reader[] ins, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param ins
     * @return ruleBase
     */
    public static RuleBase loadFromReader( Reader[] ins )
        throws SAXException,
               IOException,
               IntegrationException
    {
        return loadFromReader( ins, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Loads a RuleBase from a Reader using the given ConflictResolver
     *
     * @param ins
     * @param resolver
     * @return ruleBase
     */
    public static RuleBase loadFromReader( Reader[] ins,
                                             ConflictResolver resolver )
        throws SAXException,
               IOException,
               IntegrationException
    {
        RuleSetLoader ruleSetLoader = new RuleSetLoader();
        ruleSetLoader.addFromReader(ins);
        
        RuleBaseLoader ruleBaseLoader = new RuleBaseLoader(resolver);
        ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
        
        return ruleBaseLoader.buildRuleBase();  
    }    
}