package org.drools.jsr94.rules;

/*
 * $Id: RuleSessionImpl.java,v 1.14 2004-11-15 01:12:22 dbarnett Exp $
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
import javax.rules.RuleExecutionSetMetadata;
import javax.rules.RuleRuntime;
import javax.rules.RuleSession;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleExecutionSet;

import org.drools.WorkingMemory;
import org.drools.jsr94.rules.admin.RuleExecutionSetImpl;
import org.drools.jsr94.rules.admin.RuleExecutionSetRepository;

/**
 * The Drools implementation of the <code>RuleSession</code> interface which is
 * a representation of a client session with a rules engine. A rules engine
 * session serves as an entry point into an underlying rules engine. The
 * <code>RuleSession</code> is bound to a rules engine instance and exposes a
 * vendor-neutral rule processing API for executing <code>Rule</code>s within a
 * bound <code>RuleExecutionSet</code>.
 * <p/>
 * Note: the <code>release</code> method must be called to clean up all
 * resources used by the <code>RuleSession</code>. Calling <code>release</code>
 * may make the <code>RuleSession</code> eligible to be returned to a
 * <code>RuleSession</code> pool.
 *
 * @see RuleSession
 *
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
abstract class RuleSessionImpl implements RuleSession
{
    private WorkingMemory workingMemory;

    private RuleExecutionSetImpl ruleSet;

    private Map properties;

    protected void initWorkingMemory( )
    {
        setWorkingMemory( newWorkingMemory( ) );
    }

    protected WorkingMemory newWorkingMemory( )
    {
        WorkingMemory newWorkingMemory =
            getRuleExecutionSet( ).newWorkingMemory( );

        Map props = this.getProperties( );
        if ( props != null )
        {
            for ( Iterator iterator = props.entrySet( ).iterator( );
                  iterator.hasNext( ); )
            {
                Map.Entry entry = ( Map.Entry ) iterator.next( );
                newWorkingMemory.setApplicationData(
                    ( String ) entry.getKey( ), entry.getValue( ) );
            }
        }

        return newWorkingMemory;
    }

    protected void setProperties( Map properties )
    {
        this.properties = properties;
    }

    protected Map getProperties( )
    {
        return this.properties;
    }

    protected void setWorkingMemory( WorkingMemory workingMemory )
    {
        this.workingMemory = workingMemory;
    }

    protected WorkingMemory getWorkingMemory( )
    {
        return this.workingMemory;
    }

    protected void setRuleExecutionSet( RuleExecutionSetImpl ruleSet )
    {
        this.ruleSet = ruleSet;
    }

    protected RuleExecutionSetImpl getRuleExecutionSet( )
    {
        return this.ruleSet;
    }

    protected void checkRuleSessionValidity( )
        throws InvalidRuleSessionException
    {
        if ( this.workingMemory == null )
        {
            throw new InvalidRuleSessionException( "invalid rule session" );
        }
    }

    protected void applyFilter( List objects, ObjectFilter objectFilter )
    {
        if ( objectFilter != null )
        {
            for ( Iterator objectIter = objects.iterator( );
                  objectIter.hasNext( ); )
            {
                if ( objectFilter.filter( objectIter.next( ) ) == null )
                {
                    objectIter.remove( );
                }
            }
        }
    }

    // JSR94 interface methods start here -------------------------------------

    /**
     * Returns the meta data for the rule execution set bound to this rule
     * session.
     *
     * @return the RuleExecutionSetMetaData bound to this rule session.
     */
    public RuleExecutionSetMetadata getRuleExecutionSetMetadata( )
    {
        RuleExecutionSetRepository repository =
            RuleExecutionSetRepository.getInstance( );

        String theBindUri = null;
        for ( Iterator i = repository.getRegistrations( ).iterator( );
              i.hasNext( ); )
        {
            String aBindUri = ( String ) i.next( );
            RuleExecutionSet aRuleSet =
                repository.getRuleExecutionSet( aBindUri );
            if ( aRuleSet == ruleSet )
            {
                theBindUri = aBindUri;
                break;
            }
        }

        return new RuleExecutionSetMetadataImpl(
            theBindUri, ruleSet.getName( ), ruleSet.getDescription( ) );
    }

    /**
     * Returns the type identifier for this <code>RuleSession</code>. The
     * type identifiers are defined in the <code>RuleRuntime</code> interface.
     *
     * @return the type identifier for this <code>RuleSession</code>
     *
     * @throws InvalidRuleSessionException on illegal rule session state.
     *
     * @see RuleRuntime#STATEFUL_SESSION_TYPE
     * @see RuleRuntime#STATELESS_SESSION_TYPE
     */
    public int getType( ) throws InvalidRuleSessionException
    {
        if ( this instanceof StatelessRuleSession )
        {
            return RuleRuntime.STATELESS_SESSION_TYPE;
        }

        if ( this instanceof StatefulRuleSession )
        {
            return RuleRuntime.STATEFUL_SESSION_TYPE;
        }

        throw new InvalidRuleSessionException( "unknown type" );
    }

    /**
     * Releases all resources used by this rule session.
     * This method renders this rule session unusable until
     * it is reacquired through the <code>RuleRuntime</code>.
     */
    public void release( )
    {
        setProperties( null );
        setWorkingMemory( null );
        setRuleExecutionSet( null );
    }

    /**
     * Resets this rule session. Calling this method will bring the rule session
     * state to its initial state for this rule session and will reset any other
     * state associated with this rule session.
     * <p/>
     * A reset will not reset the state on the default object filter for a
     * <code>RuleExecutionSet</code>.
     */
    public void reset( )
    {
        initWorkingMemory( );
    }
}
