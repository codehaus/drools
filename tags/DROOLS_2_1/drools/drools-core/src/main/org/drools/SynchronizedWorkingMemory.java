package org.drools;

/*
 * $Id$
 *
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

import java.util.List;
import java.util.Map;

import org.drools.event.WorkingMemoryEventListener;
import org.drools.spi.AgendaFilter;
import org.drools.spi.AsyncExceptionHandler;

/**
 * Each implemented method of the WorkingMemory interface is synchronised.
 * This class simply delegates each method call to the underlying unsynchronized
 * WorkingMemoryImpl.
 *   
 * @author mproctor
 *
 */
public class SynchronizedWorkingMemory
    implements
    WorkingMemory
{
    final  WorkingMemory workingMemory;
    
    public SynchronizedWorkingMemory(WorkingMemory workingMemory)
    {
        this.workingMemory =workingMemory;
    }

    public synchronized void addEventListener(WorkingMemoryEventListener listener)
    {
        this.workingMemory.addEventListener( listener );
    }

    public synchronized FactHandle assertObject(Object object,
                                   boolean dynamic) throws FactException
    {
        return this.workingMemory.assertObject( object,
                                           dynamic );
    }

    public synchronized FactHandle assertObject(Object object) throws FactException
    {
        return this.workingMemory.assertObject( object );
    }

    public synchronized void clearAgenda()
    {
        this.workingMemory.clearAgenda();
    }

    public synchronized boolean containsObject(FactHandle handle)
    {
        return this.workingMemory.containsObject( handle );
    }

    public synchronized void fireAllRules() throws FactException
    {
        this.workingMemory.fireAllRules();
    }

    public synchronized void fireAllRules(AgendaFilter agendaFilter) throws FactException
    {
        this.workingMemory.fireAllRules( agendaFilter );
    }

    public synchronized Object getApplicationData(String name)
    {
        return this.workingMemory.getApplicationData( name );
    }

    public synchronized Map getApplicationDataMap()
    {
        return this.workingMemory.getApplicationDataMap();
    }

    public synchronized List getEventListeners()
    {
        return this.workingMemory.getEventListeners();
    }

    public synchronized FactHandle getFactHandle(Object object) throws NoSuchFactHandleException
    {
        return this.workingMemory.getFactHandle( object );
    }

    public synchronized List getFactHandles()
    {
        return this.workingMemory.getFactHandles();
    }

    public synchronized Object getObject(FactHandle handle) throws NoSuchFactObjectException
    {
        return this.workingMemory.getObject( handle );
    }

    public synchronized List getObjects()
    {
        return this.workingMemory.getObjects();
    }

    public synchronized List getObjects(Class objectClass)
    {
        return this.workingMemory.getObjects( objectClass );
    }

    public synchronized RuleBase getRuleBase()
    {
        return this.workingMemory.getRuleBase();
    }

    public synchronized void modifyObject(FactHandle handle,
                             Object object) throws FactException
    {
        this.workingMemory.modifyObject( handle,
                                    object );
    }

    public synchronized void removeEventListener(WorkingMemoryEventListener listener)
    {
        this.workingMemory.removeEventListener( listener );
    }

    public synchronized void retractObject(FactHandle handle) throws FactException
    {
        this.workingMemory.retractObject( handle );
    }

    public synchronized void setApplicationData(String name,
                                   Object value)
    {
        this.workingMemory.setApplicationData( name,
                                          value );
    }

    public synchronized void setAsyncExceptionHandler(AsyncExceptionHandler handler)
    {
        this.workingMemory.setAsyncExceptionHandler( handler );
    }
    
    
}
