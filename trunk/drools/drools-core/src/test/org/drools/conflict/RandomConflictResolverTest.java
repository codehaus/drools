package org.drools.conflict;

/*
$Id: RandomConflictResolverTest.java,v 1.1 2004-06-27 15:30:01 mproctor Exp $

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

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.drools.rule.InstrumentedRule;
import org.drools.rule.RuleSet;
import org.drools.spi.ConflictResolver;
import org.drools.spi.MockTuple;

public class RandomConflictResolverTest extends TestCase
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

	public RandomConflictResolverTest( String name )
	{
		super( name );
	}

	public void setUp()
	{
		this.conflictResolver = RandomConflictResolver.getInstance( );
		items = new LinkedList( );

		brieRule = new InstrumentedRule( "brie" );
		brieRule.isValid( true );

		camembertRule = new InstrumentedRule( "camembert" );
		camembertRule.isValid( true );

		stiltonRule = new InstrumentedRule( "stilton" );
		stiltonRule.isValid( true );

		brie = new MockAgendaItem( new MockTuple( ), brieRule );
	camembert = new MockAgendaItem( new MockTuple( ), camembertRule );
		stilton = new MockAgendaItem( new MockTuple( ), stiltonRule );
	}

	public void tearDown()
	{
	}

	public void testSingleInsert() throws Exception
	{
		items.clear( );
		conflictItems = this.conflictResolver.insert( brie, items );
		assertNull( conflictItems );
		MockAgendaItem item = (MockAgendaItem) items.get( 0 );
		assertEquals( "brie", item.getRule( ).getName( ) );
	}

	public void testInsertsNoConflicts() throws Exception
	{
		MockAgendaItem item;
		RuleSet ruleSet;
		items.clear( );
		ruleSet = new RuleSet( "cheese board" );

		ruleSet.addRule( brieRule );
		ruleSet.addRule( camembertRule );
		ruleSet.addRule( stiltonRule );

		//try ascending
		conflictItems = this.conflictResolver.insert( brie, items );
		assertNull( conflictItems );
		conflictItems = this.conflictResolver.insert( camembert, items );
		assertNull( conflictItems );
		conflictItems = this.conflictResolver.insert( stilton, items );
		assertNull( conflictItems );

    assertEquals(3, items.size());

		//try descending
		items.clear( );
		conflictItems = this.conflictResolver.insert( stilton, items );
		assertNull( conflictItems );
		conflictItems = this.conflictResolver.insert( camembert, items );
		assertNull( conflictItems );
		conflictItems = this.conflictResolver.insert( brie, items );
		assertNull( conflictItems );

    assertEquals(3, items.size());

		//try mixed order
		items.clear( );
		conflictItems = this.conflictResolver.insert( camembert, items );
		assertNull( conflictItems );
		conflictItems = this.conflictResolver.insert( stilton, items );
		assertNull( conflictItems );
		conflictItems = this.conflictResolver.insert( brie, items );
		assertNull( conflictItems );

    assertEquals(3, items.size());
	}
}