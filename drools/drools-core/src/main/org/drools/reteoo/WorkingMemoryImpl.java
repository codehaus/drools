package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.NoSuchFactObjectException;
import org.drools.spi.ConflictResolutionStrategy;
import org.drools.conflict.SalienceConflictResolutionStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

class WorkingMemoryImpl
    implements WorkingMemory
{
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

    private Map objects;

    private long handleCounter;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    public WorkingMemoryImpl(RuleBaseImpl ruleBase)
    {
        this( ruleBase,
              SalienceConflictResolutionStrategy.getInstance() );
    }
    
    public WorkingMemoryImpl(RuleBaseImpl ruleBase,
                             ConflictResolutionStrategy conflictResolution)
    {
        this.ruleBase      = ruleBase;
        this.joinMemories  = new HashMap();
        this.objects       = new HashMap();
        this.handleCounter = 0;

        this.agenda = new Agenda( this,
                                  conflictResolution );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    protected FactHandle newFactHandle()
    {
        return new FactHandleImpl( ++this.handleCounter );
    }

    /** Retrieve the application data that is associated with
     *  this memory.
     *
     *  @return The application data or <code>null</code> if
     *  no data has been set for this memory.
     */
    public Object getApplicationData()
    {
        return this.applicationData;
    }

    /** Set the application data associated with this memory.
     *
     *  @param appData The application data for this memory.
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
    public Agenda getAgenda()
    {
        return this.agenda;
    }

    /** Retrieve the <code>RuleBase</code>
     *  of this working memory.
     *
     *  @return The <code>RuleBase</code>.
     */
    public RuleBase getRuleBase()
    {
        return this.ruleBase;
    }

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

    public Object getObject(FactHandle handle)
        throws NoSuchFactObjectException
    {
        if ( ! this.objects.containsKey( handle ) )
        {
            throw new NoSuchFactObjectException( handle );
        }

        return this.objects.get( handle );
    }

    public List getObjects()
    {
        return new ArrayList( this.objects.values() );
    }

    public boolean containsObject(FactHandle handle)
    {
        return this.objects.containsKey( handle );
    }

    public synchronized FactHandle assertObject(Object object)
        throws FactException
    {
        FactHandle handle = newFactHandle();

        this.ruleBase.assertObject( handle,
                                    object,
                                    this );

        this.objects.put( handle,
                          object );

        return handle;
    }

    public synchronized void retractObject(FactHandle handle)
        throws FactException
    {
        Object object = getObject( handle );

        this.ruleBase.retractObject( handle,
                                     object,
                                     this );

        this.objects.remove( handle );
    }

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

    public synchronized Collection getRootFactObjects()
    {
        return new HashSet( this.objects.values() );
    }
}
