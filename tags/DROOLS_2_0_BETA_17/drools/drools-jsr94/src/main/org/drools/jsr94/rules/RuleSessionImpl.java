package org.drools.jsr94.rules;

/*
 $Id: RuleSessionImpl.java,v 1.9 2004-07-06 20:16:40 dbarnett Exp $

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

import org.drools.WorkingMemory;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.jsr94.rules.admin.RuleExecutionSetImpl;
import org.drools.jsr94.rules.admin.RuleExecutionSetRepository;

import javax.rules.*;
import javax.rules.admin.RuleExecutionSet;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

/** This interface is a representation of a client session with a rules engine.
 *
 *  <p>
 *  A rules engine session serves as an entry point into an underlying rules engine.
 *  The <code>RuleSession</code> is bound to a rules engine instance and exposes a vendor-neutral
 *  rule processing API for executing Rule(s) within a bound <code>RuleExecutionSet</code>.
 *  </p>
 *
 * @see RuleSession
 *
 * @author N. Alex Rupp (n_alex <at> codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
abstract class RuleSessionImpl
    implements RuleSession
{
    private WorkingMemory workingMemory;

    private RuleExecutionSetImpl ruleSet;

    private Map properties;

    protected void initWorkingMemory()
    {
        setWorkingMemory( newWorkingMemory() );
    }

    protected WorkingMemory newWorkingMemory()
    {
        WorkingMemory workingMemory = getRuleExecutionSet().newWorkingMemory();

        Map props = this.getProperties();
        if (props != null)
        {
            for (Iterator iterator = props.entrySet().iterator(); iterator.hasNext();)
            {
                Map.Entry entry = (Map.Entry) iterator.next();
                workingMemory.setApplicationData((String) entry.getKey(), entry.getValue());
            }
        }

        workingMemory.addEventListener( new DebugWorkingMemoryEventListener() );

        return workingMemory;
    }

    protected void setProperties(Map properties)
    {
        this.properties = properties;
    }

    protected Map getProperties()
    {
        return this.properties;
    }

    protected void setWorkingMemory(WorkingMemory workingMemory)
    {
        this.workingMemory = workingMemory;
    }

    protected WorkingMemory getWorkingMemory()
    {
        return this.workingMemory;
    }

    protected void setRuleExecutionSet(RuleExecutionSetImpl ruleSet)
    {
        this.ruleSet = ruleSet;
    }

    protected RuleExecutionSetImpl getRuleExecutionSet()
    {
        return this.ruleSet;
    }

    protected void checkRuleSessionValidity()
        throws InvalidRuleSessionException
    {
        if ( this.workingMemory == null )
        {
            throw new InvalidRuleSessionException( "invalid rule session" );
        }
    }

    /**
     * Returns the meta data for the rule execution set bound to this rule session.
     */
    public RuleExecutionSetMetadata getRuleExecutionSetMetadata()
    {
        RuleExecutionSetRepository repository =
            RuleExecutionSetRepository.getInstance();
        
        String theBindUri = null;
        for (Iterator i = repository.getRegistrations().iterator(); i.hasNext(); )
        {
            String aBindUri = (String) i.next();
            RuleExecutionSet aRuleSet = repository.getRuleExecutionSet(aBindUri);
            if (aRuleSet == ruleSet)
            {
                theBindUri = aBindUri;
                break;
            }
        }

        return new RuleExecutionSetMetadataImpl(
            theBindUri,
            ruleSet.getName(),
            ruleSet.getDescription());
    }

    /**
     * Returns the type identifier for this RuleSession.
     * The type identifiers are defined in the RuleRuntime interface.
     */
    public int getType() throws InvalidRuleSessionException
    {

        if ( this instanceof StatelessRuleSession )
            return RuleRuntime.STATELESS_SESSION_TYPE;

        if ( this instanceof StatefulRuleSession )
            return RuleRuntime.STATEFUL_SESSION_TYPE;

        throw new InvalidRuleSessionException( "unknown type" );
    }

    public void reset()
    {
        initWorkingMemory();
    }

    public void release()
    {
        setProperties( null );
        setWorkingMemory( null );
        setRuleExecutionSet( null );
    }

    protected void applyFilter(List objects,
                               ObjectFilter objectFilter)
    {
        if ( objectFilter != null )
        {
            for ( Iterator objectIter = objects.iterator();
                  objectIter.hasNext(); )
            {
                if ( objectFilter.filter( objectIter.next() ) == null )
                {
                    objectIter.remove();
                }
            }
        }
    }
}
