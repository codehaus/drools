package org.drools.conflict;

/*
$Id: BreadthFactConflictResolverTest.java,v 1.2 2004-09-16 23:43:08 mproctor Exp $

Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.

Redistribution and use of this software and associated documentation
("Software"), with or without modification, are permitted provided
that the following conditions are met:

1. Redistributions of source code must retain copyright
statements and notices.  Redistributions must also contain a
copy of this document.

2. Redistributions in binary form must reproduce the
above copyright notice, this list of conditions and the
following disclaimer in the documentation and/or other
materials provided with the distribution.

3. The name "drools" must not be used to endorse or promote
products derived from this Software without prior written
permission of The Werken Company.  For written permission,
please contact bob@werken.com.

4. Products derived from this Software may not be called "drools"
nor may "drools" appear in their names without prior written
permission of The Werken Company. "drools" is a trademark of
The Werken Company.

5. Due credit should be given to The Werken Company.
(http://werken.com/)

THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
OF THE POSSIBILITY OF SUCH DAMAGE.

*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.drools.rule.InstrumentedRule;
import org.drools.spi.ConflictResolver;
import org.drools.spi.MockTuple;

public class BreadthFactConflictResolverTest extends TestCase
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

  	public BreadthFactConflictResolverTest( String name )
  	{
  		  super( name );
  	}

  	public void setUp()
  	{
    		this.conflictResolver = BreadthFactConflictResolver.getInstance( );
    		items = new LinkedList( );

    		brieRule = new InstrumentedRule( "brie" );
    		camembertRule = new InstrumentedRule( "camembert" );
    		stiltonRule = new InstrumentedRule( "stilton" );
    		cheddarRule = new InstrumentedRule( "cheddar" );
    		fetaRule = new InstrumentedRule( "feta" );
    		mozzarellaRule = new InstrumentedRule( "mozzarella" );

    		brie = new MockAgendaItem( new MockTuple( ), brieRule );
    		camembert = new MockAgendaItem( new MockTuple( ), camembertRule );
    		stilton = new MockAgendaItem( new MockTuple( ), stiltonRule );
    		cheddar = new MockAgendaItem( new MockTuple( ), cheddarRule );
    		feta = new MockAgendaItem( new MockTuple( ), fetaRule );
    		mozzarella = new MockAgendaItem( new MockTuple( ), mozzarellaRule );
  	}

  	public void tearDown()
  	{
    		this.conflictResolver = null;
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

  	public void testSingleInsert() throws Exception
  	{
    		items.clear( );
            MockTuple tuple = (MockTuple) brie.getTuple();
            tuple.setMostRecentFactTimeStamp(0);
    		conflictItems = this.conflictResolver.insert( brie, items );
    		assertNull( conflictItems );
    		MockAgendaItem item = (MockAgendaItem) items.get( 0 );
    		assertEquals( "brie", item.getRule( ).getName( ) );
  	}
    

  	public void testInsertsNoConflicts()
  	{
    		MockAgendaItem item;
    		items.clear( );

            MockTuple tuple = (MockTuple) brie.getTuple();
            tuple.setLeastRecentFactTimeStamp(1);   
            tuple = (MockTuple) camembert.getTuple();
            tuple.setLeastRecentFactTimeStamp(2);   
            tuple = (MockTuple) stilton.getTuple();
            tuple.setLeastRecentFactTimeStamp(3);               
            
    		//try ascending
    		conflictItems = this.conflictResolver.insert( brie, items );
    		assertNull( conflictItems );
    		conflictItems = this.conflictResolver.insert( camembert, items );
    		assertNull( conflictItems );
    		conflictItems = this.conflictResolver.insert( stilton, items );
    		assertNull( conflictItems );

            assertEquals( 3, items.size() );
            assertEquals( brie, items.get( 0 ) );
            assertEquals( stilton, items.get( 2 ) );
            assertEquals( camembert, items.get( 1 ) );

    		//try descending
    		items.clear( );
    		conflictItems = this.conflictResolver.insert( stilton, items );
    		assertNull( conflictItems );
    		conflictItems = this.conflictResolver.insert( camembert, items );
    		assertNull( conflictItems );
    		conflictItems = this.conflictResolver.insert( brie, items );
    		assertNull( conflictItems );

    		assertEquals( 3, items.size() );
            assertEquals( brie, items.get( 0 ) );
    		assertEquals( stilton, items.get( 2 ) );
    		assertEquals( camembert, items.get( 1 ) );
            
    		//try mixed order
    		items.clear( );
    		conflictItems = this.conflictResolver.insert( camembert, items );
    		assertNull( conflictItems );
    		conflictItems = this.conflictResolver.insert( stilton, items );
    		assertNull( conflictItems );
    		conflictItems = this.conflictResolver.insert( brie, items );
    		assertNull( conflictItems );

            assertEquals( 3, items.size() );
            assertEquals( brie, items.get( 0 ) );
            assertEquals( stilton, items.get( 2 ) );
            assertEquals( camembert, items.get( 1 ) );           
  	}

    public void testInsertsWithConflicts()
    {
            MockAgendaItem item;
            items.clear( );

            MockTuple tuple = (MockTuple) brie.getTuple();
            tuple.setLeastRecentFactTimeStamp(1); 
            tuple = (MockTuple) feta.getTuple();
            tuple.setLeastRecentFactTimeStamp(1);             
            tuple = (MockTuple) camembert.getTuple();
            tuple.setLeastRecentFactTimeStamp(1);   
 

            conflictItems = this.conflictResolver.insert( brie, items );
            assertNull( conflictItems );

            conflictItems = this.conflictResolver.insert( feta, items );
            assertEquals( 1, conflictItems.size( ) );
            
            tuple = (MockTuple) ((MockAgendaItem) conflictItems.get( 0 )).getTuple();
            assertEquals( 1, tuple.getLeastRecentFactTimeStamp( ) );
            
            assertEquals( "brie", ((MockAgendaItem) conflictItems.get( 0 ))
                          .getRule( ).getName( ) );
                        
            conflictItems = this.conflictResolver.insert( camembert, items );
            assertEquals( 1, conflictItems.size( ) );

            tuple = (MockTuple) ((MockAgendaItem) conflictItems.get( 0 )).getTuple();
            assertEquals( 1, tuple.getLeastRecentFactTimeStamp( ) );
            
            assertEquals( "brie", ((MockAgendaItem) conflictItems.get( 0 ))
                          .getRule( ).getName( ) );
            
            assertEquals(1, items.size());            
            assertEquals(brie, items.get(0));           

            items.clear( );
            
            tuple = (MockTuple) brie.getTuple();
            tuple.setLeastRecentFactTimeStamp(1); 
            tuple = (MockTuple) feta.getTuple();
            tuple.setLeastRecentFactTimeStamp(1);             
            tuple = (MockTuple) camembert.getTuple();
            tuple.setLeastRecentFactTimeStamp(2);  
            tuple = (MockTuple) stilton.getTuple();
            tuple.setLeastRecentFactTimeStamp(3); 
            tuple = (MockTuple) cheddar.getTuple();
            tuple.setLeastRecentFactTimeStamp(3);             
            tuple = (MockTuple) mozzarella.getTuple();
            tuple.setLeastRecentFactTimeStamp(4);               

            conflictItems = this.conflictResolver.insert( stilton, items );
            assertNull( conflictItems );
            
            conflictItems = this.conflictResolver.insert( mozzarella, items );
            assertNull( conflictItems );

            conflictItems = this.conflictResolver.insert( cheddar, items );
            assertEquals( 1, conflictItems.size( ) );

            tuple = (MockTuple) ((MockAgendaItem) conflictItems.get( 0 )).getTuple();
            assertEquals( 3, tuple.getLeastRecentFactTimeStamp( ) );
            
            assertEquals( "stilton", ((MockAgendaItem) conflictItems.get( 0 ))
                    .getRule( ).getName( ) );

            conflictItems = this.conflictResolver.insert( brie, items );
            assertNull( conflictItems );

            conflictItems = this.conflictResolver.insert( feta, items );
            assertEquals( 1, conflictItems.size( ) );

            tuple = (MockTuple) ((MockAgendaItem) conflictItems.get( 0 )).getTuple();
            assertEquals( 1, tuple.getLeastRecentFactTimeStamp( ) );            
            assertEquals( "brie", ((MockAgendaItem) conflictItems.get( 0 ))
                    .getRule( ).getName( ) );

            conflictItems = this.conflictResolver.insert( camembert, items );
            assertNull( conflictItems );

            assertEquals(4, items.size());            
            assertEquals(brie, items.get(0));
            assertEquals(camembert, items.get(1));
            assertEquals(stilton, items.get(2));
            assertEquals(mozzarella, items.get(3));

            items.clear( );

            tuple = (MockTuple) brie.getTuple();
            tuple.setLeastRecentFactTimeStamp(1); 
            tuple = (MockTuple) feta.getTuple();
            tuple.setLeastRecentFactTimeStamp(3);             
            tuple = (MockTuple) camembert.getTuple();
            tuple.setLeastRecentFactTimeStamp(3);  
            tuple = (MockTuple) stilton.getTuple();
            tuple.setLeastRecentFactTimeStamp(3); 
            tuple = (MockTuple) cheddar.getTuple();
            tuple.setLeastRecentFactTimeStamp(4);             
            tuple = (MockTuple) mozzarella.getTuple();
            tuple.setLeastRecentFactTimeStamp(4);               

            conflictItems = this.conflictResolver.insert( stilton, items );
            assertNull( conflictItems );
            conflictItems = this.conflictResolver.insert( mozzarella, items );
            assertNull( conflictItems );

            conflictItems = this.conflictResolver.insert( cheddar, items );
            assertEquals( 1, conflictItems.size( ) );
            tuple = (MockTuple) ((MockAgendaItem) conflictItems.get( 0 )).getTuple();
            assertEquals( 4, tuple.getLeastRecentFactTimeStamp( ) );   
            assertEquals( "mozzarella", ((MockAgendaItem) conflictItems.get( 0 ))
                    .getRule( ).getName( ) );

            conflictItems = this.conflictResolver.insert( brie, items );
            assertNull( conflictItems );

            conflictItems = this.conflictResolver.insert( feta, items );
            assertEquals( 1, conflictItems.size( ) );
            tuple = (MockTuple) ((MockAgendaItem) conflictItems.get( 0 )).getTuple();
            assertEquals( 3, tuple.getLeastRecentFactTimeStamp( ) );   
            assertEquals( "stilton", ((MockAgendaItem) conflictItems.get( 0 ))
                    .getRule( ).getName( ) );

            conflictItems = this.conflictResolver.insert( camembert, items );
            assertEquals( 1, conflictItems.size( ) );
            tuple = (MockTuple) ((MockAgendaItem) conflictItems.get( 0 )).getTuple();
            assertEquals( 3, tuple.getLeastRecentFactTimeStamp( ) );   
            assertEquals( "stilton", ((MockAgendaItem) conflictItems.get( 0 ))
                    .getRule( ).getName( ) );
            
            assertEquals(3, items.size());            
            assertEquals(brie, items.get(0));
            assertEquals(stilton, items.get(1));
            assertEquals(mozzarella, items.get(2));
                    
    }    
    
    public void testSerialize() throws Exception
    {
        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        ObjectOutput out = new ObjectOutputStream(bos) ;
        out.writeObject(conflictResolver);
        out.close();

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray();

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        conflictResolver = (ConflictResolver) in.readObject();
        in.close();
    }
    
}