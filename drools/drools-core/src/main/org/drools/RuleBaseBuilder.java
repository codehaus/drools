package org.drools;

/*
 * $Id: RuleBaseBuilder.java,v 1.19 2004-12-14 21:00:27 mproctor Exp $
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

import org.drools.reteoo.Builder;
import org.drools.reteoo.FactHandleFactory;
import org.drools.rule.RuleSet;
import org.drools.spi.ConflictResolver;
import org.drools.spi.RuleBaseContext;

/**
 * Factory for constructing a <code>RuleBase</code>.
 *
 * <p>
 * The <code>RuleBaseBuilder</code> integrates the added <code>RuleSet</code>
 * s into the <b>Rete </b> network. A <code>RuleBaseBuilder</code> may be
 * re-used after building a <code>RuleBase</code> but it may not be used to
 * build multiple <code>RuleBase</code> s simultaneously by multiple threads.
 * </p>
 *
 * @see #build
 * @see RuleSet
 * @see RuleBase
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 *
 * @version $Id: RuleBaseBuilder.java,v 1.19 2004-12-14 21:00:27 mproctor Exp $
 */
public class RuleBaseBuilder
{
    // ----------------------------------------------------------------------
    // Instance members
    // ----------------------------------------------------------------------

    /** Underlying Rete builder. */
    private Builder builder;
    
    private RuleBaseContext ruleBaseContext;

    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------

    /**
     * Construct.
     */
    public RuleBaseBuilder( )
    {
        this.ruleBaseContext = new RuleBaseContext( );
        this.builder = new Builder( ruleBaseContext );
        
    }
    
    /**
     * Construct.
     */
    public RuleBaseBuilder( RuleBaseContext ruleBaseContext)
    {
        this.ruleBaseContext = ruleBaseContext;
        this.builder = new Builder( ruleBaseContext );
    }    

    // ----------------------------------------------------------------------
    // Instance methods
    // ----------------------------------------------------------------------

    /**
     * Add a <code>RuleSet</code>.
     *
     * @param ruleSet
     *            The rule-set to add.
     *
     * @throws RuleIntegrationException
     *             If an error occurs while attempting to integrate the rules
     *             into the Rete network..
     */
    public void addRuleSet( RuleSet ruleSet ) throws RuleSetIntegrationException, RuleIntegrationException
    {
        this.builder.addRuleSet( ruleSet );
    }

    /**
     * Build the <code>RuleBase</code>.
     *
     * <p>
     * Builds the <code>RuleBase</code> based upon all previously added
     * <code>RuleSet</code>s.
     * </p>
     *
     * @see #addRuleSet
     *
     * @return The new rule-base.
     */
    public RuleBase build( )
    {
        return this.builder.buildRuleBase( );
    }

    public void setFactHandleFactory( FactHandleFactory factHandleFactory )
    {
        this.builder.setFactHandleFactory( factHandleFactory );
    }

    public void setConflictResolver( ConflictResolver conflictResolver )
    {
        this.builder.setConflictResolver( conflictResolver );
    }
}
