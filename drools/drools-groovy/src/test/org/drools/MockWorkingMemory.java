package org.drools;

import org.drools.WorkingMemory;
import org.drools.event.WorkingMemoryEventListener;

import java.util.List;


public class MockWorkingMemory
    implements WorkingMemory
{
    private Object appData;

    /**
     * add event listener to listeners ArrayList
     * @param listener
     */
    public void addEventListener(WorkingMemoryEventListener listener) {
        //do nothing
    }    

    /**
     * remove event listener from listeners ArrayList
     * @param listener
     */
    public void removeEventListener(WorkingMemoryEventListener listener) {
      //do nothing
    }

    /**
     * Returns a read-only list of listeners
     * @return listeners
     */
    public List getListeners() {
       return null;
    }      

    public Object getApplicationData()
    {
        return this.appData;
    }

    public void setApplicationData(Object appData)
    {
        this.appData = appData;
    }

    public RuleBase getRuleBase()
    {
        return null;
    }

    public void fireAllRules()
        throws FactException
    {
        //
    }

    public Object getObject(FactHandle handle)
        throws NoSuchFactObjectException
    {
        return null;
    }

    public List getObjects()
    {
        return null;
    }

    public boolean containsObject(FactHandle handle)
    {
        return false;
    }

    public FactHandle assertObject(Object object)
        throws FactException
    {
        return null;
    }

    public void retractObject(FactHandle handle)
        throws FactException
    {

    }

    public void modifyObject(FactHandle handle,
                      Object object)
        throws FactException
    {

    }
}
