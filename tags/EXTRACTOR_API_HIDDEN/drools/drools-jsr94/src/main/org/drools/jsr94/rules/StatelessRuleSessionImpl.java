package org.drools.jsr94.rules;

/*
 * $Id: StatelessRuleSessionImpl.java,v 1.10 2004-11-15 01:12:22 dbarnett Exp $
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.StatelessRuleSession;

import org.drools.FactException;
import org.drools.WorkingMemory;
import org.drools.jsr94.rules.admin.RuleExecutionSetImpl;
import org.drools.jsr94.rules.admin.RuleExecutionSetRepository;

/**
 * The Drools implementation of the <code>StatelessRuleSession</code> interface
 * which is a representation of a stateless rules engine session. A stateless
 * rules engine session exposes a stateless rule execution API to an underlying
 * rules engine.
 *
 * @see StatelessRuleSession
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class StatelessRuleSessionImpl extends RuleSessionImpl
    implements StatelessRuleSession
{
    /**
     * Gets the <code>RuleExecutionSet</code> for this URI and associates it
     * with a RuleBase.
     *
     * @param bindUri the URI the <code>RuleExecutionSet</code> has been bound
     *        to
     * @param properties additional properties used to create the
     *        <code>RuleSession</code> implementation.
     *
     * @throws RuleExecutionSetNotFoundException if there is no rule set under
     *         the given URI
     */
    StatelessRuleSessionImpl( String bindUri, Map properties )
        throws RuleExecutionSetNotFoundException
    {
        setProperties( properties );

        RuleExecutionSetRepository repository =
            RuleExecutionSetRepository.getInstance( );

        RuleExecutionSetImpl ruleSet =
            ( RuleExecutionSetImpl ) repository.getRuleExecutionSet( bindUri );

        if ( ruleSet == null )
        {
            throw new RuleExecutionSetNotFoundException(
                "RuleExecutionSet unbound: " + bindUri );
        }

        setRuleExecutionSet( ruleSet );
    }

    /**
     * Executes the rules in the bound rule execution set using the supplied
     * list of objects. A <code>List</code> is returned containing the objects
     * created by (or passed  into the rule session) the executed rules that
     * pass the filter test of the default <code>RuleExecutionSet</code>
     * <code>ObjectFilter</code> (if present).
     * <p/>
     * The returned list may not neccessarily include all objects passed, and
     * may include <code>Object</code>s created by side-effects. The execution
     * of a <code>RuleExecutionSet</code> can add, remove and update objects.
     * Therefore the returned object list is dependent on the rules that are
     * part of the executed <code>RuleExecutionSet</code> as well as Drools
     * specific rule engine behavior.
     *
     * @param objects the objects used to execute rules.
     *
     * @return a <code>List</code> containing the objects
     *         as a result of executing the rules.
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    public List executeRules( List objects ) throws InvalidRuleSessionException
    {
        return executeRules(
            objects, getRuleExecutionSet( ).getObjectFilter( ) );
    }

    /**
     * Executes the rules in the bound rule execution set using the supplied
     * list of objects. A <code>List</code> is returned containing the objects
     * created by (or passed  into the rule engine) the executed rules and
     * filtered with the supplied object filter.
     * <p/>
     * The returned list may not neccessarily include all objects passed, and
     * may include <code>Object</code>s created by side-effects. The execution
     * of a <code>RuleExecutionSet</code> can add, remove and update objects.
     * Therefore the returned object list is dependent on the rules that are
     * part of the executed <code>RuleExecutionSet</code> as well as Drools
     * specific rule engine behavior.
     *
     * @param objects the objects used to execute rules.
     * @param filter the object filter.
     *
     * @return a <code>List</code> containing the objects as a result
     *         of executing rules, after passing through the supplied
     *         object filter.
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    public List executeRules( List objects, ObjectFilter filter )
        throws InvalidRuleSessionException
    {
        WorkingMemory workingMemory = newWorkingMemory( );

        try
        {
            for ( Iterator objectIter = objects.iterator( );
                  objectIter.hasNext( ); )
            {
                workingMemory.assertObject( objectIter.next( ) );
            }

            workingMemory.fireAllRules( );
        }
        catch ( FactException e )
        {
            throw new InvalidRuleSessionException( e.getMessage( ), e );
        }

        List results = workingMemory.getObjects( );

        applyFilter( results, filter );

        return results;
    }
}
