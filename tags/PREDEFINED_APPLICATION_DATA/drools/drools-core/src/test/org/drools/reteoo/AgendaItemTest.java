package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.InstrumentedConsequence;
import org.drools.spi.MockObjectType;

public class AgendaItemTest extends DroolsTestCase
{
    public void testConstruct() throws Exception
    {
        FactHandleImpl handle = new FactHandleImpl( 1 );

        Rule rule = new Rule( "test-rule" );
        Declaration decl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( null, rule, decl, handle );

        AgendaItem item = new AgendaItem( tuple, rule );

        assertSame( rule, item.getRule( ) );

        assertSame( tuple, item.getTuple( ) );

        assertSame( tuple.getKey( ), item.getKey( ) );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new FactHandleImpl( 2 ) ) );
    }

    public void testSetTuple() throws Exception
    {
        FactHandleImpl handle = new FactHandleImpl( 1 );
        Rule rule = new Rule( "test-rule" );
        Declaration decl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( null, rule, decl, handle );

        AgendaItem item = new AgendaItem( tuple, rule );

        assertSame( tuple, item.getTuple( ) );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new FactHandleImpl( 2 ) ) );

        ReteTuple newTuple = new ReteTuple( null, rule, decl, handle );

        item.setTuple( newTuple );

        assertSame( newTuple, item.getTuple( ) );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new FactHandleImpl( 2 ) ) );
    }

    public void testFire() throws Exception
    {

        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        FactHandleImpl handle = new FactHandleImpl( 1 );

        Rule rule = new Rule( "test-rule" );

        Declaration decl = rule.addParameterDeclaration( "cheese", new MockObjectType( true ) );

        ReteTuple tuple = new ReteTuple( (WorkingMemoryImpl) ruleBase.newWorkingMemory( ), rule, decl, handle );

        InstrumentedConsequence consequence = new InstrumentedConsequence( );

        rule.setConsequence( consequence );

        AgendaItem item = new AgendaItem( tuple, rule );

        item.fire( ( WorkingMemoryImpl ) tuple.getWorkingMemory( ) );

        assertEquals( 1, consequence.getInvokedTuples( ).size( ) );

        assertTrue( consequence.getInvokedTuples( ).contains( tuple ) );
    }
}