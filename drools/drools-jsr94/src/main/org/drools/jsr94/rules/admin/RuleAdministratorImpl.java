package org.drools.jsr94.rules.admin;

/*
 * $Id: RuleAdministratorImpl.java,v 1.13 2004-12-04 04:33:58 dbarnett Exp $
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

import java.util.Map;

import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetDeregistrationException;
import javax.rules.admin.RuleExecutionSetProvider;

/**
 * The Drools implementation of the <code>RuleAdministrator</code> interface
 * which is used by rule execution set administrators to load rule execution
 * sets from external sources and create a <code>RuleExecutionSet</code>
 * runtime object.
 * <p/>
 * The <code>RuleAdministrator</code> should be accessed by calling:
 * <p/>
 * <code>
 * RuleServiceProvider ruleServiceProvider =
 *     RuleServiceProvider.newInstance();<br/>
 * RuleAdministrator ruleAdministration =
 *     ruleServiceProvider.getRuleAdministrator();
 * </code>
 * <p/>
 * In an additional step the administrator may also choose to bind the
 * <code>RuleExecutionSet</code> instance to a URI so that it is globally
 * accessible and <code>RuleSession</code>s can be created for the
 * <code>RuleExecutionSet</code> through the RuleRuntime.
 *
 * @see RuleAdministrator
 *
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class RuleAdministratorImpl implements RuleAdministrator
{
    /** Default constructor. */
    public RuleAdministratorImpl( )
    {
        super( );
    }

    /**
     * Returns a <code>RuleExecutionSetProvider</code> implementation.
     *
     * @param properties additional properties
     *
     * @return The created <code>RuleExecutionSetProvider</code>.
     */
    public RuleExecutionSetProvider getRuleExecutionSetProvider(
        Map properties )
    {
        return new RuleExecutionSetProviderImpl( );
    }

    /**
     * Returns a <code>LocalRuleExecutionSetProvider</code> implementation.
     *
     * Returns a <code>LocalRuleExecutionSetProvider</code> implementation
     * or null if this implementation does not support creating a
     * <code>RuleExecutionSet</code> from non-serializable resources.
     *
     * @param properties additional properties
     *
     * @return The created <code>LocalRuleExecutionSetProvider</code>.
     */
    public LocalRuleExecutionSetProvider getLocalRuleExecutionSetProvider(
        Map properties )
    {
        return new LocalRuleExecutionSetProviderImpl( );
    }

    /**
     * Registers a <code>RuleExecutionSet</code> and associates it with a
     * given URI. Once a <code>RuleExecutionSet</code> has been registered it
     * is accessible to runtime clients through the <code>RuleRuntime</code>.
     * If a <code>RuleExecutionSet</code> has already been associated with
     * the URI it should be deregistered (as if
     * <code>deregisterRuleExecutionSet/</code> had been called) and the URI
     * should be associated with the new <code>RuleExecutionSet</code>.
     *
     * @param bindUri the URI to associate with the
     *        <code>RuleExecutionSet</code>.
     * @param set the <code>RuleExecutionSet</code> to associate with the URI
     * @param properties additional properties used to perform the registration
     */
    public void registerRuleExecutionSet(
            String bindUri, RuleExecutionSet set, Map properties )
    {
        // Note: an existing RuleExecutionSet is simply replaced
        RuleExecutionSetRepository repository =
            RuleExecutionSetRepository.getInstance( );
        repository.registerRuleExecutionSet( bindUri, set );
    }

    /**
     * Unregisters a previously registered <code>RuleExecutionSet</code> from
     * a URI.
     *
     * @param bindUri the URI to disassociate with the
     *        <code>RuleExecutionSet</code>.
     * @param properties additional properties used to perform the
     *        deregistration
     *
     * @throws RuleExecutionSetDeregistrationException if an error occurred that
     *         prevented unregistration
     */
    public void deregisterRuleExecutionSet( String bindUri, Map properties )
        throws RuleExecutionSetDeregistrationException
    {
        RuleExecutionSetRepository repository =
            RuleExecutionSetRepository.getInstance( );

        if ( repository.getRuleExecutionSet( bindUri ) == null )
        {
            throw new RuleExecutionSetDeregistrationException(
                "no execution set bound to: " + bindUri );
        }

        repository.unregisterRuleExecutionSet( bindUri );
    }
}
