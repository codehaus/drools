package org.drools.io;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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
    private Map ruleSets = new HashMap();
    

    public  RuleSetLoader()
    {
    }
    
  
    
    public Map getRuleSets() {
        return this.ruleSets;
    }

    /**
     * Loads a RuleBase from a URL using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase loadFromUrl(URL url, ConflictResolver resolver) passing the DefaultConflictResolver
     * 
     * @param url
     * @return RuleBase
     */
    public void addFromUrl(URL url) throws SAXException,
                                IOException,
                                IntegrationException
    {
        addFromUrl( new URL[] { url } );
    }


    /**
     * Loads a RuleBase from several URLS, merging them and using the specified ConflictResolver
     * 
     * @param urls
     * @param resolver
     * @return RuleBase
     */
    public void addFromUrl(URL[] urls ) throws SAXException,
                                                     IOException,
                                                     IntegrationException
    {
        addFromInputSource( convertToInputSource( urls ) );
    }

    /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase loadFromInputStream(InputStream in, ConflictResolver resolver) passing the DefaultConflictResolver
     * 
     * @param in
     * @return ruleBase
     */
    public void addFromInputStream(InputStream in) throws SAXException,
                                                  IOException,
                                                  IntegrationException
    {
        addFromInputStream( new InputStream[] { in } );
    }

   /**
     * Loads a RuleBase from an InputStream using the default ConflictResolver
     * 
     * @param ins
     * @param resolver
     * @return ruleBase
     */
    public void addFromInputStream(InputStream[] insr) throws SAXException,
                                                             IOException,
                                                             IntegrationException
    {
        addFromInputSource( convertToInputSource( insr )  );
    }

    /**
     * Loads a RuleBase from a Reader using the given ConflictResolver
     * 
     * @param in
     * @param resolver
     * @return ruleBase
     */
    public void addFromReader(Reader in) throws SAXException,
                                                        IOException,
                                                        IntegrationException
    {
        addFromReader( new Reader[]{in} );
    }

   /**
     * Loads a RuleBase from a Reader using the given ConflictResolver
     * 
     * @param ins
     * @param resolver
     * @return ruleBase
     */
    public void addFromReader(Reader[] ins) throws SAXException,
                                                        IOException,
                                                        IntegrationException
    {
        addFromInputSource( convertToInputSource( ins ) );
    }

    /**
     * Loads a RuleBase from a Reader using the given ConflictResolver
     * 
     * @param ins
     * @param resolver
     * @return ruleBase
     */
    public void addFromInputSurce(InputSource source) throws SAXException,
                                                        IOException,
                                                        IntegrationException
    {
        addFromInputSurce( source );
    }
    
    /**
     * Loads a RuleBase from a Reader using the given ConflictResolver.
     * All SAXExceptions with embedded exceptions are rethrown as nested
     * Exceptions in IntegrationException
     * 
     * @param ins
     * @param resolver
     * @return ruleBase
     */
    public void addFromInputSource(InputSource[] sources) throws SAXException,
                                                        IOException,
                                                        IntegrationException
    {
        String packageName = "drools.org";
        RuleBaseContext factoryContext = new RuleBaseContext();
        RuleSet ruleSet = null;
        for ( int i = 0; i < sources.length; ++i )
        {
            RuleSetReader reader = new RuleSetReader( factoryContext );
            try {
                ruleSet = reader.read( sources[i] );
            } catch ( SAXException e ) {
                if ( e.getException() != null ) 
                {
                    throw new IntegrationException( e.getException() );
                }
            }
            RuleSetCompiler compiler = new RuleSetCompiler(ruleSet,
                                                           packageName,
                                                           "drools" ); 
            this.ruleSets.put(ruleSet.getName(), compiler);
        }
    }

    private static InputSource[] convertToInputSource(URL[] urls)
    {
        InputSource[] sources = new InputSource[urls.length];
        for ( int i = 0; i < urls.length; i++ )
        {
            sources[i] = new InputSource( urls[i].toExternalForm() );
        }

        return sources;
    }

    private static InputSource[] convertToInputSource(InputStream[] streams)
    {
        InputSource[] sources = new InputSource[streams.length];
        for ( int i = 0; i < streams.length; i++ )
        {
            sources[i] = new InputSource( streams[i] );
        }

        return sources;
    }

    private static InputSource[] convertToInputSource(Reader[] readers)
    {
        InputSource[] sources = new InputSource[readers.length];
        for ( int i = 0; i < readers.length; i++ )
        {
            sources[i] = new InputSource( readers[i] );
        }

        return sources;
    }

}
