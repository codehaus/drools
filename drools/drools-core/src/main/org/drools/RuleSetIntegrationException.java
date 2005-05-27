package org.drools;

/*
 * $Id: RuleSetIntegrationException.java,v 1.4 2005-01-11 16:11:56 mproctor Exp $
 *
 * Copyright 2001-2004 (C) The Werken Company. All Rights Reserved.
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

import org.drools.rule.RuleSet;

/**
 * Indicates an error integrating a <code>RuleSet</code>
 * into a <code>RuleBase</code>.
 *
 * @see RuleBase#addRule
 * @see RuleBase#addRuleSet
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 *
 * @version $Id: RuleIntegrationException.java,v 1.6 2004/09/17 00:14:06
 *          mproctor Exp $
 */
public class RuleSetIntegrationException extends IntegrationException
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The rule. */
    private final RuleSet ruleSet;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param ruleSet
     *            The offending rule.
     */
    public RuleSetIntegrationException( RuleSet ruleSet )
    {
        this.ruleSet = ruleSet;
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the <code>RuleSet</code>.
     *
     * @return The ruleSet
     */
    public RuleSet getRuleSet( )
    {
        return this.ruleSet;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // java.lang.Throwable
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Retrieve the error message.
     *
     * @return The error message.
     */
    public String getMessage( )
    {
        return this.getRuleSet( ).getName( ) + " cannot be integrated";
    }
}
