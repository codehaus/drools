package org.drools.jsr94.rules.admin;

/*
 $Id: RuleAdministratorImpl.java,v 1.7 2004-04-04 02:54:55 n_alex Exp $

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

import javax.rules.admin.*;
import java.util.Map;

/**
 * The <code>RuleAdministrator</code> interface is used by rule execution set
 * administrators to load rule execution sets from external sources and
 * create a RuleExecutionSet runtime object.
 *
 * @see RuleAdministrator
 *
 * @author N. Alex Rupp (n_alex <at> codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class RuleAdministratorImpl implements RuleAdministrator
{
    /**
     * Returns a <code>RemoteRuleExecutionSetProvider</code> implementation.
     *
     * @see RuleAdministrator
     */
    public RuleExecutionSetProvider getRuleExecutionSetProvider( Map properties )
    {
        return new RuleExecutionSetProviderImpl();
    }

    /**
     * Returns a <code>LocalRuleExecutionSetProvider</code> implementation.
     *
     * @see RuleAdministrator#getLocalRuleExecutionSetProvider
     */
    public LocalRuleExecutionSetProvider getLocalRuleExecutionSetProvider( Map properties )
    {
        return new LocalRuleExecutionSetProviderImpl();
    }

    /**
     * Registers a <code>RuleExecutionSet</code> and associates it with a given URI.
     *
     * @see RuleAdministrator#registerRuleExecutionSet
     */
    public void registerRuleExecutionSet( String bindUri, RuleExecutionSet ruleExecutionSet, Map properties ) throws RuleExecutionSetRegisterException
    {
        try
        {
            // Note: an existing RuleExecutionSet is simply replaced
            RuleExecutionSetRepository repository = RuleExecutionSetRepository.getInstance();
            repository.registerRuleExecutionSet( bindUri, ruleExecutionSet );
        }
        catch ( Exception ex )
        {
            throw new RuleExecutionSetRegisterException( "cannot register rule execution set", ex );
        }
    }

    /**
     * Unregisters a previously registers <code>RuleExecutionSet</code> from a URI.
     *
     * @see RuleAdministrator#deregisterRuleExecutionSet
     */
    public void deregisterRuleExecutionSet( String bindUri, Map properties ) throws RuleExecutionSetDeregistrationException
    {
        RuleExecutionSetRepository repository = RuleExecutionSetRepository.getInstance();

        if ( repository.getRuleExecutionSet( bindUri ) == null )
            throw new RuleExecutionSetDeregistrationException( "no execution set bound to: " + bindUri );

        try
        {
            repository.unregisterRuleExecutionSet( bindUri );
        }
        catch ( Exception ex )
        {
            throw new RuleExecutionSetDeregistrationException( "cannot unregister rule set", ex );
        }
    }
}