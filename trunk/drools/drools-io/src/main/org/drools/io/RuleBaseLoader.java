package org.drools.io;

/*
 * $Id: RuleBaseLoader.java,v 1.5 2005-02-04 02:13:38 mproctor Exp $
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
import java.io.Reader;
import java.net.URL;

import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.RuleSet;
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
            RuleSetReader reader = new RuleSetReader( );
            RuleSet ruleSet = reader.read( urls[i] );
            builder.addRuleSet( ruleSet );
        }

        return builder.build( );
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
        RuleBaseBuilder builder = new RuleBaseBuilder( );
        builder.setConflictResolver( resolver );
        RuleBaseContext factoryContext = new RuleBaseContext( );

        for ( int i = 0; i < ins.length; ++i )
        {
            RuleSetReader reader = new RuleSetReader( factoryContext );
            RuleSet ruleSet = reader.read( ins[i] );
            builder.addRuleSet( ruleSet );
        }

        return builder.build( );
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
        RuleBaseContext factoryContext = new RuleBaseContext( );
        
        RuleBaseBuilder builder = new RuleBaseBuilder( factoryContext );
        builder.setConflictResolver( resolver );        

        for ( int i = 0; i < ins.length; ++i )
        {
            RuleSetReader reader = new RuleSetReader( factoryContext );
            RuleSet ruleSet = reader.read( ins[i] );
            builder.addRuleSet( ruleSet );
        }

        return builder.build( );
    }
}
