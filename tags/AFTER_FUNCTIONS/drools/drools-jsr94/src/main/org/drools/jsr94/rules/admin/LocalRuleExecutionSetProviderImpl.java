package org.drools.jsr94.rules.admin;

/*
 * $Id: LocalRuleExecutionSetProviderImpl.java,v 1.19 2004-12-05 20:37:06 dbarnett Exp $
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
import java.io.Reader;
import java.util.Map;

import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.xml.parsers.ParserConfigurationException;

import org.drools.RuleIntegrationException;
import org.drools.RuleSetIntegrationException;
import org.drools.io.RuleSetReader;
import org.drools.rule.RuleSet;
import org.xml.sax.SAXException;

/**
 * The Drools implementation of the <code>LocalRuleExecutionSetProvider</code>
 * interface which defines <code>RuleExecutionSet</code> creation methods for
 * defining <code>RuleExecutionSet</code>s from local (non-serializable)
 * resources.
 *
 * @see LocalRuleExecutionSetProvider
 *
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class LocalRuleExecutionSetProviderImpl
    implements LocalRuleExecutionSetProvider
{
    /** Default constructor. */
    public LocalRuleExecutionSetProviderImpl( )
    {
        super( );
    }

    /**
     * Creates a <code>RuleExecutionSet</code> implementation using a
     * supplied input stream and additional Drools-specific properties.
     * A Drools-specific rule execution set is read from the supplied
     * InputStream. The method <code>createRuleExecutionSet</code> taking
     * a Reader instance should be used if the source is a character
     * stream and encoding conversion should be performed.
     *
     * @param ruleExecutionSetStream
     *        an input stream used to read the rule execution set.
     * @param properties additional properties used to create the
     *        <code>RuleExecutionSet</code> implementation.
     *        May be <code>null</code>.
     *
     * @throws RuleExecutionSetCreateException
     *         on rule execution set creation error.
     *
     * @return The created <code>RuleExecutionSet</code>.
     */
    public RuleExecutionSet createRuleExecutionSet(
            InputStream ruleExecutionSetStream, Map properties )
        throws RuleExecutionSetCreateException
    {
        try
        {
            RuleSetReader setReader = new RuleSetReader( );
            RuleSet ruleSet = setReader.read( ruleExecutionSetStream );
            return this.createRuleExecutionSet( ruleSet, properties );
        }
        catch ( SAXException e )
        {
            throw new RuleExecutionSetCreateException(
                "cannot create rule set", e );
        }
        catch ( ParserConfigurationException e )
        {
            throw new RuleExecutionSetCreateException(
                "cannot create rule set", e );
        }
        catch ( IOException e )
        {
            throw new RuleExecutionSetCreateException(
                "cannot create rule set", e );
        }
    }

    /**
     * Creates a <code>RuleExecutionSet</code> implementation using a supplied
     * character stream Reader and additional Drools-specific properties. A
     * Drools-specific rule execution set is read from the supplied Reader.
     *
     * @param ruleExecutionSetReader
     *        a Reader used to read the rule execution set.
     * @param properties additional properties used to create the
     *        <code>RuleExecutionSet</code> implementation.
     *        May be <code>null</code>.
     *
     * @throws RuleExecutionSetCreateException
     *         on rule execution set creation error.
     *
     * @return The created <code>RuleExecutionSet</code>.
     */
    public RuleExecutionSet createRuleExecutionSet(
            Reader ruleExecutionSetReader, Map properties )
        throws RuleExecutionSetCreateException
    {
        try
        {
            RuleSetReader setReader = new RuleSetReader( );
            RuleSet ruleSet = setReader.read( ruleExecutionSetReader );
            return this.createRuleExecutionSet( ruleSet, properties );
        }
        catch ( SAXException e )
        {
            throw new RuleExecutionSetCreateException(
                "cannot create rule set", e );
        }
        catch ( ParserConfigurationException e )
        {
            throw new RuleExecutionSetCreateException(
                "cannot create rule set", e );
        }
        catch ( IOException e )
        {
            throw new RuleExecutionSetCreateException(
                "cannot create rule set", e );
        }
    }

    /**
     * Creates a <code>RuleExecutionSet</code> implementation from a
     * Drools-specific AST representation and Drools-specific properties.
     *
     * @param ruleExecutionSetAst
     *        the vendor representation of a rule execution set
     * @param properties additional properties used to create the
     *        <code>RuleExecutionSet</code> implementation.
     *        May be <code>null</code>.
     *
     * @throws RuleExecutionSetCreateException
     *         on rule execution set creation error.
     *
     * @return The created <code>RuleExecutionSet</code>.
     */
    public RuleExecutionSet createRuleExecutionSet(
            Object ruleExecutionSetAst, Map properties )
        throws RuleExecutionSetCreateException
    {
        if ( ruleExecutionSetAst instanceof RuleSet )
        {
            RuleSet ruleSet = ( RuleSet ) ruleExecutionSetAst;
            return this.createRuleExecutionSet( ruleSet, properties );
        }
        throw new RuleExecutionSetCreateException(
            " Incoming AST object must be an org.drools.rule.RuleSet.  Was "
            + ruleExecutionSetAst.getClass( ) );
    }

    /**
     * Creates a <code>RuleExecutionSet</code> implementation from a
     * <code>RuleSet</code> and Drools-specific properties.
     *
     * @param ruleSet a Drools <code>org.drools.rule.RuleSet</code>
     *        representation of a rule execution set.
     * @param properties additional properties used to create the
     *        RuleExecutionSet implementation. May be <code>null</code>.
     *
     * @throws RuleExecutionSetCreateException
     *         on rule execution set creation error.
     *
     * @return The created <code>RuleExecutionSet</code>.
     */
    private RuleExecutionSet createRuleExecutionSet(
            RuleSet ruleSet, Map properties )
        throws RuleExecutionSetCreateException
    {
        try
        {
            return new RuleExecutionSetImpl( ruleSet, properties );
        }
        catch ( RuleIntegrationException e )
        {
            throw new RuleExecutionSetCreateException(
                "Failed to create RuleExecutionSet", e );
        }
        catch ( RuleSetIntegrationException e )
        {
            throw new RuleExecutionSetCreateException(
                "Failed to create RuleExecutionSet", e );
        }
    }
}
