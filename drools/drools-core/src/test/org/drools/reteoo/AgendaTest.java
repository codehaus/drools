package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.AgendaFilter;
import org.drools.spi.MockObjectType;

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

        Declaration paramDecl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple, WorkingMemory workingMemory )
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
        agenda.fireNextItem( null );
        assertEquals( 0, agenda.size( ) );

        /*
         * This is recursive so a rule should be able to activate itself
         */
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( null );
        assertEquals( 1, agenda.size( ) );
        agenda.clearAgenda();
    }

    public void testFilters() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete() );

        WorkingMemoryImpl workingMemory = ( WorkingMemoryImpl ) ruleBase.newWorkingMemory();
        final Agenda agenda = workingMemory.getAgenda( );

        Rule rule = new Rule( "test-rule" );
        Declaration paramDecl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

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
         * Add to agenda
         */
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( null );
        assertEquals( 1, agenda.size( ) );
        agenda.clearAgenda();

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
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( filterTrue );
        assertEquals( 1, agenda.size( ) );
        agenda.clearAgenda();

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
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.size( ) );
        agenda.fireNextItem( filterFalse );
        assertEquals( 0, agenda.size( ) );
    }

    public void testClearAgenda() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete() );

        WorkingMemoryImpl workingMemory = ( WorkingMemoryImpl ) ruleBase.newWorkingMemory();
        final Agenda agenda = workingMemory.getAgenda( );

        Rule rule = new Rule( "test-rule" );
        Declaration paramDecl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

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
        agenda.fireNextItem( null );
        assertEquals( 0, agenda.size( ) );
    }

}