package org.drools.reteoo;

/*
 * $Id: TupleSetTest.java,v 1.6 2005-05-08 04:05:13 dbarnett Exp $
 *
 * Copyright 2003-2005 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
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

import org.drools.DroolsTestCase;

public class TupleSetTest extends DroolsTestCase
{
    public void testConstruct_Empty()
    {
        TupleSet set = new TupleSet( );

        assertEquals( 0, set.size( ) );

        assertFalse( set.iterator( ).hasNext( ) );
    }

    /*
     * public void testConstruct_WithSingleTuple() { ReteTuple tuple = new
     * ReteTuple();
     *
     * TupleSet set = new TupleSet( tuple );
     *
     * assertEquals( 1, set.size() );
     *
     * assertLength( 1, set.getTuples() );
     *
     * Iterator iter = set.iterator();
     *
     * assertTrue( iter.hasNext() );
     *
     * assertSame( tuple, iter.next() );
     *
     * assertFalse( iter.hasNext() ); }
     *
     * public void testConstruct_WithSetOfTuples() { ReteTuple tuple1 = new
     * ReteTuple() { public TupleKey getKey() { return new TupleKey( new
     * FactHandleImpl( 1 ) ); } };
     *
     * ReteTuple tuple2 = new ReteTuple() { public TupleKey getKey() { return
     * new TupleKey( new FactHandleImpl( 2 ) ); } };
     *
     * Set initSet = new HashSet();
     *
     * initSet.add( tuple1 ); initSet.add( tuple2 );
     *
     * assertLength( 2, initSet );
     *
     * TupleSet set = new TupleSet( initSet );
     *
     * assertEquals( 2, set.size() );
     *
     * assertLength( 2, set.getTuples() );
     *
     * Iterator iter = set.iterator();
     *
     * assertTrue( iter.hasNext() );
     *
     * assertTrue( initSet.remove( iter.next() ) );
     *
     * assertTrue( iter.hasNext() );
     *
     * assertTrue( initSet.remove( iter.next() ) );
     *
     * assertLength( 0, initSet );
     *
     * assertFalse( iter.hasNext() );
     *  }
     *
     * public void testAddAllTuples_TupleSet() { ReteTuple tuple1 = new
     * ReteTuple() { public TupleKey getKey() { return new TupleKey( new
     * FactHandleImpl( 1 ) ); } };
     *
     * ReteTuple tuple2 = new ReteTuple() { public TupleKey getKey() { return
     * new TupleKey( new FactHandleImpl( 2 ) ); } };
     *
     * Set initSet = new HashSet();
     *
     * initSet.add( tuple1 ); initSet.add( tuple2 );
     *
     * assertLength( 2, initSet );
     *
     * TupleSet set = new TupleSet( initSet );
     *
     * assertEquals( 2, set.size() );
     *
     * assertLength( 2, set.getTuples() );
     *
     * TupleSet otherSet = new TupleSet();
     *
     * assertEquals( 0, otherSet.size() );
     *
     * otherSet.addAllTuples( set );
     *
     * assertEquals( 2, otherSet.size() );
     *
     * assertContains( tuple1, otherSet.getTuples() );
     *
     * assertContains( tuple2, otherSet.getTuples() ); }
     *
     * public void testRemoveTuple() { ReteTuple tuple1 = new ReteTuple() {
     * public TupleKey getKey() { return new TupleKey( new FactHandleImpl( 1 ) ); } };
     *
     * ReteTuple tuple2 = new ReteTuple() { public TupleKey getKey() { return
     * new TupleKey( new FactHandleImpl( 2 ) ); } };
     *
     * Set initSet = new HashSet();
     *
     * initSet.add( tuple1 ); initSet.add( tuple2 );
     *
     * assertLength( 2, initSet );
     *
     * TupleSet set = new TupleSet( initSet );
     *
     * assertEquals( 2, set.size() );
     *
     * set.removeTuple( new TupleKey( new FactHandleImpl( 1 ) ) );
     *
     * assertEquals( 1, set.size() );
     *
     * assertContains( tuple2, set.getTuples() ); }
     *
     * public void testGetKeys() { final TupleKey key1 = new TupleKey( new
     * FactHandleImpl( 1 ) ); final TupleKey key2 = new TupleKey( new
     * FactHandleImpl( 2 ) );
     *
     * ReteTuple tuple1 = new ReteTuple() { public TupleKey getKey() { return
     * key1; } };
     *
     * ReteTuple tuple2 = new ReteTuple() { public TupleKey getKey() { return
     * key2; } };
     *
     * Set initSet = new HashSet();
     *
     * initSet.add( tuple1 ); initSet.add( tuple2 );
     *
     * TupleSet set = new TupleSet( initSet );
     *
     * Set keySet = set.getKeys();
     *
     * assertLength( 2, keySet );
     *
     * assertContains( key1, keySet );
     *
     * assertContains( key2, keySet ); }
     *
     * public void testGetTuple_ContainsTuple() { final TupleKey key1 = new
     * TupleKey( new FactHandleImpl( 1 ) ); final TupleKey key2 = new TupleKey(
     * new FactHandleImpl( 2 ) ); final TupleKey key3 = new TupleKey( new
     * FactHandleImpl( 3 ) );
     *
     * ReteTuple tuple1 = new ReteTuple() { public TupleKey getKey() { return
     * key1; } };
     *
     * ReteTuple tuple2 = new ReteTuple() { public TupleKey getKey() { return
     * key2; } };
     *
     * Set initSet = new HashSet();
     *
     * initSet.add( tuple1 ); initSet.add( tuple2 );
     *
     * TupleSet set = new TupleSet( initSet );
     *
     *
     * assertTrue( set.containsTuple( key1 ) );
     *
     * assertSame( tuple1, set.getTuple( key1 ) );
     *
     * assertTrue( set.containsTuple( key2 ) );
     *
     * assertSame( tuple2, set.getTuple( key2 ) );
     *
     * assertFalse( set.containsTuple( key3 ) );
     *
     * assertNull( set.getTuple( key3 ) ); }
     *
     * public void testGetTuples_ByFactHandle() { final TupleKey key1 = new
     * TupleKey(); key1.addRootFactHandle( new FactHandleImpl( 1 ) );
     * key1.addRootFactHandle( new FactHandleImpl( 2 ) );
     *
     * final TupleKey key2 = new TupleKey(); key2.addRootFactHandle( new
     * FactHandleImpl( 2 ) ); key2.addRootFactHandle( new FactHandleImpl( 3 ) );
     *
     * ReteTuple tuple1 = new ReteTuple() { public TupleKey getKey() { return
     * key1; } };
     *
     * ReteTuple tuple2 = new ReteTuple() { public TupleKey getKey() { return
     * key2; } };
     *
     * Set initSet = new HashSet();
     *
     * initSet.add( tuple1 ); initSet.add( tuple2 );
     *
     * TupleSet set = new TupleSet( initSet );
     *
     * assertLength( 1, set.getTuples( new FactHandleImpl( 1 ) ) );
     *
     * assertContains( tuple1, set.getTuples( new FactHandleImpl( 1 ) ) );
     *
     * assertLength( 1, set.getTuples( new FactHandleImpl( 3 ) ) );
     *
     * assertContains( tuple2, set.getTuples( new FactHandleImpl( 3 ) ) );
     *
     * assertLength( 2, set.getTuples( new FactHandleImpl( 2 ) ) );
     *
     * assertContains( tuple1, set.getTuples( new FactHandleImpl( 2 ) ) );
     *
     * assertContains( tuple2, set.getTuples( new FactHandleImpl( 2 ) ) ); }
     *
     * public void testRemoveTuplesByPartialKey() { final TupleKey key1 = new
     * TupleKey(); key1.addRootFactHandle( new FactHandleImpl( 1 ) );
     * key1.addRootFactHandle( new FactHandleImpl( 2 ) );
     *
     * final TupleKey key2 = new TupleKey(); key2.addRootFactHandle( new
     * FactHandleImpl( 2 ) ); key2.addRootFactHandle( new FactHandleImpl( 3 ) );
     *
     * ReteTuple tuple1 = new ReteTuple() { public TupleKey getKey() { return
     * key1; } };
     *
     * ReteTuple tuple2 = new ReteTuple() { public TupleKey getKey() { return
     * key2; } };
     *
     * Set initSet = new HashSet();
     *
     * initSet.add( tuple1 ); initSet.add( tuple2 );
     *
     * TupleSet set = new TupleSet( initSet );
     *
     * TupleKey partialKey = new TupleKey( new FactHandleImpl( 1 ) );
     *
     * set.removeTuplesByPartialKey( partialKey );
     *
     * assertLength( 1, set.getTuples() );
     *
     * assertContains( tuple2, set.getTuples() ); }
     */
}
