package org.drools.jsr94.rules.admin;

/*
 * $Id: RuleExecutionSetProviderImpl.java,v 1.15 2004-11-14 20:12:37 dbarnett Exp $
 *
 * Copyright 2002-2004 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
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
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetProvider;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.drools.io.RuleSetReader;
import org.drools.rule.RuleSet;
import org.drools.smf.DefaultSemanticsRepository;
import org.w3c.dom.Element;

/**
 * The Drools implementation of the <code>RuleExecutionSetProvider</code>
 * interface which defines <code>RuleExecutionSet</code> creation methods for
 * defining <code>RuleExecutionSet</code>s from potentially serializable
 * resources.
 *
 * @see RuleExecutionSetProvider
 *
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class RuleExecutionSetProviderImpl implements RuleExecutionSetProvider
{
    /**
     * Creates a <code>RuleExecutionSet</code> implementation from an XML
     * Document and additional Drools-specific properties. A Drools-specific
     * rule execution set is read from the supplied XML Document.
     *
     * @param ruleExecutionSetElement the XML element that is the source of the
     *        rule execution set
     * @param properties additional properties used to create the
     *        <code>RuleExecutionSet</code> implementation.
     *        May be <code>null</code>.
     *
     * @throws RuleExecutionSetCreateException on rule execution set creation
     *         error.
     *
     * @return The created <code>RuleExecutionSet</code>.
     */
    public RuleExecutionSet createRuleExecutionSet(
            Element ruleExecutionSetElement, Map properties )
        throws RuleExecutionSetCreateException
    {
        // Prepare the DOM source
        Source source = new DOMSource( ruleExecutionSetElement );

        RuleSetReader reader = null;
        try
        {
            // Create a reader to handle the SAX events
            reader =
                new RuleSetReader( DefaultSemanticsRepository.getInstance( ) );
        }
        catch ( Exception e )
        {
            throw new RuleExecutionSetCreateException(
                "Couldn't get an instance of the DefaultSemanticsRepository: "
                + e );
        }

        try
        {
            // Prepare the result
            SAXResult result = new SAXResult( reader );

            // Create a transformer
            Transformer xformer =
                TransformerFactory.newInstance( ).newTransformer( );

            // Traverse the DOM tree
            xformer.transform( source, result );
            System.out.println( result.toString( ) );

        }
        catch ( TransformerException e )
        {
            throw new RuleExecutionSetCreateException(
                "could not create RuleExecutionSet: " + e );
        }

        try
        {
            RuleSet ruleSet = reader.getRuleSet( );
            LocalRuleExecutionSetProviderImpl localRuleExecutionSetProvider =
                new LocalRuleExecutionSetProviderImpl( );
            return localRuleExecutionSetProvider.createRuleExecutionSet(
                ruleSet, properties );
        }
        catch ( Exception e )
        {
            throw new RuleExecutionSetCreateException(
                "could not create RuleExecutionSet: " + e );
        }
    }

    /**
     * Creates a <code>RuleExecutionSet</code> implementation from a
     * Drools-specific Abstract Syntax Tree (AST) representation and
     * Drools-specific properties.
     * <p/>
     * This method accepts a <code>org.drools.RuleBase</code> object as its
     * vendor-specific AST representation.
     *
     * @param ruleExecutionSetAst the Drools representation of a
     *        rule execution set
     * @param properties additional properties used to create the
     *        <code>RuleExecutionSet</code> implementation.
     *        May be <code>null</code>.
     *
     * @throws RuleExecutionSetCreateException on rule execution set creation
     *         error.
     *
     * @return The created <code>RuleExecutionSet</code>.
     */
    public RuleExecutionSet createRuleExecutionSet(
            Serializable ruleExecutionSetAst, Map properties )
        throws RuleExecutionSetCreateException
    {
        if ( ruleExecutionSetAst instanceof RuleSet )
        {
            LocalRuleExecutionSetProviderImpl localRuleExecutionSetProvider =
                new LocalRuleExecutionSetProviderImpl( );
            return localRuleExecutionSetProvider.createRuleExecutionSet(
                ruleExecutionSetAst, properties );
        }
        else
        {
            throw new IllegalArgumentException( "Serializable object must be "
                + "an instance of org.drools.rule.RuleSet.  It was "
                + ruleExecutionSetAst.getClass( ).getName( ) );
        }
    }

    /**
     * Creates a <code>RuleExecutionSet</code> implementation from a URI.
     * The URI is opaque to the specification and may be used to refer to the
     * file system, a database, or Drools-specific datasource.
     *
     * @param ruleExecutionSetUri the URI to load the rule execution set from
     * @param properties additional properties used to create the
     *        <code>RuleExecutionSet</code> implementation.
     *        May be <code>null</code>.
     *
     * @throws RuleExecutionSetCreateException on rule execution set creation
     *         error.
     * @throws IOException if an I/O error occurs while accessing the URI
     *
     * @return The created <code>RuleExecutionSet</code>.
     */
    public RuleExecutionSet createRuleExecutionSet(
            String ruleExecutionSetUri, Map properties )
        throws RuleExecutionSetCreateException, IOException
    {
        InputStream in = null;
        try
        {
            LocalRuleExecutionSetProviderImpl localRuleExecutionSetProvider =
                new LocalRuleExecutionSetProviderImpl( );
            in = new URL( ruleExecutionSetUri ).openStream( );
            Reader reader = new InputStreamReader( in );
            return localRuleExecutionSetProvider.createRuleExecutionSet(
                reader, properties );
        }
        catch ( IOException ex )
        {
            throw ex;
        }
        catch ( Exception ex )
        {
            throw new RuleExecutionSetCreateException(
                "cannot create rule set", ex );
        }
        finally
        {
            if ( in != null )
            {
                try
                {
                    in.close( );
                }
                catch ( IOException e )
                {
                    e.printStackTrace( );
                }
            }
        }
    }
}
