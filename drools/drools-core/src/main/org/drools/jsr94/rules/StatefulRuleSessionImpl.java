package org.drools.jsr94.rules;

/*
 $Id: StatefulRuleSessionImpl.java,v 1.3 2003-03-22 22:03:45 tdiesler Exp $

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

import org.drools.AssertionException;
import org.drools.DroolsException;
import org.drools.RetractionException;
import org.drools.jsr94.rules.admin.RuleExecutionSetImpl;
import org.drools.jsr94.rules.admin.RuleExecutionSetRepository;

import javax.rules.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This interface is a representation of a stateful rules engine session.
 * A stateful rules engine session exposes a stateful rule execution API to an underlying rules engine.
 * The session allows arbitrary objects to be added and removed to and from the working memory of the associated rules engine.
 * Additionally, objects currently in the working memory may be updated, and the working memory itself can be iterated.
 *
 * @see StatefulRuleSession
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class StatefulRuleSessionImpl extends RuleSessionImpl implements StatefulRuleSession {

    /** the rule set from the repository */
    private RuleExecutionSetImpl ruleSet;

    /** The working memory this session is associated with. */
    private JSR94TransactionalWorkingMemory workingMemory;

    /**
     * Gets the <code>RuleExecutionSet</code> for this URI and associated it with a RuleBase.
     *
     * @param bindUri the URI the <code>RuleExecutionSet</code> has been bound to
     * @throws RuleExecutionSetNotFoundException if there is no rule set under the given URI
     */
    StatefulRuleSessionImpl( String bindUri, Map properties ) throws RuleExecutionSetNotFoundException
    {

        // get the rule set from the repository
        RuleExecutionSetRepository repository = RuleExecutionSetRepository.getInstance();
        ruleSet = (RuleExecutionSetImpl) repository.getRuleExecutionSet( bindUri );
        if (ruleSet == null) throw new RuleExecutionSetNotFoundException( "no execution set bound to: " + bindUri );

        // Note: this breaks the factory intension of RuleBase.createTransactionalWorkingMemory
        workingMemory = new JSR94TransactionalWorkingMemory( ruleSet.getRuleBase() );
        workingMemory.setApplicationData( properties );

    }

    /**
     * Returns true if the given object is contained within the working memory of this rule session.
     *
     * @see StatefulRuleSessionImpl#containsObject
     */
    public boolean containsObject( Handle handle )
    {
        return workingMemory.getObjectHandles().contains( handle );
    }

    /**
     * Adds a given object to the working memory of this rule session.
     * The argument to this method is Object because in the non-managed env. not all objects should have
     * to implement Serialzable.
     * If the RuleSession is Serializable and it contains non-serializable fields a runtime exception will be thrown.
     *
     * @see StatefulRuleSessionImpl#addObject
     */
    public Handle addObject( Object object ) throws InvalidRuleSessionException
    {
        try
        {
            Handle handle = workingMemory.getNextHandle();
            workingMemory.assertObjectForHandle( handle, object );
            return handle;
        } catch (AssertionException ex)
        {
            throw new InvalidRuleSessionException( "cannot assert object", ex );
        }
    }

    /**
     * Adds a List of Objects to the working memory of this rule session.
     *
     * @see StatefulRuleSessionImpl#addObjects
     */
    public List addObjects( List list ) throws InvalidRuleSessionException
    {

        // return a list of handles
        List retList = new ArrayList();
        for (int i = 0; i < list.size(); i++)
        {
            retList.add( addObject( list.get( i ) ) );
        }
        return retList;
    }

    /**
     * Notifies the rules engine that a given object in working memory has changed.
     * The semantics of this call are equivalent to calling <code>removeObjectForHandle</code>
     * followed by <code>addObject</code>.
     * The original Handle is rebound to the new value for the Object however.
     *
     * @see StatefulRuleSessionImpl#updateObject
     */
    public void updateObject( Handle handle, Object object ) throws InvalidRuleSessionException
    {
        try
        {
            workingMemory.removeObjectForHandle( handle );
            workingMemory.assertObjectForHandle( handle, object );
        } catch (RetractionException ex)
        {
            throw new InvalidRuleSessionException( "cannot retract object", ex );
        } catch (AssertionException ex)
        {
            throw new InvalidRuleSessionException( "cannot assert object", ex );
        }
    }

    /**
     * Removes a given object from the working memory of this rule session.
     *
     * @see StatefulRuleSessionImpl#removeObject
     */
    public void removeObject( Handle handle ) throws InvalidRuleSessionException
    {
        try
        {
            workingMemory.removeObjectForHandle( handle );
        } catch (RetractionException ex)
        {
            throw new InvalidRuleSessionException( "cannot retract object", ex );
        }
    }

    /**
     * Returns a List of all objects in the working memory of this rule session
     * that pass the default <code>RuleExecutionSet</code> filter (if present).
     *
     * @see StatefulRuleSessionImpl#getObjects
     */
    public List getObjects()
    {

        List outList = new ArrayList();
        outList.addAll( workingMemory.getObjects() );

        // apply the default filter
        ObjectFilter objectFilter = ruleSet.getObjectFilter();
        if (objectFilter != null)
        {

            // apply the filter
            List cpyList = new ArrayList();
            for (int i = 0; i < outList.size(); i++)
            {
                Object obj = objectFilter.filter( outList.get( i ) );
                if (obj != null) cpyList.add( obj );
            }
            outList = cpyList;
        }

        return outList;
    }

    /**
     * Returns a List over the objects in the working memory of this rule session based upon a given object filter.
     *
     * @see StatefulRuleSessionImpl#getObjects(ObjectFilter)
     */
    public List getObjects( ObjectFilter objectFilter )
    {

        List outList = new ArrayList();
        outList.addAll( workingMemory.getObjects() );

        // apply the filter
        List cpyList = new ArrayList();
        for (int i = 0; i < outList.size(); i++)
        {
            Object obj = objectFilter.filter( outList.get( i ) );
            if (obj != null) cpyList.add( obj );
        }

        return cpyList;
    }

    /**
     * Executes the rules in the bound rule execution set using the objects present in working memory
     * until no rule is executable anymore.
     *
     * @see StatefulRuleSessionImpl#executeRules
     */
    public void executeRules() throws InvalidRuleSessionException
    {
        try
        {
            workingMemory.commit();
        } catch (DroolsException ex)
        {
            throw new InvalidRuleSessionException( "cannot commit working memory", ex );
        }
    }

    /**
     * Resets this rule session. Calling this method will remove all objects from the working memory
     * of this rule session and will reset any other state associated with this rule session.
     *
     * @see StatefulRuleSessionImpl#reset
     */
    public void reset()
    {
        workingMemory.abort();
        workingMemory = new JSR94TransactionalWorkingMemory( ruleSet.getRuleBase() );
    }

    /**
     * Returns the Object within the <code>StatefulRuleSession</code> associated with a Handle.
     *
     * @see StatefulRuleSessionImpl#getObject
     */
    public Object getObject( Handle handle )
    {
        return workingMemory.getObject( handle );
    }

    /**
     * Releases all resources used by this rule session.
     * This method renders this rule session unusable until it is reacquired through the RuleRuntime.
     */
    public void release()
    {
    }
}
