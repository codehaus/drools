package org.drools.jsr94.rules;

/*
 * $Id: StatelessRuleSessionImpl.java,v 1.7 2004-09-17 00:29:38 mproctor Exp $
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
 * This interface is a representation of a stateless rules engine session. A
 * stateless rules engine session exposes a stateless rule execution API to an
 * underlying rules engine.
 * 
 * @see StatelessRuleSession
 * 
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class StatelessRuleSessionImpl extends RuleSessionImpl implements
                                                             StatelessRuleSession
{
    /**
     * Gets the <code>RuleExecutionSet</code> for this URI and associated it
     * with a RuleBase.
     * 
     * @param bindUri the URI the <code>RuleExecutionSet</code> has been bound
     *        to
     * @throws RuleExecutionSetNotFoundException if there is no rule set under
     *         the given URI
     */
    StatelessRuleSessionImpl(String bindUri, Map properties) throws RuleExecutionSetNotFoundException
    {
        setProperties( properties );

        RuleExecutionSetRepository repository = RuleExecutionSetRepository
                                                                          .getInstance( );

        RuleExecutionSetImpl ruleSet = ( RuleExecutionSetImpl ) repository
                                                                          .getRuleExecutionSet( bindUri );

        if ( ruleSet == null )
        {
            throw new RuleExecutionSetNotFoundException(
                                                         "RuleExecutionSet unbound: "
                                                                                                                                                                                                                                    + bindUri );
        }

        setRuleExecutionSet( ruleSet );
    }

    /**
     * @see StatelessRuleSession
     */
    public List executeRules(List objects) throws InvalidRuleSessionException
    {
        return executeRules( objects, getRuleExecutionSet( ).getObjectFilter( ) );
    }

    /**
     * @see StatelessRuleSession#executeRules(List,ObjectFilter)
     */
    public List executeRules(List objects, ObjectFilter objectFilter) throws InvalidRuleSessionException
    {
        WorkingMemory workingMemory = newWorkingMemory( );

        try
        {
            for ( Iterator objectIter = objects.iterator( ); objectIter
                                                                       .hasNext( ); )
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

        applyFilter( results, objectFilter );

        return results;
    }
}