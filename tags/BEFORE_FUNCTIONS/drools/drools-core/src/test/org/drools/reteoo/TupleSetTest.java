package org.drools.reteoo;

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