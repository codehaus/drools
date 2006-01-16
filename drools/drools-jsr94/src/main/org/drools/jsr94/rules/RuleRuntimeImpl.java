package org.drools.jsr94.rules;

/*
 * $Id: RuleRuntimeImpl.java,v 1.13 2006-01-16 01:31:22 michaelneale Exp $
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

import java.util.List;
import java.util.Map;

import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleSession;
import javax.rules.RuleSessionTypeUnsupportedException;

import org.drools.jsr94.rules.admin.RuleExecutionSetRepository;

/**
 * The Drools implementation of the <code>RuleRuntime</code> interface which
 * is the access point for runtime execution of <code>RuleExecutionSet</code>s.
 * It provides methods to create <code>RuleSession</code> implementation as
 * well as methods to retrieve <code>RuleExecutionSet</code>s that have been
 * previously registered using the <code>RuleAdministrator</code>. <p/> The
 * <code>RuleRuntime</code> should be accessed through the
 * <code>RuleServiceProvider</code>. An instance of the
 * <code>RuleRuntime</code> can be retrieved by calling: <p/> <code>
 * RuleServiceProvider ruleServiceProvider =
 *     RuleServiceProvider.newInstance();<br/>
 * RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();
 * </code>
 * <p/> Note: the release method must be called on the <code>RuleSession</code>
 * to clean up all resources used by the <code>RuleSession</code>.
 * 
 * @see RuleRuntime
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 */
public class RuleRuntimeImpl implements RuleRuntime {
	private static final long serialVersionUID = 1L;

	private RuleExecutionSetRepository repository;

	/**
	 * Create a new <code>RuleRuntimeImpl</code>.
	 */
	public RuleRuntimeImpl(RuleExecutionSetRepository repository) {
		this.repository = repository;
		// no special initialization required
	}

	/**
	 * Creates a <code>RuleSession</code> implementation using the supplied
	 * Drools-specific rule execution set registration URI.
	 * 
	 * @param uri
	 *            the URI for the <code>RuleExecutionSet</code>
	 * @param properties
	 *            additional properties used to create the
	 *            <code>RuleSession</code> implementation.
	 * @param ruleSessionType
	 *            the type of rule session to create.
	 * 
	 * @throws RuleSessionTypeUnsupportedException
	 *             if the ruleSessionType is not supported by Drools or the
	 *             RuleExecutionSet
	 * @throws RuleExecutionSetNotFoundException
	 *             if the URI could not be resolved into a
	 *             <code>RuleExecutionSet</code>
	 * 
	 * @return The created <code>RuleSession</code>.
	 */
	public RuleSession createRuleSession(String uri, Map properties,
			int ruleSessionType) throws RuleSessionTypeUnsupportedException,
			RuleExecutionSetNotFoundException {

		if (ruleSessionType == RuleRuntime.STATELESS_SESSION_TYPE) {
			StatelessRuleSessionImpl session = new StatelessRuleSessionImpl(
					uri, properties, repository);
			return session;
		}

		if (ruleSessionType == RuleRuntime.STATEFUL_SESSION_TYPE) {
			StatefulRuleSessionImpl session = new StatefulRuleSessionImpl(uri,
					properties, repository);
			return session;
		}

		throw new RuleSessionTypeUnsupportedException("invalid session type: "
				+ ruleSessionType);
	}

	/**
	 * Retrieves a <code>List</code> of the URIs that currently have
	 * <code>RuleExecutionSet</code>s associated with them. An empty list is
	 * returned is there are no associations.
	 * 
	 * @return a <code>List</code> of <code>String</code>s (URIs)
	 */
	public List getRegistrations() {
		return repository.getRegistrations();
	}
}
