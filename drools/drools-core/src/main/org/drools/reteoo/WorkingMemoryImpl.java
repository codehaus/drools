package org.drools.reteoo;

/*
 $Id: WorkingMemoryImpl.java,v 1.11 2004-03-30 21:13:19 n_alex Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a trademark of 
    The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werken.com/)
 
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

import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.NoSuchFactObjectException;
import org.drools.spi.ConflictResolver;
import org.drools.conflict.SalienceConflictResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/** Implementation of <code>WorkingMemory</code>.
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *
 *  @version $Id: WorkingMemoryImpl.java,v 1.11 2004-03-30 21:13:19 n_alex Exp $
 */
class WorkingMemoryImpl
    implements WorkingMemory
{
    private static final String JSR_FACT_HANDLE_FACTORY_NAME = "org.drools.jsr94.rules.Jsr94FactHandleFactory";

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The <code>RuleBase</code> with which this memory is associated. */
    private RuleBaseImpl ruleBase;

    /** The actual memory for the <code>JoinNode</code>s. */
    private Map joinMemories;

    /** Rule-firing agenda. */
    private Agenda agenda;

    /** Flag to determine if a rule is currently being fired. */
    private boolean firing;

    /** Application data which is associated with this memory. */
    private Object applicationData;

    /** Handle-to-object mapping. */
    private Map objects;

    private FactHandleFactory factHandleFactory;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param ruleBase The backing rule-base.
     */
    public WorkingMemoryImpl(RuleBaseImpl ruleBase)
    {
        this( ruleBase,
              SalienceConflictResolver.getInstance() );
    }
    
    /** Construct.
     *
     *  @param ruleBase The backing rule-base.
     *  @param conflictResolver The conflict resolver.
     */
    public WorkingMemoryImpl(RuleBaseImpl ruleBase,
                             ConflictResolver conflictResolver)
    {
        this.ruleBase      = ruleBase;
        this.joinMemories  = new HashMap();
        this.objects       = new HashMap();

        this.agenda = new Agenda( this,
                                  conflictResolver );

        initializeFactHandleFactory();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    protected void initializeFactHandleFactory()
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        Class jsrHandleFactoryClass = null;

        try
        {
            jsrHandleFactoryClass = cl.loadClass( JSR_FACT_HANDLE_FACTORY_NAME );
        }
        catch (ClassNotFoundException e)
        {
            cl = getClass().getClassLoader();

            try
            {
                jsrHandleFactoryClass = cl.loadClass( JSR_FACT_HANDLE_FACTORY_NAME );
            }
            catch (ClassNotFoundException e2)
            {
                // swallow
            }
        }

        try
        {
            this.factHandleFactory = (FactHandleFactory) jsrHandleFactoryClass.newInstance();
        }
        catch (Exception e2)
        {
            this.factHandleFactory = new DefaultFactHandleFactory();
        }
    }

    /** Create a new <code>FactHandle</code>.
     *
     *  @return The new fact handle.
     */
    protected FactHandle newFactHandle()
    {
        return this.factHandleFactory.newFactHandle();
    }

    /** @see WorkingMemory
     */
    public Object getApplicationData()
    {
        return this.applicationData;
    }

    /** @see WorkingMemory
     */
    public void setApplicationData(Object appData)
    {
        this.applicationData = appData;
    }

    /** Retrieve the rule-firing <code>Agenda</code> for
     *  this <code>WorkingMemory</code>.
     *
     *  @return The <code>Agenda</code>.
     */
    protected Agenda getAgenda()
    {
        return this.agenda;
    }

    /** @see WorkingMemory
     */
    public RuleBase getRuleBase()
    {
        return this.ruleBase;
    }

    /** @see WorkingMemory
     */
    public synchronized void fireAllRules()
        throws FactException
    {
        // If we're already firing a rule, then it'll pick up
        // the firing for any other assertObject(..) that get
        // nested inside, avoiding concurrent-modification
        // exceptions, depending on code paths of the actions.

        if ( ! this.firing )
        {
            Agenda agenda = getAgenda();

            try
            {
                this.firing = true;

                while ( ! agenda.isEmpty() )
                {
                    agenda.fireNextItem();
                }
            }
            finally
            {
                this.firing = false;
            }
        }
    }

    /** @see WorkingMemory
     */
    public Object getObject(FactHandle handle)
        throws NoSuchFactObjectException
    {
        if ( ! this.objects.containsKey( handle ) )
        {
            throw new NoSuchFactObjectException( handle );
        }

        return this.objects.get( handle );
    }

    /** @see WorkingMemory
     */
    public List getObjects()
    {
        return new ArrayList( this.objects.values() );
    }

    /** @see WorkingMemory
     */
    public boolean containsObject(FactHandle handle)
    {
        return this.objects.containsKey( handle );
    }

    /** @see WorkingMemory
     */
    public synchronized FactHandle assertObject(Object object)
        throws FactException
    {
        FactHandle handle = newFactHandle();

        this.ruleBase.assertObject( handle,
                                    object,
                                    this );

        putObject( handle,
                   object );

        return handle;
    }

    /** Associate an object with its handle.
     *
     *  @param handle The handle.
     *  @param object The object.
     */
    void putObject(FactHandle handle,
                   Object object)
    {
        this.objects.put( handle,
                          object );
    }

    /** @see WorkingMemory
     */
    public synchronized void retractObject(FactHandle handle)
        throws FactException
    {
        this.ruleBase.retractObject( handle,
                                     this );

        this.objects.remove( handle );
    }

    /** @see WorkingMemory
     */
    public synchronized void modifyObject(FactHandle handle,
                                          Object object)
        throws FactException
    {
        if ( ! containsObject( handle ) )
        {
            throw new NoSuchFactObjectException( handle );
        }

        this.ruleBase.modifyObject( handle,
                                    object,
                                    this );

        this.objects.put( handle,
                          object );
    }

    /** Retrieve the <code>JoinMemory</code> for a particular <code>JoinNode</code>.
     *
     *  @param node The <code>JoinNode</code> key.
     *
     *  @return The node's memory.
     */
    public JoinMemory getJoinMemory(JoinNode node)
    {
        JoinMemory memory = (JoinMemory) this.joinMemories.get( node );

        if ( memory == null )
        {
            memory = new JoinMemory( node );

            this.joinMemories.put( node,
                                   memory );
        }

        return memory;
    }
}
