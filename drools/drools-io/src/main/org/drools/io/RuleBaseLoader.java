package org.drools.io;

/*
 * $Id: RuleBaseLoader.java,v 1.11 2006-01-13 07:26:55 michaelneale Exp $
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
 * Convenience methods for loading a <code>RuleBase</code>, as well as lower level calls
 * for building up a rule base from RuleSets.
 * 
 * Note that the loadXXX static helper methods work directly off DRL, building/compiling behind the scenes.
 * The addXXX methods generally work with pre compiled rules binaries (DDJ's as they are known).
 *
 * 
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
 * @see SerializableRuleBaseProxy
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * TODO: Move static helper methods into a different class.
 */
public final class RuleBaseLoader
{

    private RuleBaseBuilder builder = new RuleBaseBuilder();

    private List ruleSetBinaries = new ArrayList();
    
    
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
     * Adds to the rulebase from a RuleSetLoader instance.
     * The binaries from the ruleset loader will be kept for serialization purposes.
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
            RuleSetCompiler compiler = (RuleSetCompiler) it.next();
            ruleSets[i] = (compiler).getRuleSet();
            addRuleSetBinary(compiler.getBinaryDeploymentJar());
            i++;
        }
        addFromRuleSet( ruleSets );
    }

    /**
     * Loads a RuleBase from several URLS, merging them and using the specified
     * ConflictResolver. URLS should be either to compiled DDJ rules, or a configuration file
     * which indicates where to find the DDJs.
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
     * The URL must be to a DDJ compiled resource, or a conf file pointing to
     * the DDJ compiled rules. 
     * If you do not use pre compiled rulesets, use one of the static helper methods instead.
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
            addFromDDJInputStream(url.openStream());
            //its url to drools deployment jar
            //addFromRuleSet( getRuleSet(url) );
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
    
    /** This will add multiple rulesets. Bytes should be for DDJ compiled rules
     * 
     * @param bytes
     * @throws IntegrationException
     * @throws IOException
     * @throws SAXException
     */
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
     * Loads a ruleset from the DDJ bytes.
     * @param bytes
     * @throws IOException
     * @throws IntegrationException
     * @throws SAXException
     */    
    public void addFromByteArray(byte[] bytes)throws IOException,
    IntegrationException, SAXException {
        
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        addFromDDJInputStream(stream);
    }
    
    /**
     * The input stream here should be to a DDJ compiled rules "jar" only.
     * NOT source.
     */
    private void addFromDDJInputStream(InputStream jarInputStream) throws IOException,
        IntegrationException, SAXException 
    {
        JarInputStream jos = new JarInputStream( jarInputStream );
        
        Map functions = new HashMap();
        Map others = new HashMap();
        Map invokers = new HashMap();
        
        ByteArrayClassLoader classLoader = new ByteArrayClassLoader(Thread.currentThread().getContextClassLoader());
        
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
                    //We need to add to the classloader in a certain order...
                    name = entry.getName().replace('/', '.').substring(0, name.length() - 6);
                    if (name.indexOf("Invoker") > 0) {
                        invokers.put(name, bos.toByteArray());
                    } else if (name.indexOf("Function_") > 0) {
                        functions.put(name, bos.toByteArray());
                    } else {
                        others.put(name, bos.toByteArray());
                    }
                   
                }
                else
                {
                    ruleSetBytes = bos.toByteArray();
                }
            }     
        
        }
        
        addToClassLoader(functions, classLoader);
        addToClassLoader(others, classLoader);
        addToClassLoader(invokers, classLoader);
        
        ByteArrayInputStream bis = new ByteArrayInputStream( ruleSetBytes );
        ObjectInput in = new ObjectInputStreamWithLoader( bis,
           
                                                          classLoader );
        addRuleSetBinary( ruleSetBytes );
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

    private void addRuleSetBinary(byte[] ruleSetBytes)
    {
        this.ruleSetBinaries.add(ruleSetBytes);
    }   

 
    


    /**
     * this adds to the classloader.
     * @param classes
     * @param classLoader
     */
    private void addToClassLoader(Map classes,
                                  ByteArrayClassLoader classLoader)
    {
        for ( Iterator iter = classes.keySet().iterator( ); iter.hasNext( ); )
        {
            String name = (String) iter.next( );
            byte[] data = (byte[]) classes.get(name);
            classLoader.addByteArray( name, data );
            
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
    
  
    
    /**
     * This will return a rulebase based on the added rulesets.
     * The rulebase should be serializable by default.
     * @return
     */
    public RuleBase buildRuleBase() {
       RuleBase ruleBase = this.builder.build();
       if (this.ruleSetBinaries.size() > 0) {
           SerializableRuleBaseProxy proxy = new SerializableRuleBaseProxy(ruleBase, this.ruleSetBinaries);
           return proxy;
       } else {
           return ruleBase;
       }
    }
    
    class ByteArrayClassLoader extends  ClassLoader
    {
        ByteArrayClassLoader(ClassLoader parent) {
          super(parent);
        }
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
     *
     * @param url A URL to the DRL to compile and load.
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
      
      return buildFromLoader( resolver,
                              ruleSetLoader );      
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
        
        return buildFromLoader( resolver,
                                  ruleSetLoader );                  
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
        
        return buildFromLoader( resolver,
                                  ruleSetLoader );  
    }

    private static RuleBase buildFromLoader(ConflictResolver resolver,
                                              RuleSetLoader ruleSetLoader) throws SAXException,
                                                                          IOException,
                                                                          IntegrationException
    {
        RuleBaseLoader ruleBaseLoader = new RuleBaseLoader(resolver);
        ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);

        return ruleBaseLoader.buildRuleBase();
    }    

}