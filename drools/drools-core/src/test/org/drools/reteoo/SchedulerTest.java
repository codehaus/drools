package org.drools.reteoo;

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