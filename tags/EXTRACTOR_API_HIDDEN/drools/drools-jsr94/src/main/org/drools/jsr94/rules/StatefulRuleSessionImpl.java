package org.drools.jsr94.rules;

/*
 * $Id: StatefulRuleSessionImpl.java,v 1.15 2004-11-15 01:12:22 dbarnett Exp $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.rules.Handle;
import javax.rules.InvalidHandleException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.StatefulRuleSession;

import org.drools.DroolsException;
import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.NoSuchFactObjectException;
import org.drools.jsr94.rules.admin.RuleExecutionSetImpl;
import org.drools.jsr94.rules.admin.RuleExecutionSetRepository;

/**
 * The Drools implementation of the <code>StatefulRuleSession</code> interface
 * which is a representation of a stateful rules engine session. A stateful
 * rules engine session exposes a stateful rule execution API to an underlying
 * rules engine. The session allows arbitrary objects to be added and removed
 * to and from the rule session state. Additionally, objects currently part of
 * the rule session state may be updated.
 * <p/>
 * There are inherently side-effects to adding objects to the rule session
 * state. The execution of a RuleExecutionSet can add, remove and update objects
 * in the rule session state. The objects in  the rule session state are
 * therefore dependent on the rules within the <code>RuleExecutionSet</code> as
 * well as the rule engine vendor's specific rule engine behavior.
 * <p/>
 * <code>Handle</code> instances are used by the rule engine vendor to track
 * <code>Object</code>s added to the rule session state. This allows multiple
 * instances of equivalent <code>Object</code>s to be added to the session state
 * and identified, even after serialization.
 *
 * @see StatefulRuleSession
 *
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class StatefulRuleSessionImpl
    extends RuleSessionImpl implements StatefulRuleSession
{
    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

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
    StatefulRuleSessionImpl( String bindUri, Map properties )
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
                "no execution set bound to: " + bindUri );
        }

        setRuleExecutionSet( ruleSet );

        initWorkingMemory( );
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /**
     * Returns <code>true</code> if the given object is contained
     * within rulesession state of this rule session.
     *
     * @param objectHandle the handle to the target object.
     *
     * @return <code>true</code> if the given object is contained
     *         within the rule session state of this rule session.
     */
    public boolean containsObject( Handle objectHandle )
    {
        if ( objectHandle instanceof FactHandle )
        {
            return getWorkingMemory( ).containsObject( ( FactHandle ) objectHandle );
        }

        return false;
    }

    /**
     * Adds a given object to the rule session state of this rule session.
     * The argument to this method is Object because in the non-managed
     * env. not all objects should have to implement Serializable. If the
     * <code>RuleSession</code> is <code>Serializable</code> and it contains
     * non-serializable fields a runtime exception will be thrown.
     *
     * @param object the object to be added.
     *
     * @return the Handle for the newly added Object
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    public Handle addObject( Object object ) throws InvalidRuleSessionException
    {
        checkRuleSessionValidity( );

        try
        {
            return ( Handle ) getWorkingMemory( ).assertObject( object );
        }
        catch ( FactException e )
        {
            throw new InvalidRuleSessionException( "cannot assert object", e );
        }
    }

    /**
     * Adds a <code>List</code> of <code>Object</code>s to the rule session
     * state of this rule session.
     *
     * @param objList the objects to be added.
     *
     * @return a <code>List</code> of <code>Handle</code>s, one for each added
     *         <code>Object</code>. The <code>List</code> must be ordered in
     *         the same order as the input <code>objList</code>.
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    public List addObjects( List objList ) throws InvalidRuleSessionException
    {
        checkRuleSessionValidity( );

        List handles = new ArrayList( );

        for ( Iterator objectIter = objList.iterator( ); objectIter.hasNext( ); )
        {
            handles.add( addObject( objectIter.next( ) ) );
        }
        return handles;
    }

    /**
     * Notifies the rules engine that a given object in the rule session
     * state has changed.
     * <p/>
     * The semantics of this call are equivalent to calling
     * <code>removeObject</code> followed by <code>addObject</code>. The
     * original <code>Handle</code> is rebound to the new value for the
     * <code>Object</code> however.
     *
     * @param objectHandle the handle to the original object.
     * @param newObject the new object to bind to the handle.
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     * @throws InvalidHandleException if the input <code>Handle</code>
     *         is no longer valid
     */
    public void updateObject( Handle objectHandle, Object newObject )
        throws InvalidRuleSessionException, InvalidHandleException
    {
        checkRuleSessionValidity( );

        if ( objectHandle instanceof FactHandle )
        {
            try
            {
                getWorkingMemory( ).modifyObject(
                    ( FactHandle ) objectHandle, newObject );
            }
            catch ( FactException e )
            {
                throw new InvalidRuleSessionException(
                    "cannot update object", e );
            }
        }
        else
        {
            throw new InvalidHandleException( "invalid handle" );

        }
    }

    /**
     * Removes a given object from the rule session state of this rule session.
     *
     * @param handleObject the handle to the object to be removed
     *        from the rule session state.
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     * @throws InvalidHandleException if the input <code>Handle</code>
     *         is no longer valid
     */
    public void removeObject( Handle handleObject )
        throws InvalidRuleSessionException, InvalidHandleException
    {
        checkRuleSessionValidity( );

        if ( handleObject instanceof FactHandle )
        {
            try
            {
                getWorkingMemory( ).retractObject( ( FactHandle ) handleObject );
            }
            catch ( FactException e )
            {
                throw new InvalidRuleSessionException(
                    "cannot remove object", e );
            }
        }
        else
        {
            throw new InvalidHandleException( "invalid handle" );
        }
    }

    /**
     * Returns a List of all objects in the rule session state of this rule
     * session. The objects should pass the default filter test of the default
     * <code>RuleExecutionSet</code> filter (if present).
     * <p/>
     * This may not neccessarily include all objects added by calls to
     * <code>addObject</code>, and may include <code>Object</code>s created by
     * side-effects. The execution of a <code>RuleExecutionSet</code> can add,
     * remove and update objects as part of the rule session state. Therefore
     * the rule session state is dependent on the rules that are part of the
     * executed <code>RuleExecutionSet</code> as well as the rule vendor's
     * specific rule engine behavior.
     *
     * @return a <code>List</code> of all objects part of the rule session state.
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    public List getObjects( ) throws InvalidRuleSessionException
    {
        checkRuleSessionValidity( );

        return getObjects( getRuleExecutionSet( ).getObjectFilter( ) );
    }

    /**
     * Returns a <code>List</code> over the objects in rule session state of
     * this rule session. The objects should pass the filter test on the
     * specified <code>ObjectFilter</code>.
     * <p/>
     * This may not neccessarily include all objects added by calls to
     * <code>addObject</code>, and may include <code>Object</code>s created by
     * side-effects. The execution of a <code>RuleExecutionSet</code> can add,
     * remove and update objects as part of the rule session state. Therefore
     * the rule session state is dependent on the rules that are part of the
     * executed <code>RuleExecutionSet</code> as well as the rule vendor's
     * specific rule engine behavior.
     *
     * @param filter the object filter.
     *
     * @return a <code>List</code> of all the objects in the rule session state
     *         of this rule session based upon the given object filter.
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    public List getObjects( ObjectFilter filter )
        throws InvalidRuleSessionException
    {
        checkRuleSessionValidity( );

        List objects = new ArrayList( );

        objects.addAll( getWorkingMemory( ).getObjects( ) );

        applyFilter( objects, filter );

        return objects;
    }

    /**
     * Executes the rules in the bound rule execution set using the objects
     * present in the rule session state. This will typically modify the rule
     * session state - and may add, remove or update <code>Object</code>s bound
     * to <code>Handle</code>s.
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    public void executeRules( ) throws InvalidRuleSessionException
    {
        checkRuleSessionValidity( );

        try
        {
            getWorkingMemory( ).fireAllRules( );
        }
        catch ( DroolsException e )
        {
            throw new InvalidRuleSessionException( "cannot execute rules", e );
        }
    }

    /**
     * @see StatefulRuleSessionImpl
     */
    public Object getObject( Handle handle )
        throws InvalidRuleSessionException, InvalidHandleException
    {
        checkRuleSessionValidity( );

        if ( handle instanceof FactHandle )
        {
            try
            {
                return getWorkingMemory( ).getObject( ( FactHandle ) handle );
            }
            catch ( NoSuchFactObjectException e )
            {
                throw new InvalidHandleException( "invalid handle", e );
            }
        }
        else
        {
            throw new InvalidHandleException( "invalid handle" );
        }
    }

    /**
     * Returns a <code>List</code> of the <code>Handle</code>s
     * being used for object identity.
     *
     * @return a <code>List</code> of <code>Handle</code>s present
     *         in the currect state of the rule session.
     */
    public List getHandles( )
    {
        List handles = new LinkedList( );
        for ( Iterator i = getWorkingMemory( ).getFactHandles( ).iterator( );
              i.hasNext( ); )
        {
            Object object = i.next( );
            if ( object instanceof Handle )
            {
                handles.add( object );
            }
        }
        return handles;
    }
}
