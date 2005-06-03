package org.drools.conflict;

/*
 * $Id: DefaultConflictResolverTest.java,v 1.12 2005-02-04 02:13:36 mproctor Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import org.drools.rule.InstrumentedRule;
import org.drools.rule.RuleSet;
import org.drools.spi.ConflictResolver;
import org.drools.spi.InstrumentedCondition;
import org.drools.spi.MockAgendaItem;
import org.drools.spi.MockTuple;
import org.drools.util.PriorityQueue;

public class DefaultConflictResolverTest extends TestCase
{
    private ConflictResolver conflictResolver;

    private InstrumentedRule brieRule;

    private InstrumentedRule camembertRule;

    private InstrumentedRule stiltonRule;

    private InstrumentedRule cheddarRule;

    private InstrumentedRule fetaRule;

    private InstrumentedRule mozzarellaRule;

    private MockAgendaItem   brie;

    private MockAgendaItem   camembert;

    private MockAgendaItem   stilton;

    private MockAgendaItem   cheddar;

    private MockAgendaItem   feta;

    private MockAgendaItem   mozzarella;

    private PriorityQueue    items;

    public DefaultConflictResolverTest(String name)
    {
        super( name );
    }

    public void setUp() throws Exception
    {
        this.conflictResolver = DefaultConflictResolver.getInstance( );
        items = new PriorityQueue( this.conflictResolver );

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

        brie = new MockAgendaItem( new MockTuple( ), brieRule );
        camembert = new MockAgendaItem( new MockTuple( ), camembertRule );
        stilton = new MockAgendaItem( new MockTuple( ), stiltonRule );
        cheddar = new MockAgendaItem( new MockTuple( ), cheddarRule );
        feta = new MockAgendaItem( new MockTuple( ), fetaRule );
        mozzarella = new MockAgendaItem( new MockTuple( ), mozzarellaRule );

        MockTuple tuple = ( MockTuple ) brie.getTuple( );
        tuple.setMostRecentFactTimeStamp( 1 );
        tuple = ( MockTuple ) feta.getTuple( );
        tuple.setMostRecentFactTimeStamp( 2 );
        tuple = ( MockTuple ) camembert.getTuple( );
        tuple.setMostRecentFactTimeStamp( 3 );

        brieRule.setSalience( 1 );
        fetaRule.setSalience( 1 );
        camembertRule.setSalience( 2 );
        stiltonRule.setSalience( 3 );
        cheddarRule.setSalience( 3 );
        mozzarellaRule.setSalience( 4 );

        RuleSet ruleSet;
        ruleSet = new RuleSet( "cheese board" );

        ruleSet.addRule( mozzarellaRule );
        ruleSet.addRule( camembertRule );
        ruleSet.addRule( cheddarRule );
        ruleSet.addRule( brieRule );
        ruleSet.addRule( stiltonRule );
        ruleSet.addRule( fetaRule );
    }

    public void tearDown()
    {
        conflictResolver = null;
        items = null;

        brieRule = null;
        camembertRule = null;
        stiltonRule = null;
        cheddarRule = null;
        fetaRule = null;
        mozzarellaRule = null;

        brie = null;
        camembert = null;
        stilton = null;
        cheddar = null;
        feta = null;
        mozzarella = null;
    }

    public void testSingleInsert()
    {
        this.items.add( brie );

        assertEquals( 1, this.items.size( ) );

        assertSame( brie, this.items.remove( ) );
    }

    public void testAscendingOrderInsert()
    {
        brieRule.setSalience( 0 );
        fetaRule.setSalience( 0 );
        camembertRule.setSalience( 0 );

        this.items.add( brie );
        this.items.add( feta );
        this.items.add( camembert );

        assertEquals( 3, this.items.size( ) );

        assertSame( brie, items.remove( ) );
        assertSame( feta, items.remove( ) );
        assertSame( camembert, items.remove( ) );
    }

    public void testWhatDoesThisTest() throws Exception
    {
        //one condition
        brieRule.addCondition( new InstrumentedCondition( ) );
        //one condition
        fetaRule.addCondition( new InstrumentedCondition( ) );
        //one condition
        cheddarRule.addCondition( new InstrumentedCondition( ) );
        //two conditions
        stiltonRule.addCondition( new InstrumentedCondition( ) );
        stiltonRule.addCondition( new InstrumentedCondition( ) );

        this.items.add( stilton );
        this.items.add( mozzarella );
        this.items.add( cheddar );
        this.items.add( camembert );
        this.items.add( brie );
        this.items.add( feta );

        assertSame( mozzarella, items.remove( ) );
        assertSame( stilton, items.remove( ) );
        assertSame( cheddar, items.remove( ) );
        assertSame( camembert, items.remove( ) );
        assertSame( brie, items.remove( ) );
        assertSame( feta, items.remove( ) );
    }

    public void testAndWhatIsThisTesting() throws Exception
    {
        //one condition
        cheddarRule.addCondition( new InstrumentedCondition( ) );
        //one condition
        mozzarellaRule.addCondition( new InstrumentedCondition( ) );

        //one condition
        camembertRule.addCondition( new InstrumentedCondition( ) );
        //two conditions
        fetaRule.addCondition( new InstrumentedCondition( ) );
        fetaRule.addCondition( new InstrumentedCondition( ) );

        //three conditions
        stiltonRule.addCondition( new InstrumentedCondition( ) );
        stiltonRule.addCondition( new InstrumentedCondition( ) );
        stiltonRule.addCondition( new InstrumentedCondition( ) );

        this.items.add( stilton );
        this.items.add( mozzarella );
        this.items.add( cheddar );
        this.items.add( brie );
        this.items.add( feta );
        this.items.add( camembert );

        assertSame( mozzarella, items.remove( ) );
        assertSame( stilton, items.remove( ) );
        assertSame( cheddar, items.remove( ) );
        assertSame( camembert, items.remove( ) );
        assertSame( brie, items.remove( ) );
        assertSame( feta, items.remove( ) );
    }

    public void testSerialize() throws Exception
    {
        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream( );
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( conflictResolver );
        out.close( );

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray( );

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream(
                                                new ByteArrayInputStream( bytes ) );
        conflictResolver = ( ConflictResolver ) in.readObject( );
        in.close( );
    }
}