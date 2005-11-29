package org.drools;

/*
 * $Id: MockWorkingMemory.java,v 1.13 2005-11-29 01:21:53 michaelneale Exp $
 *
 * Copyright 2004-2005 (C) The Werken Company. All Rights Reserved.
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.event.WorkingMemoryEventListener;
import org.drools.reteoo.DefaultFactHandleFactory;
import org.drools.reteoo.FactHandleFactory;
import org.drools.spi.AgendaFilter;
import org.drools.spi.AsyncExceptionHandler;

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
    public List getEventListeners()
    {
        return null;
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

   /**
     * Clear the Agenda
     */
    public void clearAgenda()
    {
        //
    }

    public RuleBase getRuleBase()
    {
        return null;
    }

    public void fireAllRules() throws FactException
    {
        //
    }

    public void fireAllRules( AgendaFilter agendaFilter ) throws FactException
    {
        //
    }

    public Object getObject(FactHandle handle) throws NoSuchFactObjectException
    {
        return null;
    }

    public FactHandle getFactHandle(Object object) throws NoSuchFactHandleException
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

    public FactHandle assertObject(Object object, boolean dynamic) throws FactException
    {
        return null;
    }

    public void retractObject(FactHandle handle) throws FactException
    {
        //
    }

    public void modifyObject(FactHandle handle, Object object) throws FactException
    {
        //
    }

    public void setAsyncExceptionHandler(AsyncExceptionHandler handler)
    {
        //
    }

//    public FactHandleFactory getFactHandleFactory()
//    { 
//        return new DefaultFactHandleFactory();
//    }
}
