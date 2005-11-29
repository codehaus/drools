package org.drools.reteoo;

/*
 * Copyright 2001-2004 (C) The Werken Company. All Rights Reserved.
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.NoSuchFactHandleException;
import org.drools.NoSuchFactObjectException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.event.WorkingMemoryEventListener;
import org.drools.event.WorkingMemoryEventSupport;
import org.drools.spi.AgendaFilter;
import org.drools.spi.AsyncExceptionHandler;
import org.drools.util.IdentityMap;
import org.drools.util.PrimitiveLongMap;
import org.drools.util.PrimitiveLongStack;

/**
 * Implementation of <code>WorkingMemory</code>.
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris </a>
 */
class WorkingMemoryImpl
    implements
    WorkingMemory,
    PropertyChangeListener
{
    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------
    private static final Class[]            ADD_REMOVE_PROPERTY_CHANGE_LISTENER_ARG_TYPES = new Class[]{PropertyChangeListener.class};

    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The arguments used when adding/removing a property change listener. */
    private final Object[]                  addRemovePropertyChangeListenerArgs           = new Object[]{this};

    /** The actual memory for the <code>JoinNode</code>s. */
    private final Map                       joinMemories                                  = new HashMap( );

    /** Application data which is associated with this memory. */
    private final Map                       applicationData                               = new HashMap( );

    /** Handle-to-object mapping. */
    private final PrimitiveLongMap          objects                                       = new PrimitiveLongMap( 32,
                                                                                                                  8 );

    /** Object-to-handle mapping. */
    private final Map                       handles                                       = new IdentityMap( );
    private final PrimitiveLongStack        factHandlePool                                = new PrimitiveLongStack( );

    /** The eventSupport */
    private final WorkingMemoryEventSupport eventSupport                                  = new WorkingMemoryEventSupport( this );

    /** The <code>RuleBase</code> with which this memory is associated. */
    private final RuleBaseImpl              ruleBase;

    /** Rule-firing agenda. */
    private final Agenda                    agenda;

    /** Flag to determine if a rule is currently being fired. */
    private boolean                         firing;
    
    private FactHandleFactory               factHandleFactory;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param ruleBase
     *            The backing rule-base.
     */
    public WorkingMemoryImpl(RuleBaseImpl ruleBase)
    {
        this.ruleBase = ruleBase;
        this.agenda = new Agenda( this,
                                  ruleBase.getConflictResolver( ) );
        this.factHandleFactory = ruleBase.newFactHandleFactory();
    }

    // ------------------------------------------------------------
    // Instance methods
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
        if ( !this.factHandlePool.isEmpty( ) )
        {
            return factHandleFactory.newFactHandle( this.factHandlePool.pop( ) );
        }
        else
        {
            return factHandleFactory.newFactHandle( );
        }
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
    public void setApplicationData(String name,
                                   Object value)
    {
        // Make sure the application data has been declared in the RuleBase
        Map applicationDataDefintions = this.ruleBase.getApplicationData( );
        Class type = (Class) applicationDataDefintions.get( name );
        if ( ( type == null ) )
        {
            throw new RuntimeException( "Unexpected application data [" + name + "]" );
        }
        else if  ( !type.isInstance( value ) )
        {
            throw new RuntimeException( "Illegal class for application data. " +
                                        "Expected [" + type.getName() + "], " +
                                        "found [" + value.getClass().getName() + "]." );

        }
        else
        {
          this.applicationData.put( name,
                                    value );
        }
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
        this.agenda.clearAgenda( );
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

        if ( !firing )
        {
            try
            {
                firing = true;

                while ( !agenda.isEmpty( ) )
                {
                    agenda.fireNextItem( agendaFilter );
                }
            }
            finally
            {
                firing = false;
            }
        }
    }

    /**
     * @see WorkingMemory
     */
    public void fireAllRules() throws FactException
    {
        fireAllRules( null );
    }

    /**
     * @see WorkingMemory
     */
    public Object getObject(FactHandle handle) throws NoSuchFactObjectException
    {
        Object object = this.objects.get( ((FactHandleImpl) handle).getId( ) );

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
        FactHandle factHandle = (FactHandle) this.handles.get( object );

        if ( factHandle == null )
        {
            throw new NoSuchFactHandleException( object );
        }

        return factHandle;
    }

    public List getFactHandles()
    {
        return new ArrayList( this.handles.values( ) );
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
        List matching = new LinkedList( );
        Iterator objIter = this.objects.values( ).iterator( );
        Object obj;
        while ( objIter.hasNext( ) )
        {
            obj = objIter.next( );

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
    public boolean containsObject(FactHandle handle)
    {
        return this.objects.containsKey( ((FactHandleImpl) handle).getId( ) );
    }

    /**
     * @see WorkingMemory
     */
    public FactHandle assertObject(Object object) throws FactException
    {
        return assertObject( object, /* Not-Dynamic */
                             false );
    }

    public FactHandle assertObject(Object object,
                                   boolean dynamic) throws FactException
    {
        FactHandle handle = (FactHandle) handles.get( object );

        if ( handle != null )
        {
            return handle;
        }

        handle = newFactHandle( );

        putObject( handle,
                   object );

        if ( dynamic )
        {
            addPropertyChangeListener( object );
        }

        this.agenda.setMode( Agenda.ASSERT );
        ruleBase.assertObject( handle,
                               object,
                               this );

        eventSupport.fireObjectAsserted( handle,
                                         object );
        this.agenda.setMode( Agenda.NONE );
        return handle;
    }

    private void addPropertyChangeListener(Object object)
    {
        try
        {
            Method method = object.getClass( ).getMethod( "addPropertyChangeListener",
                                                          ADD_REMOVE_PROPERTY_CHANGE_LISTENER_ARG_TYPES );

            method.invoke( object,
                           addRemovePropertyChangeListenerArgs );
        }
        catch ( NoSuchMethodException e )
        {
            System.err.println( "Warning: Method addPropertyChangeListener not found"
                                + " on the class " + object.getClass( )
                                + " so Drools will be unable to process JavaBean"
                                + " PropertyChangeEvents on the asserted Object" );
        }
        catch ( IllegalArgumentException e )
        {
            System.err.println( "Warning: The addPropertyChangeListener method" + " on the class " + object.getClass( ) + " does not take" + " a simple PropertyChangeListener argument" + " so Drools will be unable to process JavaBean"
                                + " PropertyChangeEvents on the asserted Object" );
        }
        catch ( IllegalAccessException e )
        {
            System.err.println( "Warning: The addPropertyChangeListener method" + " on the class " + object.getClass( ) + " is not public" + " so Drools will be unable to process JavaBean" + " PropertyChangeEvents on the asserted Object" );
        }
        catch ( InvocationTargetException e )
        {
            System.err.println( "Warning: The addPropertyChangeListener method" + " on the class " + object.getClass( ) + " threw an InvocationTargetException" + " so Drools will be unable to process JavaBean"
                                + " PropertyChangeEvents on the asserted Object: " + e.getMessage( ) );
        }
        catch ( SecurityException e )
        {
            System.err.println( "Warning: The SecurityManager controlling the class " + object.getClass( ) + " did not allow the lookup of a" + " addPropertyChangeListener method" + " so Drools will be unable to process JavaBean"
                                + " PropertyChangeEvents on the asserted Object: " + e.getMessage( ) );
        }
    }

    private void removePropertyChangeListener(FactHandle handle) throws NoSuchFactObjectException
    {
        Object object = null;
        try
        {
            object = getObject( handle );

            Method mehod = handle.getClass( ).getMethod( "removePropertyChangeListener",
                                                         ADD_REMOVE_PROPERTY_CHANGE_LISTENER_ARG_TYPES );

            mehod.invoke( handle,
                          addRemovePropertyChangeListenerArgs );
        }
        catch ( NoSuchMethodException e )
        {
            // The removePropertyChangeListener method on the class
            // was not found so Drools will be unable to
            // stop processing JavaBean PropertyChangeEvents
            // on the retracted Object
        }
        catch ( IllegalArgumentException e )
        {
            System.err.println( "Warning: The removePropertyChangeListener method" + " on the class " + object.getClass( ) + " does not take" + " a simple PropertyChangeListener argument" + " so Drools will be unable to stop processing JavaBean"
                                + " PropertyChangeEvents on the retracted Object" );
        }
        catch ( IllegalAccessException e )
        {
            System.err.println( "Warning: The removePropertyChangeListener method" + " on the class " + object.getClass( ) + " is not public" + " so Drools will be unable to stop processing JavaBean" + " PropertyChangeEvents on the retracted Object" );
        }
        catch ( InvocationTargetException e )
        {
            System.err.println( "Warning: The removePropertyChangeL istener method" + " on the class " + object.getClass( ) + " threw an InvocationTargetException" + " so Drools will be unable to stop processing JavaBean"
                                + " PropertyChangeEvents on the retracted Object: " + e.getMessage( ) );
        }
        catch ( SecurityException e )
        {
            System.err.println( "Warning: The SecurityManager controlling the class " + object.getClass( ) + " did not allow the lookup of a" + " removePropertyChangeListener method" + " so Drools will be unable to stop processing JavaBean"
                                + " PropertyChangeEvents on the retracted Object: " + e.getMessage( ) );
        }
    }

    /**
     * Associate an object with its handle.
     *
     * @param handle
     *            The handle.
     * @param object
     *            The object.
     */
    Object putObject(FactHandle handle,
                     Object object)
    {
        Object oldValue = this.objects.put( ((FactHandleImpl) handle).getId( ),
                                            object );

        this.handles.put( object,
                          handle );

        return oldValue;
    }

    Object removeObject(FactHandle handle)
    {
        Object object = this.objects.remove( ((FactHandleImpl) handle).getId( ) );

        this.handles.remove( object );

        return object;
    }

    /**
     * @see WorkingMemory
     */
    public void retractObject(FactHandle handle) throws FactException
    {
        removePropertyChangeListener( handle );

        this.agenda.setMode( Agenda.RETRACT );

        ruleBase.retractObject( handle,
                                this );

        Object oldObject = removeObject( handle );

        factHandlePool.push( ((FactHandleImpl) handle).getId( ) );

        eventSupport.fireObjectRetracted( handle,
                                          oldObject );
        this.agenda.setMode( Agenda.NONE );

        ((FactHandleImpl) handle).invalidate( );
    }

    /**
     * @see WorkingMemory
     */
    public void modifyObject(FactHandle handle,
                                          Object object) throws FactException
    {
        Object originalObject = removeObject( handle );

        if ( originalObject == null )
        {
            throw new NoSuchFactObjectException( handle );
        }

        putObject( handle,
                   object );

        this.agenda.setMode( Agenda.MODIFY );

        this.ruleBase.retractObject( handle,
                                     this );

        this.ruleBase.assertObject( handle,
                                    object,
                                    this );

        this.agenda.removeMarkedItemsFromAgenda( );

        this.agenda.setMode( Agenda.NONE );

        /*
         * this.ruleBase.modifyObject( handle, object, this );
         */
        this.eventSupport.fireObjectModified( handle,
                                              originalObject,
                                              object );
    }

    /**
     * Retrieve the <code>JoinMemory</code> for a particular
     * <code>JoinNode</code>.
     *
     * @param node
     *            The <code>JoinNode</code> key.
     *
     * @return The node's memory.
     */
    public JoinMemory getJoinMemory(JoinNode node)
    {
        JoinMemory memory = (JoinMemory) this.joinMemories.get( node );

        if ( memory == null )
        {
            memory = new JoinMemory( node.getTupleDeclarations( ),
                                     node.getCommonDeclarations( ) );

            this.joinMemories.put( node,
                                   memory );
        }

        return memory;
    }

    public WorkingMemoryEventSupport getEventSupport()
    {
        return eventSupport;
    }

    /**
     * Sets the AsyncExceptionHandler to handle exceptions thrown by the Agenda
     * Scheduler used for duration rules.
     *
     * @param handler
     */
    public void setAsyncExceptionHandler(AsyncExceptionHandler handler)
    {
        this.agenda.setAsyncExceptionHandler( handler );
    }

    public void dumpMemory()
    {
        Iterator it = this.joinMemories.keySet( ).iterator( );
        while ( it.hasNext( ) )
        {
            ((JoinMemory) this.joinMemories.get( it.next( ) )).dump( );
        }

    }

    public void propertyChange(PropertyChangeEvent event)
    {
        Object object = event.getSource( );

        try
        {
            modifyObject( getFactHandle( object ),
                          object );
        }
        catch ( NoSuchFactHandleException e )
        {
            // Not a fact so unable to process the chnage event
        }
        catch ( FactException e )
        {
            throw new RuntimeException( e.getMessage( ) );
        }
    }

    public FactHandleFactory getFactHandleFactory()
    {
        
        return factHandleFactory;
    }
    
}