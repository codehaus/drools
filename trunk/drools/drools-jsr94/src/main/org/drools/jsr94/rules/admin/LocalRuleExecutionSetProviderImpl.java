package org.drools.jsr94.rules.admin;

/*
 $Id: LocalRuleExecutionSetProviderImpl.java,v 1.9 2004-04-02 22:23:40 n_alex Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import org.drools.io.RuleSetReader;
import org.drools.RuleBase;

import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.HashMap;

/**
 * The <code>LocalRuleExecutionSetProvider</code> interface defines <code>RuleExecutionSet</code> 
 * creation methods for defining <code>RuleExecutionSets</code> from local (non-serializable) resources.
 *
 * @see LocalRuleExecutionSetProvider
 *
 * @author N. Alex Rupp (n_alex <at> codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class LocalRuleExecutionSetProviderImpl implements LocalRuleExecutionSetProvider
{
    /**
     * Creates a <code>RuleExecutionSet</code> implementation using a supplied
     * input stream and additional vendor-specific properties.
     *
     * @see LocalRuleExecutionSetProvider#createRuleExecutionSet(InputStream, Map)
     */
    public RuleExecutionSet createRuleExecutionSet(
            InputStream inputStream,
            Map properties)
            throws IOException,
            RuleExecutionSetCreateException
    {
        Reader reader = new InputStreamReader( inputStream );
        return createRuleExecutionSet( reader, properties );
    }

    /**
     * Creates a <code>RuleExecutionSet</code> implementation using
     * a supplied character stream Reader and vendor-specific properties.
     *
     * @see LocalRuleExecutionSetProvider#createRuleExecutionSet(Reader, Map)
     */
    public RuleExecutionSet createRuleExecutionSet(
            Reader in,
            Map properties)
            throws RuleExecutionSetCreateException
    {
        try
        {
            RuleSetReader ruleSetReader = new RuleSetReader();
            org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder();
            builder.addRuleSet( ruleSetReader.read( in ) );
            RuleBase ruleBase = builder.build();

            return createRuleExecutionSet( ruleBase, properties );
        }
        catch ( Exception ex )
        {
            throw new RuleExecutionSetCreateException( "cannot create rule set", ex );
        }
    }

    /**
     * <p> Creates a <code>RuleExecutionSet</code> implementation from a vendor
     * specific AST representation and vendor-specific properties. </p>
     *
     * <p> This method accepts <code>org.drools.rule.RuleBase</code> as the incoming
     * AST object parameter. </p>
     *
     * @see LocalRuleExecutionSetProvider#createRuleExecutionSet(Object, Map)
     */
    public RuleExecutionSet createRuleExecutionSet(
            Object astObject,
            Map properties)
            throws RuleExecutionSetCreateException
    {
        if (properties == null)
        {
            properties = new HashMap();
        }
        if (astObject instanceof RuleBase) {
            try
            {
                RuleBase ruleBase = (RuleBase) astObject;
                return new RuleExecutionSetImpl(ruleBase, properties);
            }
            catch ( Exception ex )
            {
                throw new RuleExecutionSetCreateException( "cannot create rule set", ex );
            }
        }
        throw new IllegalArgumentException(" " +
                "Incoming AST object must be an " +
                "org.drools.rule.RuleBase.  Was " +
                astObject.getClass().toString());
    }
}
