package org.drools.reteoo;

import org.drools.MockFactHandle;
import org.drools.AssertionException;
import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.InstrumentedConsequence;
import org.drools.spi.MockObjectType;
import org.drools.DroolsTestCase;

import java.util.List;

public class TerminalNodeTest
    extends DroolsTestCase
{
    public void testAssertTuple()
        throws Exception
    {
        final InstrumentedAgenda agenda = new InstrumentedAgenda( null,
                                                                  DefaultConflictResolver.getInstance() );

        WorkingMemoryImpl memory = new WorkingMemoryImpl( null )
            {
                public Agenda getAgenda()
                {
                    return agenda;
                }
            };



        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = new Declaration( new MockObjectType( true ),
        "paramVar" );                                                 
        rule.addParameterDeclaration( paramDecl );
        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence() );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition() );        

        TerminalNode node = new TerminalNode( new MockTupleSource(),
                                              rule );

    	RuleBase ruleBase = new RuleBaseImpl( new Rete(), new DefaultConflictResolver());
        
        ReteTuple tuple = new ReteTuple(ruleBase.newWorkingMemory(), rule);

        node.assertTuple( tuple,
                          memory );

        assertLength( 1,
                      agenda.getAdded() );

        assertContains( tuple,
                        agenda.getAdded() );
    }

    public void testRetractTuples()
        throws Exception
    {
        final InstrumentedAgenda agenda = new InstrumentedAgenda( null,
                                                                  DefaultConflictResolver.getInstance() );

        WorkingMemoryImpl memory = new WorkingMemoryImpl( null )
            {
                public Agenda getAgenda()
                {
                    return agenda;
                }
            };



        Rule rule = new Rule( "test-rule" );

        InstrumentedConsequence consequence = new InstrumentedConsequence();

        rule.setConsequence( consequence );

        TerminalNode node = new TerminalNode( new MockTupleSource(),
                                              rule );

        TupleKey key = new TupleKey();

        node.retractTuples( key,
                            memory );

        assertLength( 1,
                      agenda.getRemoved() );

        assertContains( key,
                        agenda.getRemoved() );
    }

    public void testModifyTuples()
        throws Exception
    {
        final InstrumentedAgenda agenda = new InstrumentedAgenda( null,
                                                                  DefaultConflictResolver.getInstance() );

        WorkingMemoryImpl memory = new WorkingMemoryImpl( null )
            {
                public Agenda getAgenda()
                {
                    return agenda;
                }
            };



        Rule rule = new Rule( "test-rule" );

        InstrumentedConsequence consequence = new InstrumentedConsequence();

        rule.setConsequence( consequence );

        TerminalNode node = new TerminalNode( new MockTupleSource(),
                                              rule );

        MockFactHandle handle = new MockFactHandle( 42 );

        node.modifyTuples( handle,
                           new TupleSet(),
                           memory );

        assertLength( 1,
                      agenda.getModified() );

        assertContains( handle,
                        agenda.getModified() );
    }
}
