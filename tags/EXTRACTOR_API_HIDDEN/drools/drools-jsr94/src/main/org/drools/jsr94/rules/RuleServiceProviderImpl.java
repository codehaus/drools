package org.drools.jsr94.rules;

/*
 * $Id: RuleServiceProviderImpl.java,v 1.10 2004-11-15 01:12:22 dbarnett Exp $
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

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.admin.RuleAdministrator;

import org.drools.jsr94.rules.admin.RuleAdministratorImpl;

/**
 * This class provides access to the <code>RuleRuntime</code> and
 * <code>RuleAdministrator</code> implementation supplied by Drools
 * when running under J2SE.
 * <p/>
 * This class should be used in environments without a JNDI provider -
 * typically when writing standalone J2SE clients. Within the J2EE environment
 * the <code>RuleServiceProvider</code> implementation class provided by Drools
 * should be retrieved using a JNDI lookup.
 * <p/>
 * This class should be constructed using the
 * <code>RuleServiceProviderManager.getRuleServiceProvider</code> method.
 *
 * @see RuleRuntimeImpl
 * @see RuleAdministratorImpl
 * @see RuleServiceProvider
 * @see javax.rules.RuleServiceProviderManager#getRuleServiceProvider(String)
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class RuleServiceProviderImpl extends RuleServiceProvider
{
    /** An instance of <code>RuleRuntimeImpl</code>. */
    private RuleRuntime ruleRuntime;

    /** An instance of <code>RuleAdministratorImpl</code>. */
    private RuleAdministrator ruleAdministrator;

    /**
     * Create a new <code>RuleServiceProviderImpl</code>.
     */
    public RuleServiceProviderImpl( )
    {
        // no special initialization required
    }

    /**
     * Returns a class instance of <code>RuleRuntime</code>. Specifically
     * an instance of the Drools <code>RuleRuntimeImpl</code> is returned.
     *
     * @return an instance of <code>RuleRuntime</code>
     */
    public synchronized RuleRuntime getRuleRuntime( )
    {
        if ( ruleRuntime != null )
        {
            return ruleRuntime;
        }
        return ruleRuntime = new RuleRuntimeImpl( );
    }

    /**
     * Returns a class instance of <code>RuleAdministrator</code>. Specifically
     * an instance of the Drools <code>RuleAdministratorImpl</code> is returned.
     *
     * @return an instance of <code>RuleAdministrator</code>
     */
    public synchronized RuleAdministrator getRuleAdministrator( )
    {
        if ( ruleAdministrator != null )
        {
            return ruleAdministrator;
        }
        return ruleAdministrator = new RuleAdministratorImpl( );
    }
}