package org.drools;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.event.WorkingMemoryEventListener;

public class MockWorkingMemory implements WorkingMemory
{
    private Map appData = new HashMap( );

    /**
     * add event listener to listeners ArrayList
     *
     * @param listener
     */
    public void addEventListener(WorkingMemoryEventListener listener)
    {
        //do nothing
    }

    /**
     * remove event listener from listeners ArrayList
     *
     * @param listener
     */
    public void removeEventListener(WorkingMemoryEventListener listener)
    {
        //do nothing
    }

    /**
     * Returns a read-only list of listeners
     *
     * @return listeners
     */
    public List getListeners()
    {
        return null;
    }

    public Object getApplicationData()
    {
        return appData.get( "appData" );
    }

    /**
     * Retrieve all of the set application data in this memory
     *
     * @return the application data as a Map
     */
    public Map getApplicationDataMap()
    {
        return Collections.unmodifiableMap( appData );
    }

    /**
     * Set a specific piece of application data in this working memory
     *
     * @param name the name under which to populate the data
     * @param value the application data
     */
    public void setApplicationData(String name, Object value)
    {
        appData.put( name, value );
    }

    /**
     * Retrieve a specific piece of application data by name
     *
     * @return application data or null if nothing is set under this name
     */
    public Object getApplicationData(String name)
    {
        return appData.get( name );
    }

    public void setApplicationData(Object appData)
    {
        this.setApplicationData( "appData", appData );
    }

    public RuleBase getRuleBase()
    {
        return null;
    }

    public void fireAllRules() throws FactException
    {
        //
    }

    public Object getObject(FactHandle handle) throws NoSuchFactObjectException
    {
        return null;
    }

    public FactHandle getFactHandle( Object object ) throws NoSuchFactHandleException
    {
        return null;
    }
    
    public List getObjects()
    {
        return null;
    }

    public List getObjects(Class cls)
    {
        return null;
    }

    public List getFactHandles()
    {
        return null;
    }

    public boolean containsObject(FactHandle handle)
    {
        return false;
    }

    public FactHandle assertObject(Object object) throws FactException
    {
        return null;
    }

    public void retractObject(FactHandle handle) throws FactException
    {

    }

    public void modifyObject(FactHandle handle, Object object) throws FactException
    {

    }
}
