package org.drools.reteoo;
/*
 * $Id: SchedulerTest.java,v 1.2 2005-01-11 15:13:56 mproctor Exp $
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

import java.util.HashMap;
import java.util.Map;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.rule.FixedDuration;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.AgendaFilter;
import org.drools.spi.AsyncExceptionHandler;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Duration;
import org.drools.spi.MockObjectType;

/**
 * @author mproctor
 */

public class SchedulerTest extends DroolsTestCase
{
    public void testScheduledActivation() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        WorkingMemoryImpl workingMemory = ( WorkingMemoryImpl ) ruleBase.newWorkingMemory();
        final Scheduler scheduler = Scheduler.getInstance();

        final Rule rule = new Rule( "test-rule" );
        final Map data = new HashMap();

        rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple, WorkingMemory workingMemory )
            {
                data.put("tested", "yes");
            }
        } );

        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        
        rule.setDuration(new FixedDuration(1));

        ReteTuple tuple = new ReteTuple( workingMemory );
        
        AgendaItem item = new AgendaItem(tuple, rule);
        
        assertNull(data.get("tested"));
        
        //Schedule is for 1 second
        scheduler.scheduleAgendaItem(item, workingMemory);
        
        //sleep for 2 seconds
        Thread.sleep( 2000 );       
        
        //now check for update
        assertEquals("yes", data.get("tested"));
    }

    public void testExceptionHandler() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        WorkingMemoryImpl workingMemory = ( WorkingMemoryImpl ) ruleBase.newWorkingMemory();
        final Scheduler scheduler = Scheduler.getInstance();

        final Rule rule = new Rule( "test-rule" );

        rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple, WorkingMemory workingMemory ) throws ConsequenceException
            {
                throw new ConsequenceException("not enough cheese", rule);
            }
        } );

        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        
        rule.setDuration(new FixedDuration(1));

        ReteTuple tuple = new ReteTuple( workingMemory );
        
        AgendaItem item = new AgendaItem(tuple, rule);

        final Map data = new HashMap();
        
        AsyncExceptionHandler handler = new AsyncExceptionHandler()
        {
            public void handleException(ConsequenceException exception, WorkingMemory workingMemory)
            {
                data.put("tested", "yes");
            }
        };
        workingMemory.setAsyncExceptionHandler(handler);        
        
        assertNull(data.get("tested"));
        
        //Schedule is for 1 second
        scheduler.scheduleAgendaItem(item, workingMemory);        
        
        //sleep for 2 seconds
        Thread.sleep( 2000 );       
        
        //now check for update
        assertEquals("yes", data.get("tested"));
    }    
    
}
