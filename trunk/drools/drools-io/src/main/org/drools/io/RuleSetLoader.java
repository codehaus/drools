package org.drools.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.RuleSet;
import org.drools.smf.RuleSetCompiler;
import org.drools.smf.RuleSetPackage;
import org.drools.spi.ConflictResolver;
import org.drools.spi.RuleBaseContext;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RuleSetLoader
{
    /**
     * Default constructor - marked private to prevent instantiation.
     */
    private RuleSetLoader()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Loads a RuleBase from a URL using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase loadFromUrl(URL url, ConflictResolver resolver) passing the DefaultConflictResolver
     * 
     * @param url
     * @return RuleBase
     */
    public static RuleSetPackage loadFromUrl(URL url) throws SAXException,
                                                                IOException,
                                                                IntegrationException
    {
        return loadFromUrl( url,
                            DefaultConflictResolver.getInstance() );
    }

    /**
     * Loads a RuleBase from a URL using the given ConflictResolver
     * 
     * @param url
     * @param resolver
     * @return RuleBase
     */
    public static RuleSetPackage loadFromUrl(URL url,
                                             ConflictResolver resolver) throws SAXException,
                                                                       IOException,
                                                                       IntegrationException
    {
        return loadFromUrl( new URL[]{url},
                            resolver)[0];
    }

    /**
     * Loads a RuleBase using several URLs, using the DefaultConflictResolver.
     * 
     * This is a convenience method and calls public static RuleBase loadFromUrl(URL[] url, ConflictResolver resolver) passing the DefaultConflictResolver
     * 
     * @param urls
     * @return RuleBase
     */
    public static RuleSetPackage[] loadFromUrl(URL[] urls) throws SAXException,
                                                          IOException,
                                                          IntegrationException
    {
        return loadFromUrl( urls,
                            DefaultConflictResolver.getInstance() );
    }

    /**
     * Loads a RuleBase from several URLS, merging them and using the specified ConflictResolver
     * 
     * @param urls
     * @param resolver
     * @return RuleBase
     */
    public static RuleSetPackage[] loadFromUrl(URL[] urls,
                                               ConflictResolver resolver) throws SAXException,
                                                                          IOException,
                                                                          IntegrationException
    {
        return loadFromReader( convertToInputSource( urls ),
                               DefaultConflictResolver.getInstance() );
    }

    /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase loadFromInputStream(InputStream in, ConflictResolver resolver) passing the DefaultConflictResolver
     * 
     * @param in
     * @return ruleBase
     */
    public static RuleSetPackage loadFromInputStream(InputStream in) throws SAXException,
                                                                    IOException,
                                                                    IntegrationException
    {
        return loadFromInputStream( in,
                                    DefaultConflictResolver.getInstance() );
    }

    /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     * 
     * @param in
     * @param resolver
     * @return ruleBase
     */
    public static RuleSetPackage loadFromInputStream(InputStream in,
                                                     ConflictResolver resolver) throws SAXException,
                                                                               IOException,
                                                                               IntegrationException
    {
        return loadFromInputStream( new InputStream[]{in},
                                    resolver  )[0];
    }

    /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase loadFromInputStream(InputStream[] ins, ConflictResolver resolver) passing the DefaultConflictResolver
     * 
     * @param ins
     * @return ruleBase
     */
    public static RuleSetPackage[] loadFromInputStream(InputStream[] ins,
                                                       String packageName) throws SAXException,
                                                                         IOException,
                                                                         IntegrationException
    {
        return loadFromInputStream( ins,
                                    DefaultConflictResolver.getInstance() );
    }

    /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     * 
     * @param ins
     * @param resolver
     * @return ruleBase
     */
    public static RuleSetPackage[] loadFromInputStream(InputStream[] ins,
                                                       ConflictResolver resolver) throws SAXException,
                                                                                 IOException,
                                                                                 IntegrationException
    {
        return loadFromReader( convertToInputSource( ins ),
                               DefaultConflictResolver.getInstance() );
    }

    /**
     * Loads a RuleBase from a Reader using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase loadFromReader(Reader in, ConflictResolver resolver) passing the DefaultConflictResolver
     * 
     * @param in
     * @return ruleBase
     */
    public static RuleSetPackage loadFromReader(Reader in) throws SAXException,
                                                          IOException,
                                                          IntegrationException
    {
        return loadFromReader( in,
                               DefaultConflictResolver.getInstance() );
    }

    /**
     * Loads a RuleBase from a Reader using the given ConflictResolver
     * 
     * @param in
     * @param resolver
     * @return ruleBase
     */
    public static RuleSetPackage loadFromReader(Reader in,
                                                ConflictResolver resolver) throws SAXException,
                                                                          IOException,
                                                                          IntegrationException
    {
        return loadFromReader( new Reader[]{in},
                               resolver )[0];
    }

    /**
     * Loads a RuleBase from a Reader using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase loadFromReader(Reader[] ins, ConflictResolver resolver) passing the DefaultConflictResolver
     * 
     * @param ins
     * @return ruleBase
     */
    public static RuleSetPackage[] loadFromReader(Reader[] ins) throws SAXException,
                                                               IOException,
                                                               IntegrationException
    {
        return loadFromReader( ins ,
                               DefaultConflictResolver.getInstance() );
    }
    
    /**
     * Loads a RuleBase from a Reader using the given ConflictResolver
     * 
     * @param ins
     * @param resolver
     * @return ruleBase
     */  
    public static RuleSetPackage[] loadFromReader(Reader[] ins,
                                                  ConflictResolver resolver) throws SAXException,
    IOException,
    IntegrationException
    {
        return loadFromReader( convertToInputSource( ins ),
                               resolver );
    }    

    /**
     * Loads a RuleBase from a Reader using the given ConflictResolver
     * 
     * @param ins
     * @param resolver
     * @return ruleBase
     */
    public static RuleSetPackage[] loadFromReader(InputSource[] sources,
                                                  ConflictResolver resolver) throws SAXException,
                                                                            IOException,
                                                                            IntegrationException
    {
        String packageName = "drools.org";
        RuleBaseContext factoryContext = new RuleBaseContext();
        RuleSetCompiler compiler = RuleSetCompiler.getInstance();
        RuleSet ruleSet  = null;
        List packages = new ArrayList();
        for ( int i = 0; i < sources.length; ++i )
        {
            RuleSetReader reader = new RuleSetReader( factoryContext );
            ruleSet = reader.read( sources[i] );
            
            packages.add( compiler.compile( ruleSet,
                                            packageName,
                                            "drools" ) );
                        
        }
        
        return (org.drools.smf.RuleSetPackage[] ) packages.toArray( new RuleSetPackage[ packages.size() ] );
    }
     
    private static InputSource[] convertToInputSource( URL[] urls )
    {
        InputSource[] sources = new InputSource[ urls.length ];
        for ( int i = 0; i < urls.length; i++ )
        {
            sources[i] = new InputSource( urls[i].toExternalForm( ) );
        }
        
        return sources;
    }
    
    private static InputSource[] convertToInputSource( InputStream[] streams )
    {
        InputSource[] sources = new InputSource[ streams.length ];
        for ( int i = 0; i < streams.length; i++ )
        {
            sources[i] = new InputSource( streams[i] );
        }
        
        return sources;        
    }
    
    private static InputSource[] convertToInputSource( Reader[] readers )
    {
        InputSource[] sources = new InputSource[ readers.length ];
        for ( int i = 0; i < readers.length; i++ )
        {
            sources[i] = new InputSource( readers[i] );
        }
        
        return sources;        
    }
    
    
}
