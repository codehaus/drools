package org.drools.reteoo;

/*
 * $Id: WorkingMemoryImpl.java,v 1.42 2004-11-15 23:06:07 mproctor Exp $
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

import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.NoSuchFactHandleException;
import org.drools.NoSuchFactObjectException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.event.WorkingMemoryEventListener;
import org.drools.event.WorkingMemoryEventSupport;
import org.drools.spi.AgendaFilter;
import org.drools.util.IdentityMap;
import org.drools.util.PrimitiveLongMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of <code>WorkingMemory</code>.
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris </a>
 *
 * @version $Id: WorkingMemoryImpl.java,v 1.42 2004-11-15 23:06:07 mproctor Exp $
 */
class WorkingMemoryImpl implements WorkingMemory
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The actual memory for the <code>JoinNode</code>s. */
    private final Map           joinMemories                = new HashMap( );

    /** Application data which is associated with this memory. */
    private final Map           applicationData             = new HashMap( );

    /** Handle-to-object mapping. */
    private final PrimitiveLongMap  objects                     = new PrimitiveLongMap( 32, 8 );

    /** Object-to-handle mapping. */
    private final Map           handles                     = new IdentityMap( );

    /** The eventSupport */
    private final WorkingMemoryEventSupport eventSupport    = new WorkingMemoryEventSupport( this );

    /** The <code>RuleBase</code> with which this memory is associated. */
    private final RuleBaseImpl  ruleBase;

    /** Rule-firing agenda. */
    private final Agenda        agenda;

    /** Flag to determine if a rule is currently being fired. */
    private boolean             firing;

    private long                conditionCounter;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param ruleBase The backing rule-base.
     */
    public WorkingMemoryImpl( RuleBaseImpl ruleBase )
    {
        this.ruleBase = ruleBase;
        this.agenda = new Agenda( this, ruleBase.getConflictResolver( ) );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    public void addEventListener(WorkingMemoryEventListener listener)
    {
        this.eventSupport.addEventListener( listener );
    }

    public void removeEventListener(WorkingMemoryEventListener listener)
    {
        this.eventSupport.removeEventListener( listener );
    }

    public List getEventListeners()
    {
        return eventSupport.getEventListeners( );
    }

    /**
     * Create a new <code>FactHandle</code>.
     *
     * @return The new fact handle.
     */
    FactHandle newFactHandle()
    {
        return this.ruleBase.getFactHandleFactory().newFactHandle( );
    }

    /**
     * @see WorkingMemory
     * @deprecated use form which takes String argument
     */
    public Object getApplicationData()
    {
        return this.applicationData.get( "appData" );
    }

    /**
     * @see WorkingMemory
     * @deprecated use form which takes String argument
     */
    public void setApplicationData(Object appData)
    {
        this.applicationData.put( "appData", appData );
    }

    /**
     * @see WorkingMemory
     */
    public Map getApplicationDataMap()
    {
        return this.applicationData;
    }

    /**
     * @see WorkingMemory
     */
    public void setApplicationData(String name, Object value)
    {
        this.applicationData.put( name, value );
    }

    /**
     * @see WorkingMemory
     */
    public Object getApplicationData(String name)
    {
        return this.applicationData.get( name );
    }

    /**
     * Retrieve the rule-firing <code>Agenda</code> for this
     * <code>WorkingMemory</code>.
     *
     * @return The <code>Agenda</code>.
     */
    protected Agenda getAgenda()
    {
        return this.agenda;
    }

    /**
     * Clear the Agenda
     */
    public void clearAgenda()
    {
        this.agenda.clearAgenda();
    }

    /**
     * @see WorkingMemory
     */
    public RuleBase getRuleBase()
    {
        return this.ruleBase;
    }

    public void fireAllRules(AgendaFilter agendaFilter) throws FactException
    {
        // If we're already firing a rule, then it'll pick up
        // the firing for any other assertObject(..) that get
        // nested inside, avoiding concurrent-modification
        // exceptions, depending on code paths of the actions.

        if ( !this.firing )
        {
            Agenda agenda = getAgenda( );

            try
            {
                this.firing = true;

                while ( !agenda.isEmpty( ) )
                {
                    agenda.fireNextItem( agendaFilter );
                }
            }
            finally
            {
                this.firing = false;
            }
        }
    }

    /**
     * @see WorkingMemory
     */
    public synchronized void fireAllRules() throws FactException
    {
        fireAllRules(null);
    }

    /**
     * @see WorkingMemory
     */
    public Object getObject(FactHandle handle) throws NoSuchFactObjectException
    {
        Object object = this.objects.get( handle.getId() );

        if ( object == null )
        {
            throw new NoSuchFactObjectException( handle );
        }

        return object;
    }

    /**
     * @see WorkingMemory
     */
    public FactHandle getFactHandle(Object object) throws NoSuchFactHandleException
    {
        FactHandle factHandle = ( FactHandle ) this.handles.get( object );

        if ( object == null )
        {
            throw new NoSuchFactHandleException( object );
        }

        return factHandle;
    }

    /**
     * @see WorkingMemory
     */
    public List getObjects()
    {
        return new ArrayList( this.objects.values( ) );
    }

    public List getObjects(Class objectClass)
    {
        List matching = new ArrayList();

        for ( Iterator objIter = this.objects.values().iterator();
              objIter.hasNext(); )
        {
            Object obj = objIter.next();

            if ( objectClass.isInstance( obj ) )
            {
                matching.add( obj );
            }
        }

        return matching;
    }

    /**
     * @see WorkingMemory
     */
    /*
    public List getFactHandles()
    {
        return new ArrayList( this.objects.keySet( ) );
    }
    */
    
    /**
     * @see WorkingMemory
     */
    public boolean containsObject(FactHandle handle)
    {
        return this.objects.containsKey( handle.getId() );
    }

    /**
     * @see WorkingMemory
     */
    public synchronized FactHandle assertObject(Object object) throws FactException
    {
        FactHandle handle = ( FactHandle ) this.handles.get( object );

        if ( handle == null )
        {
            handle = newFactHandle( );

            putObject( handle, object );

            this.ruleBase.assertObject( handle, object, this );

            this.eventSupport.fireObjectAsserted( handle, object );
        }

        return handle;
    }

    /**
     * Associate an object with its handle.
     *
     * @param handle The handle.
     * @param object The object.
     */
    void putObject(FactHandle handle, Object object)
    {
        this.objects.put( handle.getId(), object );

        this.handles.put( object, handle );
    }

    void removeObject(FactHandle handle)
    {
        this.handles.remove( this.objects.remove( handle.getId() ) );
    }    

    /**
     * @see WorkingMemory
     */
    public synchronized void retractObject(FactHandle handle) throws FactException
    {
        this.ruleBase.retractObject( handle, this );

        removeObject(handle);

        this.eventSupport.fireObjectRetracted( handle );
    }

    /**
     * @see WorkingMemory
     */
    public synchronized void modifyObject(FactHandle handle, Object object) throws FactException
    {
        Object original = this.objects.put( handle.getId(), object );

        if ( object == null )
        {
            throw new NoSuchFactObjectException( handle );
        }

        this.handles.remove( original );

        this.handles.put( object, handle );

        this.ruleBase.retractObject( handle, this );

        this.ruleBase.assertObject( handle, object, this );

        this.eventSupport.fireObjectModified( handle, object );
    }

    /**
     * Retrieve the <code>JoinMemory</code> for a particular
     * <code>JoinNode</code>.
     *
     * @param node The <code>JoinNode</code> key.
     *
     * @return The node's memory.
     */
    public JoinMemory getJoinMemory(JoinNode node)
    {
        JoinMemory memory = ( JoinMemory ) this.joinMemories.get( node );

        if ( memory == null )
        {
            memory = new JoinMemory( node.getCommonDeclarations( ) );

            this.joinMemories.put( node, memory );
        }

        return memory;
    }

    public long getConditionTimeStamp()
    {
        return this.conditionCounter++;
    }

    public WorkingMemoryEventSupport getEventSupport()
    {
        return eventSupport;
    }
}
