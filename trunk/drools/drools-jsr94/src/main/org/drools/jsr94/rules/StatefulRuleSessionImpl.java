package org.drools.jsr94.rules;

/*
 * $Id: StatefulRuleSessionImpl.java,v 1.13 2004-11-05 20:08:36 dbarnett Exp $
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
 * This interface is a representation of a stateful rules engine session. A
 * stateful rules engine session exposes a stateful rule execution API to an
 * underlying rules engine. The session allows arbitrary objects to be added and
 * removed to and from the working memory of the associated rules engine.
 * Additionally, objects currently in the working memory may be updated, and the
 * working memory itself can be iterated.
 * 
 * @see StatefulRuleSession
 * 
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class StatefulRuleSessionImpl extends RuleSessionImpl implements
                                                            StatefulRuleSession
{
    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /**
     * Gets the <code>RuleExecutionSet</code> for this URI and associated it
     * with a RuleBase.
     * 
     * @param bindUri the URI the <code>RuleExecutionSet</code> has been bound
     *        to
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
     * @see StatefulRuleSessionImpl
     */
    public boolean containsObject( Handle handle )
    {
        if ( handle instanceof FactHandle )
        {
            return getWorkingMemory( ).containsObject( ( FactHandle ) handle );
        }

        return false;
    }

    /**
     * @see StatefulRuleSessionImpl
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
     * @see StatefulRuleSessionImpl
     */
    public List addObjects( List objects ) throws InvalidRuleSessionException
    {
        checkRuleSessionValidity( );

        List handles = new ArrayList( );

        for ( Iterator objectIter = objects.iterator( ); objectIter.hasNext( ); )
        {
            handles.add( addObject( objectIter.next( ) ) );
        }
        return handles;
    }

    /**
     * @see StatefulRuleSessionImpl
     */
    public void updateObject( Handle handle, Object object )
        throws InvalidRuleSessionException, InvalidHandleException
    {
        checkRuleSessionValidity( );

        if ( handle instanceof FactHandle )
        {
            try
            {
                getWorkingMemory( ).modifyObject(
                    ( FactHandle ) handle, object );
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
     * @see StatefulRuleSessionImpl
     */
    public void removeObject( Handle handle )
        throws InvalidRuleSessionException, InvalidHandleException
    {
        checkRuleSessionValidity( );

        if ( handle instanceof FactHandle )
        {
            try
            {
                getWorkingMemory( ).retractObject( ( FactHandle ) handle );
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
     * @see StatefulRuleSessionImpl
     */
    public List getObjects( ) throws InvalidRuleSessionException
    {
        checkRuleSessionValidity( );

        return getObjects( getRuleExecutionSet( ).getObjectFilter( ) );
    }

    /**
     * @see StatefulRuleSessionImpl
     */
    public List getObjects( ObjectFilter objectFilter )
        throws InvalidRuleSessionException
    {
        checkRuleSessionValidity( );

        List objects = new ArrayList( );

        objects.addAll( getWorkingMemory( ).getObjects( ) );

        applyFilter( objects, objectFilter );

        return objects;
    }

    /**
     * @see StatefulRuleSessionImpl
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
