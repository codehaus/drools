package org.drools.conflict;

import junit.framework.TestCase;
import org.drools.rule.Rule;
import org.drools.conflict.SalienceConflictResolver;
import org.drools.spi.ConflictResolver;

import java.util.List;
import java.util.LinkedList;

public class SalienceConflictResolverTest extends TestCase
{
    private ConflictResolver conflictResolver;

    private Rule brieRule;
    private Rule camembertRule;
    private Rule stiltonRule;
    private Rule cheddarRule;
    private Rule fetaRule;
    private Rule mozzarellaRule;

    private MockAgendaItem brie;
    private MockAgendaItem camembert;
    private MockAgendaItem stilton;
    private MockAgendaItem cheddar;
    private MockAgendaItem feta;
    private MockAgendaItem mozzarella;

    private LinkedList items;
    private List conflictItems;

    public SalienceConflictResolverTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.conflictResolver = SalienceConflictResolver.getInstance();
        items = new LinkedList();

        brieRule = new Rule( "brie" );
        camembertRule = new Rule( "camembert" );
        stiltonRule = new Rule( "stilton" );
        cheddarRule = new Rule( "cheddar" );
        fetaRule = new Rule( "feta" );
        mozzarellaRule = new Rule( "mozzarella" );

        brie = new MockAgendaItem( new MockTuple( ),
                                   brieRule );
        camembert = new MockAgendaItem( new MockTuple( ),
                                        camembertRule );
        stilton = new MockAgendaItem( new MockTuple( ),
                                      stiltonRule );
        cheddar = new MockAgendaItem( new MockTuple( ),
                                      cheddarRule );
        feta = new MockAgendaItem( new MockTuple( ),
                                   fetaRule );
        mozzarella = new MockAgendaItem( new MockTuple( ),
                                         mozzarellaRule );
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

    public void testInsertsNoConflicts()
    {
        MockAgendaItem item;
        items.clear();
        brieRule.setSalience(1);
        camembertRule.setSalience(2);
        stiltonRule.setSalience(3);

        //try ascending
        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);

        item = (MockAgendaItem) items.get(0);
        assertEquals("stilton", item.getRule().getName());
        item = (MockAgendaItem) items.get(1);
        assertEquals("camembert", item.getRule().getName());
        item = (MockAgendaItem) items.get(2);
        assertEquals("brie", item.getRule().getName());

        //try descending
        items.clear();
        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);

        item = (MockAgendaItem) items.get(0);
        assertEquals("stilton", item.getRule().getName());
        item = (MockAgendaItem) items.get(1);
        assertEquals("camembert", item.getRule().getName());
        item = (MockAgendaItem) items.get(2);
        assertEquals("brie", item.getRule().getName());

        //try mixed order
        items.clear();
        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);

        item = (MockAgendaItem) items.get(0);
        assertEquals("stilton", item.getRule().getName());
        item = (MockAgendaItem) items.get(1);
        assertEquals("camembert", item.getRule().getName());
        item = (MockAgendaItem) items.get(2);
        assertEquals("brie", item.getRule().getName());
    }

    public void testInsertsWithConflicts()
    {
        MockAgendaItem item;
        items.clear();

        brieRule.setSalience(1);
        fetaRule.setSalience(1);
        camembertRule.setSalience(2);
        stiltonRule.setSalience(3);
        cheddarRule.setSalience(3);
        mozzarellaRule.setSalience(4);


        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(mozzarella, items);
        assertNull(conflictItems);

        conflictItems = this.conflictResolver.insert(cheddar, items);
        assertEquals(1, conflictItems.size());
        assertEquals(3, ((MockAgendaItem) conflictItems.get(0)).getRule().getSalience());
        assertEquals("stilton", ((MockAgendaItem) conflictItems.get(0)).getRule().getName());

        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);

        conflictItems = this.conflictResolver.insert(feta, items);
        assertEquals(1, conflictItems.size());
        assertEquals(1, ((MockAgendaItem) conflictItems.get(0)).getRule().getSalience());
        assertEquals("brie", ((MockAgendaItem) conflictItems.get(0)).getRule().getName());

        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);


        items.clear();

        brieRule.setSalience(1);
        fetaRule.setSalience(3);
        camembertRule.setSalience(3);
        stiltonRule.setSalience(3);
        cheddarRule.setSalience(4);
        mozzarellaRule.setSalience(4);


        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(mozzarella, items);
        assertNull(conflictItems);

        conflictItems = this.conflictResolver.insert(cheddar, items);
        assertEquals(1, conflictItems.size());
        assertEquals(4, ((MockAgendaItem) conflictItems.get(0)).getRule().getSalience());
        assertEquals("mozzarella", ((MockAgendaItem) conflictItems.get(0)).getRule().getName());

        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);

        conflictItems = this.conflictResolver.insert(feta, items);
        assertEquals(1, conflictItems.size());
        assertEquals(3, ((MockAgendaItem) conflictItems.get(0)).getRule().getSalience());
        assertEquals("stilton", ((MockAgendaItem) conflictItems.get(0)).getRule().getName());

        conflictItems = this.conflictResolver.insert(camembert, items);
        assertEquals(1, conflictItems.size());
        assertEquals(3, ((MockAgendaItem) conflictItems.get(0)).getRule().getSalience());
        assertEquals("stilton", ((MockAgendaItem) conflictItems.get(0)).getRule().getName());
    }
}
