package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.MockFactHandle;
import org.drools.rule.Rule;
import org.drools.rule.Declaration;
import org.drools.spi.InstrumentedConsequence;
import org.drools.MockObjectType;

public class AgendaItemTest
    extends DroolsTestCase
{
    public void testConstruct()
        throws Exception
    {
        Declaration decl = new Declaration( new MockObjectType(),
                                            "cheese" );

        MockFactHandle handle = new MockFactHandle( 1 );

        ReteTuple tuple = new ReteTuple( null,
                                         null,
                                         decl,
                                         handle,
                                         new Object() );

        Rule rule = new Rule( "test-rule" );

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
        Declaration decl = new Declaration( new MockObjectType(),
                                            "cheese" );
        
        MockFactHandle handle = new MockFactHandle( 1 );
        
        ReteTuple tuple = new ReteTuple( null,
                                         null,
                                         decl,
                                         handle,
                                         new Object() );
        
        Rule rule = new Rule( "test-rule" );
        
        AgendaItem item = new AgendaItem( tuple,
                                          rule );
        
        assertSame( tuple,
                    item.getTuple() );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new MockFactHandle( 2 ) ) );

        ReteTuple newTuple = new ReteTuple( null,
                                            null,
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
        Declaration decl = new Declaration( new MockObjectType(),
                                            "cheese" );

        MockFactHandle handle = new MockFactHandle( 1 );

        ReteTuple tuple = new ReteTuple( null,
                                         null,
                                         decl,
                                         handle,
                                         new Object() );

        Rule rule = new Rule( "test-rule" );

        InstrumentedConsequence consequence = new InstrumentedConsequence();

        rule.setConsequence( consequence );

        AgendaItem item = new AgendaItem( tuple,
                                          rule );

        item.fire( null );

        assertEquals( 1,
                      consequence.getInvokedTuples().size() );

        assertTrue( consequence.getInvokedTuples().contains( tuple ) );
    }
}
