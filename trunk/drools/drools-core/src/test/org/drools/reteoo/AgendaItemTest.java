package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.MockFactHandle;
import org.drools.rule.Rule;
import org.drools.rule.Declaration;
import org.drools.spi.InstrumentedConsequence;
import org.drools.spi.MockObjectType;
import org.drools.rule.RuleSet;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.RuleBase;

public class AgendaItemTest
    extends DroolsTestCase
{
    public void testConstruct()
        throws Exception
    {
        Declaration decl = new Declaration( new MockObjectType(true),
                                            "cheese" );

        MockFactHandle handle = new MockFactHandle( 1 );

        Rule rule = new Rule( "test-rule" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );                                                 
        rule.addParameterDeclaration( paramDecl );
        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence() );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition() );         

        ReteTuple tuple = new ReteTuple( null,
                                         rule,
                                         decl,
                                         handle,
                                         new Object() );

        AgendaItem item = new AgendaItem( tuple,
                                          rule );

        assertSame( rule,
                    item.getRule() );

        assertSame( tuple,
                    item.getTuple() );

        assertSame( tuple.getKey(),
                    item.getKey() );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new MockFactHandle( 2 ) ) );
    }

    public void testSetTuple()
        throws Exception
    {
        Declaration decl = new Declaration( new MockObjectType(true),
                                            "cheese" );

        MockFactHandle handle = new MockFactHandle( 1 );
        Rule rule = new Rule( "test-rule" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );                                                 
        rule.addParameterDeclaration( paramDecl );
        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence() );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition() ); 
        
        ReteTuple tuple = new ReteTuple( null,
                                         rule,
                                         decl,
                                         handle,
                                         new Object() );

        AgendaItem item = new AgendaItem( tuple,
                                          rule );

        assertSame( tuple,
                    item.getTuple() );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new MockFactHandle( 2 ) ) );      
        
        ReteTuple newTuple = new ReteTuple( null,
                                            rule,
                                            decl,
                                            handle,
                                            new Object() );

        item.setTuple( newTuple );

        assertSame( newTuple,
                    item.getTuple() );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new MockFactHandle( 2 ) ) );
    }

    public void testFire()
        throws Exception
    {

        RuleBase ruleBase = new RuleBaseImpl( new Rete(), new DefaultConflictResolver());


        Declaration decl = new Declaration( new MockObjectType(true),
                                            "cheese" );

        MockFactHandle handle = new MockFactHandle( 1 );

        Rule rule = new Rule( "test-rule" );

        ReteTuple tuple = new ReteTuple( ruleBase.newWorkingMemory(),
            rule,
            decl,
            handle,
            new Object() );

        InstrumentedConsequence consequence = new InstrumentedConsequence();

        rule.setConsequence( consequence );

        AgendaItem item = new AgendaItem( tuple,
                                          rule );

        item.fire( tuple.getWorkingMemory() );

        assertEquals( 1,
                      consequence.getInvokedTuples().size() );

        assertTrue( consequence.getInvokedTuples().contains( tuple ) );
    }
}
