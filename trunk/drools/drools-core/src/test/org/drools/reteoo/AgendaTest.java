package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.MockObjectType;
import org.drools.WorkingMemory;

public class AgendaTest extends DroolsTestCase
{
    public void testAddToAgenda() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl(
                                              new Rete( ),
                                              DefaultConflictResolver
                                                                     .getInstance( ) );

        WorkingMemoryImpl workingMemory = ( WorkingMemoryImpl ) ruleBase
                                                                        .newWorkingMemory( );
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

        assertEquals( 0, agenda.getItems( ).size( ) );

        /*
         * This is not recursive so a rule should not be able to activate itself
         */
        rule.setNoLoop( true );
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.getItems( ).size( ) );
        agenda.fireNextItem( );
        assertEquals( 0, agenda.getItems( ).size( ) );

        /*
         * This is recursive so a rule should be able to activate itself
         */
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple, rule );
        assertEquals( 1, agenda.getItems( ).size( ) );
        agenda.fireNextItem( );
        assertEquals( 1, agenda.getItems( ).size( ) );

    }
}