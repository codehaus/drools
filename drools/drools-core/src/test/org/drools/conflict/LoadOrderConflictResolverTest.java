package org.drools.conflict;

import junit.framework.TestCase;
import org.drools.rule.InstrumentedRule;
import org.drools.rule.RuleSet;
import org.drools.conflict.LoadOrderConflictResolver;
import org.drools.spi.ConflictResolver;

import java.util.List;
import java.util.LinkedList;

public class LoadOrderConflictResolverTest extends TestCase
{
    private ConflictResolver conflictResolver;

    private InstrumentedRule brieRule;
    private InstrumentedRule camembertRule;
    private InstrumentedRule stiltonRule;

    private MockAgendaItem brie;
    private MockAgendaItem camembert;
    private MockAgendaItem stilton;

    private LinkedList items;
    private List conflictItems;

    public LoadOrderConflictResolverTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.conflictResolver = LoadOrderConflictResolver.getInstance();
        items = new LinkedList();

        brieRule = new InstrumentedRule( "brie" );
        brieRule.isValid( true );

        camembertRule = new InstrumentedRule( "camembert" );
        camembertRule.isValid( true );

        stiltonRule = new InstrumentedRule( "stilton" );
        stiltonRule.isValid( true );

        brie = new MockAgendaItem( new MockTuple( ),
                                   brieRule );
        camembert = new MockAgendaItem( new MockTuple( ),
                                        camembertRule );
        stilton = new MockAgendaItem( new MockTuple( ),
                                      stiltonRule );
    }

    public void tearDown()
    {
    }

    public void testSingleInsert()
        throws Exception
    {
        items.clear();
        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);
        MockAgendaItem item = (MockAgendaItem) items.get(0);
        assertEquals("brie", item.getRule().getName());
    }

    public void testInsertsNoConflicts() throws Exception
    {
        MockAgendaItem item;
        RuleSet ruleSet;
        items.clear();
        ruleSet = new RuleSet( "cheese board" );

        ruleSet.addRule(brieRule);
        ruleSet.addRule(camembertRule);
        ruleSet.addRule(stiltonRule);

        //try ascending
        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);

        item = (MockAgendaItem) items.get(0);
        assertEquals("brie", item.getRule().getName());
        item = (MockAgendaItem) items.get(1);
        assertEquals("camembert", item.getRule().getName());
        item = (MockAgendaItem) items.get(2);
        assertEquals("stilton", item.getRule().getName());

        //try descending
        items.clear();
        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);

        item = (MockAgendaItem) items.get(0);
        assertEquals("brie", item.getRule().getName());
        item = (MockAgendaItem) items.get(1);
        assertEquals("camembert", item.getRule().getName());
        item = (MockAgendaItem) items.get(2);
        assertEquals("stilton", item.getRule().getName());

        //try mixed order
        items.clear();
        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);

        item = (MockAgendaItem) items.get(0);
        assertEquals("brie", item.getRule().getName());
        item = (MockAgendaItem) items.get(1);
        assertEquals("camembert", item.getRule().getName());
        item = (MockAgendaItem) items.get(2);
        assertEquals("stilton", item.getRule().getName());
    }
}
