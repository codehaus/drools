package org.drools.io;

/*
 * $Id: RuleBaseBuilder.java,v 1.7 2004-11-03 22:54:36 mproctor Exp $
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

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.RuleSet;
import org.drools.spi.ConflictResolver;

/**
 * Factory for constructing a <code>RuleBase</code>.
 * 
 * <p>
 * The <code>RuleBaseBuilder</code> integrates the added <code>RuleSet</code>
 * s into the <b>Rete </b> network. A <code>RuleBaseBuilder</code> may be
 * re-used after building a <code>RuleBase</code> but it may not be used to
 * build multiple <code>RuleBase</code> s simultaneously by multiple threads.
 * </p>
 * 
 * @see #build
 * @see RuleSet
 * @see RuleBase
 * 
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 */
public class RuleBaseBuilder
{
    /**
     * Builds a RuleBase from a URL using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase
     * buildFromUrl(URL url, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     * 
     * @param url
     * @return RuleBase
     * @throws Exception
     */
    public static RuleBase buildFromUrl( URL url ) throws Exception
    {
        return buildFromUrl( url, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Builds a RuleBase from a URL using the given ConflictResolver
     * 
     * @para url
     * @param resolver
     * @return RuleBase
     * @throws Exception
     */
    public static RuleBase buildFromUrl( URL url, ConflictResolver resolver ) throws Exception
    {
        return buildFromUrl( new URL[]{url}, resolver );
    }

    /**
     * Builds a RuleBase using several URLs, using the DefaultConflictResolver.
     * 
     * This is a convenience method and calls public static RuleBase
     * buildFromUrl(URL[] url, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     * 
     * @param urls
     * @return RuleBase
     * @throws Exception
     */
    public static RuleBase buildFromUrl( URL[] urls ) throws Exception
    {
        return buildFromUrl( urls, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Builds a RuleBase from several URLS, merging them and using the specified
     * ConflictResolver
     * 
     * @param urls
     * @param resolver
     * @return RuleBase
     * @throws Exception
     */
    public static RuleBase buildFromUrl( URL[] urls, ConflictResolver resolver ) throws Exception
    {
        org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder( );
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
     * Builds a RuleBase from an InputStream using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase
     * buildFromInputStream(InputStream in, ConflictResolver resolver) passing
     * the DefaultConflictResolver
     * 
     * @param in
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase buildFromInputStream( InputStream in ) throws Exception
    {
        return buildFromInputStream( in, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Builds a RuleBase from an InputStream using the default ConflictResolver
     * 
     * @param in
     * @param resolver
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase buildFromInputStream( InputStream in,
                                                ConflictResolver resolver ) throws Exception
    {
        return buildFromInputStream( new InputStream[]{in}, resolver );
    }

    /**
     * Builds a RuleBase from an InputStream using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase
     * buildFromInputStream(InputStream[] ins, ConflictResolver resolver)
     * passing the DefaultConflictResolver
     * 
     * @param ins
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase buildFromInputStream( InputStream[] ins ) throws Exception
    {
        return buildFromInputStream( ins, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Builds a RuleBase from an InputStream using the default ConflictResolver
     * 
     * @param ins
     * @param resolver
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase buildFromInputStream( InputStream[] ins,
                                                ConflictResolver resolver ) throws Exception
    {
        org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder( );
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
     * Builds a RuleBase from a Reader using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase
     * buildFromReader(Reader in, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     * 
     * @param in
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase buildFromReader( Reader in ) throws Exception
    {
        return buildFromReader( in, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Builds a RuleBase from a Reader using the given ConflictResolver
     * 
     * @param in
     * @param resolver
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase buildFromReader( Reader in, ConflictResolver resolver ) throws Exception
    {
        return buildFromReader( new Reader[]{in}, resolver );
    }

    /**
     * Builds a RuleBase from a Reader using the default ConflictResolver
     * 
     * This is a convenience method and calls public static RuleBase
     * buildFromReader(Reader[] ins, ConflictResolver resolver) passing the
     * DefaultConflictResolver
     * 
     * @param ins
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase buildFromReader( Reader[] ins ) throws Exception
    {
        return buildFromReader( ins, DefaultConflictResolver.getInstance( ) );
    }

    /**
     * Builds a RuleBase from a Reader using the given ConflictResolver
     * 
     * @param ins
     * @param resolver
     * @return ruleBase
     * @throws Exception
     */
    public static RuleBase buildFromReader( Reader[] ins,
                                           ConflictResolver resolver ) throws Exception
    {
        org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder( );
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
