package org.drools.reteoo;

import java.util.Iterator;
import java.util.Set;

import org.drools.DroolsTestCase;
import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.TestWorkingMemoryEventListener;
import org.drools.WorkingMemory;
import org.drools.event.ActivationCancelledEvent;
import org.drools.event.ActivationCreatedEvent;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.InstrumentedCondition;
import org.drools.spi.MockObjectType;

/**
 * 
 * @author mproctor
 * 
 * @todo remove schedule items
 * @todo add/remove schedule items
 * 
 */

public class EventTest extends DroolsTestCase
{
    public void testAddToAgenda() throws Exception
    {
        RuleSet ruleSet = new RuleSet( "test rule-set" );
        Rule rule = new Rule( "test-rule" );

        // rule.addParameterDeclaration( intDecl );
        Declaration stringDecl1 = rule.addParameterDeclaration( "stringVar1",
                                                                new MockObjectType( String.class ) );
        Declaration stringDecl2 = rule.addParameterDeclaration( "stringVar2",
                                                                new MockObjectType( String.class ) );
        Declaration intDecl = rule.addParameterDeclaration( "intVar",
                                                            new MockObjectType( Integer.class ) );

        // add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple,
                               WorkingMemory workingMemory)
            {
                // Agenda agenda =
                // ((WorkingMemoryImpl)workingMemory).getAgenda();
                // agenda.addToAgenda( ( ReteTuple ) tuple, tuple.getRule( ) );
                // System.err.println("fire");
            }
        } );
        // add condition
        InstrumentedCondition c1 = new InstrumentedCondition( );
        c1.addDeclaration( stringDecl1 );
        c1.isAllowed( true );
        InstrumentedCondition c2 = new InstrumentedCondition( );
        c2.addDeclaration( stringDecl2 );
        c2.isAllowed( true );
        InstrumentedCondition c3 = new InstrumentedCondition( );
        c3.addDeclaration( intDecl );
        c3.isAllowed( true );
        rule.addCondition( c1 );
        rule.addCondition( c2 );
        rule.addCondition( c3 );

        // rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        ruleSet.addRule( rule );
        Builder builder = new Builder( );
        builder.addRuleSet( ruleSet );
        RuleBase ruleBase = builder.buildRuleBase( );

        WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) ruleBase.newWorkingMemory( );
        TestWorkingMemoryEventListener listener = new TestWorkingMemoryEventListener( );
        workingMemory.addEventListener( listener );

        /*
         * This is not recursive so a rule should not be able to activate itself
         */

        assertEquals( 0,
                      listener.asserted );
        assertEquals( 0,
                      listener.tested );
        FactHandle stringFact1 = workingMemory.assertObject( "cheddar" );
        FactHandle stringFact2 = workingMemory.assertObject( "brie" );
        FactHandle intFact = workingMemory.assertObject( new Integer( 5 ) );
        assertEquals( 3,
                      listener.asserted );
        assertEquals( 5,
                      listener.tested );
        assertEquals( 4,
                      listener.created );
        assertEquals( 4,
                      ((WorkingMemoryImpl) workingMemory).getAgenda( ).size( ) );

        assertEquals( 0,
                      listener.fired );

        workingMemory.fireAllRules( );

        assertEquals( 4,
                      listener.fired );
        assertEquals( 0,
                      ((WorkingMemoryImpl) workingMemory).getAgenda( ).size( ) );

        assertEquals( 0,
                      listener.modified );
        assertEquals( 0,
                      listener.cancelled );

        workingMemory.modifyObject( stringFact1,
                                    "gouda" );

        assertEquals( 3,
                      ((WorkingMemoryImpl) workingMemory).getAgenda( ).size( ) );
        assertEquals( 0,
                      listener.cancelled );
        assertEquals( 1,
                      listener.modified );
        assertEquals( 7,
                      listener.tested );
        assertEquals( 7,
                      listener.created );
        assertEquals( 0,
                      listener.cancelled );
        workingMemory.clearAgenda( );
        assertEquals( 3,
                      listener.cancelled );

        assertEquals( 0,
                      listener.retracted );
        workingMemory.retractObject( stringFact2 );
        assertEquals( 1,
                      listener.retracted );
    }

}