package org.drools.conflict;

/*
 * $Id: BreadthFactConflictResolverTest.java,v 1.2 2004/09/16 23:43:08 mproctor
 * Exp $
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

import junit.framework.TestCase;
import org.drools.PriorityQueue;
import org.drools.rule.InstrumentedRule;
import org.drools.spi.ConflictResolver;
import org.drools.spi.MockTuple;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class RecencyConflictResolverTest extends TestCase
{
    private ConflictResolver conflictResolver;

    private InstrumentedRule brieRule;

    private InstrumentedRule camembertRule;

    private InstrumentedRule stiltonRule;

    private MockAgendaItem   brie;

    private MockAgendaItem   camembert;

    private MockAgendaItem   stilton;

    private PriorityQueue    items;

    public RecencyConflictResolverTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.conflictResolver = RecencyConflictResolver.getInstance( );
        items = new PriorityQueue( conflictResolver );

        brieRule = new InstrumentedRule( "brie" );
        camembertRule = new InstrumentedRule( "camembert" );
        stiltonRule = new InstrumentedRule( "stilton" );

        brie = new MockAgendaItem( new MockTuple( ), brieRule );
        camembert = new MockAgendaItem( new MockTuple( ), camembertRule );
        stilton = new MockAgendaItem( new MockTuple( ), stiltonRule );

        MockTuple tuple = ( MockTuple ) brie.getTuple( );
        tuple.setMostRecentFactTimeStamp( 1 );
        tuple = ( MockTuple ) camembert.getTuple( );
        tuple.setMostRecentFactTimeStamp( 2 );
        tuple = ( MockTuple ) stilton.getTuple( );
        tuple.setMostRecentFactTimeStamp( 3 );
    }

    public void tearDown()
    {
        conflictResolver = null;
        items = null;

        brieRule = null;
        camembertRule = null;
        stiltonRule = null;

        brie = null;
        camembert = null;
        stilton = null;
    }

    public void testSingleInsert()
    {
        this.items.add( brie );

        assertEquals( 1, this.items.size( ) );

        assertSame( brie, this.items.remove( ) );
    }

    public void testAscendingOrderInsert()
    {
        this.items.add( brie );
        this.items.add( camembert );
        this.items.add( stilton );

        assertEquals( 3, items.size( ) );

        assertSame( brie, items.remove( ) );
        assertSame( camembert, items.remove( ) );
        assertSame( stilton, items.remove( ) );
    }

    public void testDescendingOrderInsert()
    {
        this.items.add( stilton );
        this.items.add( camembert );
        this.items.add( brie );

        assertEquals( 3, items.size( ) );

        assertSame( brie, items.remove( ) );
        assertSame( camembert, items.remove( ) );
        assertSame( stilton, items.remove( ) );
    }

    public void testMixedOrderInsert()
    {
        this.items.add( camembert );
        this.items.add( stilton );
        this.items.add( brie );

        assertEquals( 3, items.size( ) );

        assertSame( brie, items.remove( ) );
        assertSame( camembert, items.remove( ) );
        assertSame( stilton, items.remove( ) );
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