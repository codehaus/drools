package org.drools.io;

/*
 * $Id: RuleBaseFactory.java,v 1.1 2004-12-04 14:08:54 simon Exp $
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

import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.RuleSet;
import org.drools.spi.ConflictResolver;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * Convenience factory methods for constructing a <code>RuleBase</code>.
 *
 * <p>
 * The <code>RuleBaseFactory</code> provides convenience methods for creating
 * <code>RuleBase</code> s from streams. <code>RuleBaseFactory</code> is
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
public final class RuleBaseFactory
{
    /**
     * Default constructor - marked private to prevent instantiation.
     */
    private RuleBaseFactory( )
    {
        throw new UnsupportedOperationException( );
    }

    /**
     * Creates a RuleBase from a URL using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * createFromUrl(URL url, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param url
     * @return RuleBase
     * @throws Exception
     */
    public static RuleBase createFromUrl( URL url ) throws Exception
    {
        return createFromUrl( url, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Creates a RuleBase from a URL using the given ConflictResolver
     *
     * @param url
     * @param resolver
     * @return RuleBase
     * @throws Exception
     */
    public static RuleBase createFromUrl( URL url, ConflictResolver resolver ) throws Exception
    {
        return createFromUrl( new URL[]{url}, resolver );
    }

    /**
     * Creates a RuleBase using several URLs, using the DefaultConflictResolver.
     *
     * This is a convenience method and calls public static RuleBase
     * createFromUrl(URL[] url, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param urls
     * @return RuleBase
     * @throws Exception
     */
    public static RuleBase createFromUrl( URL[] urls ) throws Exception
    {
        return createFromUrl( urls, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Creates a RuleBase from several URLS, merging them and using the specified
     * ConflictResolver
     *
     * @param urls
     * @param resolver
     * @return RuleBase
     * @throws Exception
     */
    public static RuleBase createFromUrl( URL[] urls, ConflictResolver resolver ) throws Exception
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
     * Creates a RuleBase from an InputStream using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * createFromInputStream(InputStream in, ConflictResolver resolver) passing
     * the DefaultConflictResolver
     *
     * @param in
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase createFromInputStream( InputStream in ) throws Exception
    {
        return createFromInputStream( in, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Creates a RuleBase from an InputStream using the default ConflictResolver
     *
     * @param in
     * @param resolver
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase createFromInputStream( InputStream in,
                                                  ConflictResolver resolver ) throws Exception
    {
        return createFromInputStream( new InputStream[]{in}, resolver );
    }

    /**
     * Creates a RuleBase from an InputStream using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * createFromInputStream(InputStream[] ins, ConflictResolver resolver)
     * passing the DefaultConflictResolver
     *
     * @param ins
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase createFromInputStream( InputStream[] ins ) throws Exception
    {
        return createFromInputStream( ins, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Creates a RuleBase from an InputStream using the default ConflictResolver
     *
     * @param ins
     * @param resolver
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase createFromInputStream( InputStream[] ins,
                                                  ConflictResolver resolver ) throws Exception
    {
        RuleBaseBuilder builder = new RuleBaseBuilder( );
        builder.setConflictResolver( resolver );

        for ( int i = 0; i < ins.length; ++i )
        {
            RuleSetReader reader = new RuleSetReader( );
            RuleSet ruleSet = reader.read( ins[i] );
            builder.addRuleSet( ruleSet );
        }

        return builder.build( );
    }

    /**
     * Creates a RuleBase from a Reader using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * createFromReader(Reader in, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param in
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase createFromReader( Reader in ) throws Exception
    {
        return createFromReader( in, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Creates a RuleBase from a Reader using the given ConflictResolver
     *
     * @param in
     * @param resolver
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase createFromReader( Reader in, ConflictResolver resolver ) throws Exception
    {
        return createFromReader( new Reader[]{in}, resolver );
    }

    /**
     * Creates a RuleBase from a Reader using the default ConflictResolver
     *
     * This is a convenience method and calls public static RuleBase
     * createFromReader(Reader[] ins, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     *
     * @param ins
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase createFromReader( Reader[] ins ) throws Exception
    {
        return createFromReader( ins, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Creates a RuleBase from a Reader using the given ConflictResolver
     *
     * @param ins
     * @param resolver
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase createFromReader( Reader[] ins,
                                             ConflictResolver resolver ) throws Exception
    {
        RuleBaseBuilder builder = new RuleBaseBuilder( );
        builder.setConflictResolver( resolver );

        for ( int i = 0; i < ins.length; ++i )
        {
            RuleSetReader reader = new RuleSetReader( );
            RuleSet ruleSet = reader.read( ins[i] );
            builder.addRuleSet( ruleSet );
        }

        return builder.build( );
    }
}
