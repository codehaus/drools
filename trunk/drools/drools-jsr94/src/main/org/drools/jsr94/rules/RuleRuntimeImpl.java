package org.drools.jsr94.rules;

/*
 * $Id: RuleRuntimeImpl.java,v 1.9 2004-09-17 00:29:38 mproctor Exp $
 * 
 * Copyright 2002 (C) The Werken Company. All Rights Reserved.
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

import java.util.List;
import java.util.Map;

import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleSession;
import javax.rules.RuleSessionTypeUnsupportedException;

import org.drools.jsr94.rules.admin.RuleExecutionSetRepository;

/**
 * <code>RuleRuntime</code> interface.
 * 
 * @see RuleRuntime
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 */
public class RuleRuntimeImpl implements RuleRuntime
{
    /**
     * Creates a <code>RuleSession</code> implementation using the supplied
     * vendor-specific rule execution set registration URI.
     * 
     * @see RuleRuntime#createRuleSession
     */
    public RuleSession createRuleSession(String bindUri,
                                         Map properties,
                                         int ruleSessionType) throws RuleExecutionSetNotFoundException,
                                                             RuleSessionTypeUnsupportedException
    {

        if ( ruleSessionType == RuleRuntime.STATELESS_SESSION_TYPE )
        {
            StatelessRuleSessionImpl session = new StatelessRuleSessionImpl(
                                                                             bindUri,
                                                                             properties );
            return session;
        }

        if ( ruleSessionType == RuleRuntime.STATEFUL_SESSION_TYPE )
        {
            StatefulRuleSessionImpl session = new StatefulRuleSessionImpl(
                                                                           bindUri,
                                                                           properties );
            return session;
        }

        throw new RuleSessionTypeUnsupportedException( "invalid session type: "
                                                       + ruleSessionType );
    }

    /**
     * Retrieves a List of the URIs that currently have
     * <code>RuleExecutionSets</code> associated with them.
     * 
     * An empty list is returned is there are no associations.
     * 
     * @see RuleRuntime#getRegistrations
     */
    public List getRegistrations()
    {
        RuleExecutionSetRepository repository = RuleExecutionSetRepository
                                                                          .getInstance( );
        return repository.getRegistrations( );
    }
}