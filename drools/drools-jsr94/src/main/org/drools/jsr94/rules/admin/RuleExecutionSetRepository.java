package org.drools.jsr94.rules.admin;

/*
 * $Id: RuleExecutionSetRepository.java,v 1.12 2006-01-16 01:31:23 michaelneale Exp $
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetRegisterException;

/**
 * Stores the registered <code>RuleExecutionSet</code> objects.
 * 
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public final class RuleExecutionSetRepository implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The Singleton instance of the repository. */
	// private static RuleExecutionSetRepository REPOSITORY;
	/** Holds the registered <code>RuleExecutionSet</code> objects. */
	private Map map = new HashMap();

	/** Private constructor; use <code>getInstance</code> instead. */
	public RuleExecutionSetRepository() {
		// Hide the constructor.
	}

	/**
	 * Gets the Singleton instance of a <code>RuleExecutionSetRepository</code>.
	 * 
	 * @return The Singleton instance of the repository.
	 */
	// public static synchronized RuleExecutionSetRepository getInstance( )
	// {
	// if ( RuleExecutionSetRepository.REPOSITORY != null )
	// {
	// return RuleExecutionSetRepository.REPOSITORY;
	// }
	// return RuleExecutionSetRepository.REPOSITORY =
	// new RuleExecutionSetRepository( );
	// }
	/**
	 * Retrieves a <code>List</code> of the URIs that currently have
	 * <code>RuleExecutionSet</code>s associated with them.
	 * 
	 * An empty list is returned is there are no associations.
	 * 
	 * @return a <code>List</code> of the URIs that currently have
	 *         <code>RuleExecutionSet</code>s associated with them.
	 */
	public List getRegistrations() {
		List list = new ArrayList();
		list.addAll(this.map.keySet());
		return list;
	}

	/**
	 * Get the <code>RuleExecutionSet</code> bound to this URI, or return
	 * <code>null</code>.
	 * 
	 * @param bindUri
	 *            the URI associated with the wanted
	 *            <code>RuleExecutionSet</code>.
	 * 
	 * @return the <code>RuleExecutionSet</code> bound to the given URI.
	 */
	public RuleExecutionSet getRuleExecutionSet(String bindUri) {
		return (RuleExecutionSet) this.map.get(bindUri);
	}

	/**
	 * Register a <code>RuleExecutionSet</code> under the given URI.
	 * 
	 * @param bindUri
	 *            the URI to associate with the <code>RuleExecutionSet</code>.
	 * @param ruleSet
	 *            the <code>RuleExecutionSet</code> to associate with the URI
	 * 
	 * @throws RuleExecutionSetRegisterException
	 *             if an error occurred that prevented registration (i.e. if
	 *             bindUri or ruleSet are <code>null</code>)
	 */
	public void registerRuleExecutionSet(String bindUri,
			RuleExecutionSet ruleSet) throws RuleExecutionSetRegisterException {
		if (bindUri == null) {
			throw new RuleExecutionSetRegisterException(
					"bindUri cannot be null");
		}
		if (ruleSet == null) {
			throw new RuleExecutionSetRegisterException(
					"ruleSet cannot be null");
		}
		this.map.put(bindUri, ruleSet);
	}

	/**
	 * Unregister a <code>RuleExecutionSet</code> from the given URI.
	 * 
	 * @param bindUri
	 *            the URI to disassociate with the <code>RuleExecutionSet</code>.
	 */
	public void unregisterRuleExecutionSet(String bindUri) {
		if (bindUri == null) {
			throw new NullPointerException("bindUri cannot be null");
		}
		this.map.remove(bindUri);
	}
}
