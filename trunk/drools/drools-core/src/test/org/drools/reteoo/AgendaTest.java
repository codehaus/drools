package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.MockObjectType;
import org.drools.spi.AgendaFilter;

/**
 * 
 * @author mproctor
 *
 * @todo remove schedule items
 * @todo add/remove schedule items
 * 
 */

public class AgendaTest extends DroolsTestCase
{
    public void testAddToAgenda() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete() );

        WorkingMemoryImpl workingMemory = ( WorkingMemoryImpl ) ruleBase.newWorkingMemory();
        final Agenda agenda = workingMemory.getAgenda( );

        Rule rule = new Rule( "test-rule" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );
        rule.addParameterDeclaration( paramDecl );
        //add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple,
                               WorkingMemory workingMemory)
            {
                agenda.addToAgenda( ( ReteTuple ) tuple, tuple.getRule( ) );
            }
        } );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( workingMemory, rule );

        assertEquals( 0, agenda.size( ) );

        /*
         * This is not recursive so a rule should not be able to activate itself
         */
        rule.setNoLoop( true );
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( );
        assertEquals( 0, agenda.size( ) );

        /*
         * This is recursive so a rule should be able to activate itself
         */
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( );
        assertEquals( 1, agenda.size( ) );
    }
    
    public void testFilters() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete() );

        WorkingMemoryImpl workingMemory = ( WorkingMemoryImpl ) ruleBase.newWorkingMemory();
        final Agenda agenda = workingMemory.getAgenda( );

        Rule rule = new Rule( "test-rule" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );
        rule.addParameterDeclaration( paramDecl );
        //add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple,
                               WorkingMemory workingMemory)
            {
                //do nothing
            }
        } );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( workingMemory, rule );

        assertEquals( 0, agenda.size( ) );

        /*
         * Add to agenda
         */
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( );
        assertEquals( 0, agenda.size( ) );

        /*
         * True filter, activations should always add
         */
        AgendaFilter filterTrue = new AgendaFilter( )
        {
            public boolean accept(Activation item)
            {
                return true;
            }
        };
        agenda.setAgendaFilter(filterTrue);
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( );
        assertEquals( 0, agenda.size( ) );     

        /*
         * False filter, activations should always be denied
         */
        AgendaFilter filterFalse = new AgendaFilter( )
        {
            public boolean accept(Activation item)
            {
                return false;
            }
        };
        agenda.setAgendaFilter(filterFalse);
        agenda.addToAgenda( tuple, rule );
        assertEquals( 0, agenda.size( ) );
        agenda.fireNextItem( );
        assertEquals( 0, agenda.size( ) );     
        
        /*
         * Check Filters remove ok
         */        
        agenda.setAgendaFilter(null);
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( );
        assertEquals( 0, agenda.size( ) );   

        /*
         * Check True filter works
         */ 
        agenda.setAgendaFilter(filterTrue);
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );   
        agenda.fireNextItem( );
        assertEquals( 0, agenda.size( ) );          
    }
 
    public void testClearAgenda() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete() );

        WorkingMemoryImpl workingMemory = ( WorkingMemoryImpl ) ruleBase.newWorkingMemory();
        final Agenda agenda = workingMemory.getAgenda( );

        Rule rule = new Rule( "test-rule" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );
        rule.addParameterDeclaration( paramDecl );
        //add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple,
                               WorkingMemory workingMemory)
            {
                agenda.addToAgenda( ( ReteTuple ) tuple, tuple.getRule( ) );
                workingMemory.clearAgenda();
            }
        } );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( workingMemory, rule );

        assertEquals( 0, agenda.size( ) );

        /*
         * This is recursive so a rule should be able to activate itself
         * But then we clear the agenda afterwards to size should still be 0
         */
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( );
        assertEquals( 0, agenda.size( ) );
    }    
    
}