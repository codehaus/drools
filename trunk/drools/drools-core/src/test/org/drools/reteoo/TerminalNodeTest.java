package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.InstrumentedConsequence;
import org.drools.spi.MockObjectType;

public class TerminalNodeTest extends DroolsTestCase
{
    public void testAssertTuple() throws Exception
    {
        final Agenda[] agendas = new Agenda[1];

        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete() ) )
        {
            public Agenda getAgenda()
            {
                return agendas[0];
            }
        };

        final InstrumentedAgenda agenda = new InstrumentedAgenda(
                                                                  memory,
                                                                  DefaultConflictResolver
                                                                                         .getInstance( ) );
        agendas[0] = agenda;

        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );
        rule.addParameterDeclaration( paramDecl );
        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        TerminalNode node = new TerminalNode( new MockTupleSource( ), rule );

        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        ReteTuple tuple = new ReteTuple( ruleBase.newWorkingMemory( ), rule );

        node.assertTuple( tuple, memory );

        assertLength( 1, agenda.getAdded( ) );

        assertContains( tuple, agenda.getAdded( ) );
    }

    public void testRetractTuples() throws Exception
    {
        final Agenda[] agendas = new Agenda[1];

        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete() ) )
        {
            public Agenda getAgenda()
            {
                return agendas[0];
            }
        };

        final InstrumentedAgenda agenda = new InstrumentedAgenda(memory,
                                                                  DefaultConflictResolver.getInstance( ) );
        agendas[0] = agenda;

        Rule rule = new Rule( "test-rule" );

        InstrumentedConsequence consequence = new InstrumentedConsequence( );

        rule.setConsequence( consequence );

        TerminalNode node = new TerminalNode( new MockTupleSource( ), rule );

        TupleKey key = TupleKey.EMPTY;

        node.retractTuples( key, memory );

        assertLength( 1, agenda.getRemoved( ) );

        assertContains( key, agenda.getRemoved( ) );
    }
}