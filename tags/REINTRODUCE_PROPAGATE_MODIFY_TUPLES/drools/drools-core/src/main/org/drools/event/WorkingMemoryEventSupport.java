package org.drools.event;

/*
 * $Id: WorkingMemoryEventSupport.java,v 1.1 2004-11-09 13:52:38 simon Exp $
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

import org.drools.WorkingMemory;
import org.drools.FactHandle;
import org.drools.spi.Condition;
import org.drools.spi.Tuple;
import org.drools.spi.Consequence;
import org.drools.rule.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.io.Serializable;

/**
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris</a>
 */
public class WorkingMemoryEventSupport implements Serializable
{
    private final List          listeners       = new ArrayList();
    private final WorkingMemory workingMemory;

    public WorkingMemoryEventSupport( WorkingMemory workingMemory )
    {
        this.workingMemory = workingMemory;
    }

    public void addEventListener( WorkingMemoryEventListener listener )
    {
        if ( !this.listeners.contains( listener ) )
        {
            this.listeners.add( listener );
        }
    }

    public void removeEventListener( WorkingMemoryEventListener listener )
    {
        this.listeners.remove( listener );
    }

    public List getEventListeners()
    {
        return Collections.unmodifiableList( this.listeners );
    }

    public void fireObjectAsserted( FactHandle handle, Object object )
    {
        if ( this.listeners.isEmpty( ) )
        {
            return;
        }

        ObjectAssertedEvent event = new ObjectAssertedEvent( this.workingMemory, handle, object );

        Iterator iter = this.listeners.iterator( );
        while ( iter.hasNext( ) )
        {
            ( ( WorkingMemoryEventListener ) iter.next( ) ).objectAsserted( event );
        }
    }

    public void fireObjectModified( FactHandle handle, Object object )
    {
        if ( this.listeners.isEmpty() )
        {
            return;
        }

        ObjectModifiedEvent event = new ObjectModifiedEvent( this.workingMemory, handle, object );

        Iterator iter = this.listeners.iterator();
        while ( iter.hasNext() )
        {
            ( ( WorkingMemoryEventListener ) iter.next() ).objectModified( event );
        }
    }

    public void fireObjectRetracted( FactHandle handle )
    {
        if ( this.listeners.isEmpty() )
        {
            return;
        }

        ObjectRetractedEvent event = new ObjectRetractedEvent( this.workingMemory, handle );

        Iterator iter = this.listeners.iterator();
        while ( iter.hasNext() )
        {
            ( ( WorkingMemoryEventListener ) iter.next() ).objectRetracted( event );
        }
    }

    public void fireConditionTested( Rule rule, Condition condition, Tuple tuple, boolean result )
    {
        if ( this.listeners.isEmpty() )
        {
            return;
        }

        ConditionTestedEvent event = new ConditionTestedEvent( this.workingMemory, rule, condition, tuple, result );

        Iterator iter = this.listeners.iterator();
        while ( iter.hasNext() )
        {
            ( ( WorkingMemoryEventListener ) iter.next() ).conditionTested( event );
        }
    }

    public void fireActivationCreated( Consequence consequence, Tuple tuple )
    {
        if ( this.listeners.isEmpty() )
        {
            return;
        }

        ActivationCreatedEvent event = new ActivationCreatedEvent( this.workingMemory, consequence, tuple );

        Iterator iter = this.listeners.iterator();
        while ( iter.hasNext() )
        {
            ( ( WorkingMemoryEventListener ) iter.next() ).activationCreated( event );
        }
    }

    public void fireActivationCancelled( Consequence consequence, Tuple tuple )
    {
        if ( this.listeners.isEmpty() )
        {
            return;
        }

        ActivationCancelledEvent event = new ActivationCancelledEvent( this.workingMemory, consequence, tuple );

        Iterator iter = this.listeners.iterator();
        while ( iter.hasNext() )
        {
            ( ( WorkingMemoryEventListener ) iter.next() ).activationCancelled( event );
        }
    }

    public void fireActivationFired( Consequence consequence, Tuple tuple )
    {
        if ( this.listeners.isEmpty() )
        {
            return;
        }

        ActivationFiredEvent event = new ActivationFiredEvent( this.workingMemory, consequence, tuple );

        Iterator iter = this.listeners.iterator();
        while ( iter.hasNext() )
        {
            ( ( WorkingMemoryEventListener ) iter.next() ).activationFired( event );
        }
    }
}