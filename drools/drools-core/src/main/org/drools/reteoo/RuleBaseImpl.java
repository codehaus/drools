package org.drools.reteoo;

/*
 * $Id: RuleBaseImpl.java,v 1.30 2005-11-29 01:21:53 michaelneale Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.spi.ConflictResolver;
import org.drools.spi.RuleBaseContext;

/**
 * Implementation of <code>RuleBase</code>.
 * 
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * 
 * @version $Id: RuleBaseImpl.java,v 1.30 2005-11-29 01:21:53 michaelneale Exp $
 */
class RuleBaseImpl
    implements
    RuleBase
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The root Rete-OO for this <code>RuleBase</code>. */
    private final Rete rete;    

    /** Conflict resolution strategy. */
    private final ConflictResolver conflictResolver;

    /** The fact handle factory. */
    private final FactHandleFactory factHandleFactory;
    
    private List ruleSets;
    
    private Map applicationData;
    
    private RuleBaseContext ruleBaseContext;
    
    private static ThreadLocal currentWoringMemory = new ThreadLocal();    

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param rete
     *            The rete network.
     */
    RuleBaseImpl(Rete rete)
    {
        this( rete,
              DefaultConflictResolver.getInstance( ),
              new DefaultFactHandleFactory( ),
              null,
              new HashMap( ) ,
              new RuleBaseContext( ) );
    }

    /**
     * Construct.
     * 
     * @param rete
     *            The rete network.
     * @param conflictResolver
     *            The conflict resolver.
     * @param factHandleFactory
     *            The fact handle factory.
     * @param ruleSets
     * @param applicationData
     */
    RuleBaseImpl(Rete rete,
                 ConflictResolver conflictResolver,
                 FactHandleFactory factHandleFactory,
                 List ruleSets,
                 Map applicationData,
                 RuleBaseContext ruleBaseContext)
    {
        this.rete = rete;
        this.factHandleFactory = factHandleFactory;
        this.conflictResolver = conflictResolver;
        this.ruleSets = ruleSets;
        this.applicationData = applicationData;
        this.ruleBaseContext = ruleBaseContext;
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * @see RuleBase
     */
    public WorkingMemory newWorkingMemory()
    {
        return new WorkingMemoryImpl( this );
    }

    /**
     * This will create a new instance, as they should not be shared between working memories.
     * This RuleBase takes in a factory simply to allow implementations to be swapped in and out.
     * And it is really too embarrasing to have a "factory factory".
     * TODO: refactor this differently so that factories are configured more implicitly.
     * @see RuleBase
     */
    public FactHandleFactory newFactHandleFactory()
    {
        return this.factHandleFactory.newInstance();
    }

    /**
     * @see RuleBase
     */
    public ConflictResolver getConflictResolver()
    {
        return this.conflictResolver;
    }

    /**
     * Retrieve the Rete-OO network for this <code>RuleBase</code>.
     * 
     * @return The RETE-OO network.
     */
    Rete getRete()
    {
        return this.rete;
    }

    /**
     * Assert a fact object.
     * 
     * @param handle
     *            The handle.
     * @param object
     *            The fact.
     * @param workingMemory
     *            The working-memory.
     * 
     * @throws FactException
     *             If an error occurs while performing the assertion.
     */
    void assertObject(FactHandle handle,
                      Object object,
                      WorkingMemoryImpl workingMemory) throws FactException
    {
        getRete( ).assertObject( handle,
                                 object,
                                 workingMemory );
    }

    /**
     * Retract a fact object.
     * 
     * @param handle
     *            The handle.
     * @param workingMemory
     *            The working-memory.
     * 
     * @throws FactException
     *             If an error occurs while performing the retraction.
     */
    void retractObject(FactHandle handle,
                       WorkingMemoryImpl workingMemory) throws FactException
    {
        getRete( ).retractObject( handle,
                                  workingMemory );
    }


    public List getRuleSets()
    {
        return this.ruleSets;
    }

    public Map getApplicationData()
    {
        return this.applicationData;
    }    
    
    public RuleBaseContext getRuleBaseContext()
    {
        return this.ruleBaseContext;
    }
    
    public WorkingMemory getCurrentThreadWorkingMemory() {
        Object wm = currentWoringMemory.get();
        if (wm == null) {
            wm = this.newWorkingMemory();
            currentWoringMemory.set(wm);
        }
        return (WorkingMemory) wm;
    }
}
