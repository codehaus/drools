package org.drools;

/*
 $Id: RuleBase.java,v 1.16 2003-10-14 22:57:57 bob Exp $

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

import org.drools.reteoo.Builder;
import org.drools.reteoo.Rete;
import org.drools.reteoo.impl.ReteImpl;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;

/** Collection of <code>Rule</code>s.
 *
 *  @see Rule
 *  @see RuleSet
 *
 *  @author <a href="bob@werken.com">bob mcwhirter</a>
 */
public class RuleBase
{
    private static Log log = LogFactory.getLog(RuleBase.class);

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The root Rete-OO for this <code>RuleBase</code>. */
    private ReteImpl rete;

    /** Rete-OO Network builder. */
    private Builder  builder;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public RuleBase()
    {
        this.rete    = new ReteImpl();
        this.builder = new Builder( this.rete );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve this <code>RuleBase's</code> <code>Builder</code>.
     *
     *  @return The <code>Builder</code>.
     */
    private Builder getBuilder()
    {
        return this.builder;
    }

    /** Add a <code>RuleSet</code> of <code>Rules</code> to this <code>RuleBase</code>.
     *
     *  <p>
     *  A <code>RuleSet</code> may be added to multiple <code>RuleBases</code>.
     *  Any changes to a <code>RuleSet</code> or its component <code>Rule</code>
     *  once it has been added are ignored.
     *  </p>
     *
     *  @param ruleSet The <code>RuleSet</code> to add.
     *
     *  @throws RuleIntegrationException If a member rule does not allow for
     *          complete and correct integration into the underlying Rete network.
     */
    public void addRuleSet(RuleSet ruleSet) throws RuleIntegrationException
    {
       log.debug("addRuleSet: " + ruleSet);

        Iterator ruleIter = ruleSet.getRuleIterator();
        Rule     eachRule = null;

        while ( ruleIter.hasNext() )
        {
            eachRule = (Rule) ruleIter.next();

            addRule( eachRule );
        }
    }

    /** Add a <code>Rule</code> to this <code>RuleBase</code>.
     *
     *  <p>
     *  A <code>Rule</code> may be added to multiple <code>RuleBases</code>.
     *  Any changes to a <code>Rule</code> once it has been added are ignored.
     *  </p>
     *
     *  @param rule The rule to add.
     *
     *  @throws RuleIntegrationException If the rule does not allow for
     *          complete and correct integration into the underlying Rete
     *          network.
     */
    public void addRule(Rule rule) throws RuleIntegrationException
    {
        log.debug("addRule: " + rule);

        getBuilder().addRule( rule );
    }

    /** Create a new <code>WorkingMemory</code> session for
     *  this <code>RuleBase</code>.
     *
     *  @see WorkingMemory
     *
     *  @return A newly initialized <code>WorkingMemory</code>.
     */
    public WorkingMemory newWorkingMemory()
    {
        return new WorkingMemory( this );
    }

    /** @deprecated In favour of {@link #newWorkingMemory}.
     */
    public WorkingMemory createWorkingMemory()
    {
        return newWorkingMemory();
    }

    /** Retrieve the Rete-OO network for this <code>RuleBase</code>.
     *
     *  @return The RETE-OO network. 
     */
    Rete getRete()
    {
        return this.rete;
    }

    /** Assert a new fact object into this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  @param object The object to assert.
     *  @param workingMemory The working memory session.
     *
     *  @throws AssertionException if an error occurs during assertion.
     */
    void assertObject(Object object,
                      WorkingMemory workingMemory) throws AssertionException
    {
        getRete().assertObject( object,
                                workingMemory );
    }
    
    /** Retract a fact object from this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  @param object The object to retract.
     *  @param workingMemory The working memory session.
     *
     *  @throws RetractionException if an error occurs during retraction.
     */
    void retractObject(Object object,
                       WorkingMemory workingMemory) throws RetractionException
    {
        getRete().retractObject( object,
                                 workingMemory );
    }
    
    /** Modify a fact object in this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  With the exception of time-based nodes, modification of
     *  a fact object is semantically equivelent to retracting and
     *  re-asserting it.
     *
     *  @param object The object to modify.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException if an error occurs during assertion.
     */
    void modifyObject(Object object,
                      WorkingMemory workingMemory) throws FactException
    {
        getRete().modifyObject( object,
                                workingMemory );
    }
}
