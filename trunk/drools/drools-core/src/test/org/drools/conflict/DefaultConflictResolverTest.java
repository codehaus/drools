package org.drools.conflict;

import junit.framework.TestCase;
import org.drools.rule.InstrumentedRule;
import org.drools.rule.RuleSet;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.spi.ConflictResolver;

import java.util.List;
import java.util.LinkedList;

public class DefaultConflictResolverTest extends TestCase
{
    private ConflictResolver conflictResolver;

    private InstrumentedRule brieRule;
    private InstrumentedRule camembertRule;
    private InstrumentedRule stiltonRule;
    private InstrumentedRule cheddarRule;
    private InstrumentedRule fetaRule;
    private InstrumentedRule mozzarellaRule;

    private MockAgendaItem brie;
    private MockAgendaItem camembert;
    private MockAgendaItem stilton;
    private MockAgendaItem cheddar;
    private MockAgendaItem feta;
    private MockAgendaItem mozzarella;

    private LinkedList items;
    private List conflictItems;

    public DefaultConflictResolverTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.conflictResolver = DefaultConflictResolver.getInstance();
        items = new LinkedList();

        brieRule = new InstrumentedRule( "brie" );
        brieRule.isValid( true );

        camembertRule = new InstrumentedRule( "camembert" );
        camembertRule.isValid( true );

        stiltonRule = new InstrumentedRule( "stilton" );
        stiltonRule.isValid( true );

        cheddarRule = new InstrumentedRule( "cheddar" );
        cheddarRule.isValid( true );

        fetaRule = new InstrumentedRule( "feta" );
        fetaRule.isValid( true );

        mozzarellaRule = new InstrumentedRule( "mozzarella" );
        mozzarellaRule.isValid( true );

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

    public void testInsertsWithConflicts() throws Exception
    {
        MockAgendaItem item;
        RuleSet ruleSet;
        items.clear();
        ruleSet = new RuleSet( "cheese board" );


        brieRule.setSalience(0);
        fetaRule.setSalience(0);
        camembertRule.setSalience(0);

        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(feta, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);

        assertEquals("brie", ((MockAgendaItem) items.get(0)).getRule().getName());
        assertEquals("feta", ((MockAgendaItem) items.get(1)).getRule().getName());
        assertEquals("camembert", ((MockAgendaItem) items.get(2)).getRule().getName());

        items.clear();

        brieRule.setSalience(1);
        fetaRule.setSalience(1);
        camembertRule.setSalience(2);
        stiltonRule.setSalience(3);
        cheddarRule.setSalience(3);
        mozzarellaRule.setSalience(4);

        ruleSet.addRule(mozzarellaRule);
        ruleSet.addRule(camembertRule);
        ruleSet.addRule(cheddarRule);
        ruleSet.addRule(brieRule);
        ruleSet.addRule(stiltonRule);
        ruleSet.addRule(fetaRule);

        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(mozzarella, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(cheddar, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(feta, items);
        assertNull(conflictItems);

        assertEquals("mozzarella", ((MockAgendaItem) items.get(0)).getRule().getName());
        assertEquals("cheddar", ((MockAgendaItem) items.get(1)).getRule().getName());
        assertEquals("stilton", ((MockAgendaItem) items.get(2)).getRule().getName());
        assertEquals("camembert", ((MockAgendaItem) items.get(3)).getRule().getName());
        assertEquals("brie", ((MockAgendaItem) items.get(4)).getRule().getName());
        assertEquals("feta", ((MockAgendaItem) items.get(5)).getRule().getName());

        ruleSet = new RuleSet( "cheese board" );
        items.clear();

        brieRule.setSalience(1);
        fetaRule.setSalience(3);
        camembertRule.setSalience(3);
        stiltonRule.setSalience(3);
        cheddarRule.setSalience(4);
        mozzarellaRule.setSalience(4);

        ruleSet.addRule(stiltonRule);
        ruleSet.addRule(mozzarellaRule);
        ruleSet.addRule(camembertRule);
        ruleSet.addRule(cheddarRule);
        ruleSet.addRule(fetaRule);
        ruleSet.addRule(brieRule);

        conflictItems = this.conflictResolver.insert(stilton, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(mozzarella, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(cheddar, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(brie, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(feta, items);
        assertNull(conflictItems);
        conflictItems = this.conflictResolver.insert(camembert, items);
        assertNull(conflictItems);

        assertEquals("mozzarella", ((MockAgendaItem) items.get(0)).getRule().getName());
        assertEquals("cheddar", ((MockAgendaItem) items.get(1)).getRule().getName());
        assertEquals("stilton", ((MockAgendaItem) items.get(2)).getRule().getName());
        assertEquals("camembert", ((MockAgendaItem) items.get(3)).getRule().getName());
        assertEquals("feta", ((MockAgendaItem) items.get(4)).getRule().getName());
        assertEquals("brie", ((MockAgendaItem) items.get(5)).getRule().getName());


    }
}
