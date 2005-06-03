package org.drools;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.drools.spi.AgendaFilter;

public interface WorkingMemory extends Serializable
{

    /**
     * Retrieve all of the set application data in this memory
     *
     * @return the application data as a Map
     */
    Map getApplicationDataMap( );

    /**
     * Set a specific piece of application data in this working memory
     *
     * @param name
     *            the name under which to populate the data
     * @param value
     *            the application data
     */
    void setApplicationData( String name,
                             Object value );

    /**
     * Retrieve a specific piece of application data by name
     *
     * @return application data or null if nothing is set under this name
     */
    Object getApplicationData( String name );

    /**
     * Retrieve the <code>RuleBase</code> of this working memory.
     *
     * @return The <code>RuleBase</code>.
     */
    RuleBase getRuleBase( );

    /**
     * Fire all items on the agenda until empty.
     *
     * @throws FactException
     *             If an error occurs.
     */
    void fireAllRules( ) throws FactException;

    /**
     * Fire all items on the agenda until empty, using the given AgendaFiler
     *
     * @throws FactException
     *             If an error occurs.
     */
    void fireAllRules( AgendaFilter agendaFilter ) throws FactException;

    /**
     * Retrieve the object associated with a <code>FactHandle</code>.
     *
     * @see #containsObject
     *
     * @param handle
     *            The fact handle.
     *
     * @return The associated object.
     *
     * @throws NoSuchFactObjectException
     *             If no object is known to be associated with the specified
     *             handle.
     */
    Object getObject( FactHandle handle ) throws NoSuchFactObjectException;

    /**
     * Retrieve the <code>FactHandle</code> associated with an Object.
     *
     * @see #containsObject
     *
     * @param object
     *            The object.
     *
     * @return The associated fact handle.
     *
     * @throws NoSuchFactHandleException
     *             If no handle is known to be associated with the specified
     *             object.
     */
    FactHandle getFactHandle( Object object ) throws NoSuchFactHandleException;

    /**
     * Retrieve all known objects.
     *
     * @return The list of all known objects.
     */
    List getObjects( );

    /**
     * Retrieve all known objects of the specified class.
     *
     * @param objectClass
     *            The class of object to return.
     *
     * @return The list of all known objects of the specified class.
     */
    List getObjects( Class objectClass );

    /**
     * Retrieve all known Fact Handles.
     *
     * @return The list of all known fact handles.
     */
    List getFactHandles( );

    /**
     * Determine if an object is associated with a <code>FactHandle</code>.
     *
     * @param handle
     *            The fact handle.
     *
     * @return <code>true</code> if an object is known to be associated with
     *         the specified handle, otherwise <code>false</code>.
     */
    boolean containsObject( FactHandle handle );

    /**
     * Assert a fact.
     *
     * @param object The fact object.
     *
     * @return The new fact-handle associated with the object.
     *
     * @throws FactException If an error occurs.
     */
    FactHandle assertObject( Object object ) throws FactException;

    /**
     * Assert a fact registering JavaBean <code>PropertyChangeListeners</code>
     * on the Object to automatically trigger <code>modifyObject</code> calls
     * if <code>dynamic</code> is <code>true</code>.
     *
     * @param object The fact object.
     * @param dynamic true if Drools should add JavaBean
     *        <code>PropertyChangeListeners</code> to the object.
     *
     * @return The new fact-handle associated with the object.
     *
     * @throws FactException If an error occurs.
     */
//    FactHandle assertObject( Object object, boolean dynamic ) throws FactException;

    /**
     * Retract a fact.
     *
     * @param handle
     *            The fact-handle associated with the fact to retract.
     *
     * @throws FactException
     *             If an error occurs.
     */
    void retractObject( FactHandle handle ) throws FactException;

    /**
     * Modify a fact.
     *
     * @param handle
     *            The fact-handle associated with the fact to modify.
     * @param object
     *            The new value of the fact.
     *
     * @throws FactException
     *             If an error occurs.
     */
    void modifyObject( FactHandle handle,
                       Object object ) throws FactException;

    /**
     * Sets the AsyncExceptionHandler to handle exceptions thrown by the Agenda Scheduler
     * used for duration rules.
     * @param handler
     */
//duration
//    void setAsyncExceptionHandler(AsyncExceptionHandler handler);

    /**
     * Clear the Agenda
     *
     */
    void clearAgenda( );
}
