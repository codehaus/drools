package org.drools.reteoo;

import junit.framework.TestCase;

public class JoinMemoryTest
    extends TestCase
{

    public void testNothing()
    {
    }
    
    /*
    private Declaration joinDecl1;
    private Declaration joinDecl2;

    private ReteTuple tuple1;
    private ReteTuple tuple2;
    private ReteTuple tuple3;
    private ReteTuple tuple4;

    private MockTupleSource leftInput1;
    private MockTupleSource rightInput1;

    private JoinNode   oneColumnJoinNode;
    private JoinMemory oneColumnJoinMemory;

    private MockTupleSource leftInput2;
    private MockTupleSource rightInput2;

    private JoinNode   twoColumnJoinNode;
    private JoinMemory twoColumnJoinMemory;

    public void setUp()
    {
        this.joinDecl1 = new Declaration( new MockObjectType(),
                                          "one" );

        this.joinDecl2 = new Declaration( new MockObjectType(),
                                          "two" );

        this.tuple1 = new ReteTuple();
        this.tuple2 = new ReteTuple();
        this.tuple3 = new ReteTuple();
        this.tuple4 = new ReteTuple();

        //     | cheese | toast |
        this.tuple1.putOtherColumn( this.joinDecl1,
                         "one-cheese" );

        this.tuple1.putOtherColumn( this.joinDecl2,
                         "two-toast" );

        //     | cheese | sneeze | 
        this.tuple2.putOtherColumn( this.joinDecl1,
                         "one-cheese" );

        this.tuple2.putOtherColumn( this.joinDecl2,
                         "two-sneeze" );

        //     | toast | sneeze |
        this.tuple3.putOtherColumn( this.joinDecl1,
                         "one-toast" );

        this.tuple3.putOtherColumn( this.joinDecl2,
                         "two-sneeze" );

        //     | cheese | sneeze |
        this.tuple4.putOtherColumn( this.joinDecl1,
                         "one-cheese" );

        this.tuple4.putOtherColumn( this.joinDecl2,
                         "two-sneeze" );

        // ----------------------------------------

        this.leftInput1  = new MockTupleSource();
        this.rightInput1 = new MockTupleSource();

        this.leftInput1.addTupleDeclaration( this.joinDecl1 );
        this.rightInput1.addTupleDeclaration( this.joinDecl1 );

        this.oneColumnJoinNode = new JoinNode( this.leftInput1,
                                                   this.rightInput1 );
        
        this.oneColumnJoinMemory = new JoinMemory( this.oneColumnJoinNode );

        // ----------------------------------------

        this.leftInput2  = new MockTupleSource();
        this.rightInput2 = new MockTupleSource();

        this.leftInput2.addTupleDeclaration( this.joinDecl1 );
        this.leftInput2.addTupleDeclaration( this.joinDecl2 );
        this.rightInput2.addTupleDeclaration( this.joinDecl1 );
        this.rightInput2.addTupleDeclaration( this.joinDecl2 );

        this.twoColumnJoinNode = new JoinNode( this.leftInput2,
                                               this.rightInput2 );
        
        this.twoColumnJoinMemory = new JoinMemory( this.twoColumnJoinNode );
        
    }

    public void tearDown()
    {
        this.joinDecl1 = null;
        this.joinDecl2 = null;
    }
    */

    /** If two Tuples having multiple common join columns contain 
     *  the same values in thoses columns, then a new joined tuple
     *  MUST be created an returned.
     */
    /*
    public void testMultiColumnAttemptJoinSingleTuple()
    {
        ReteTuple tuple = null;

        // ----------------------------------------

        tuple = this.twoColumnJoinMemory.attemptJoin( this.tuple1,
                                                      this.tuple2 );

        assertNull( tuple );

        // ----------------------------------------

        tuple = this.twoColumnJoinMemory.attemptJoin( this.tuple1,
                                                      this.tuple3 );

        assertNull( tuple );

        // ----------------------------------------

        tuple = this.twoColumnJoinMemory.attemptJoin( this.tuple1,
                                                      this.tuple4 );

        assertNull( tuple );

        // ----------------------------------------

        tuple = this.twoColumnJoinMemory.attemptJoin( this.tuple2,
                                                      this.tuple4 );

        assertNotNull( tuple );

        assertEquals( "one-cheese",
                      tuple.get( this.joinDecl1 ) );

        assertEquals( "two-sneeze",
                      tuple.get( this.joinDecl2 ) );
    }
    */

    /** If two Tuples having a common join column contain the
     *  same values in that column, then a new joined tuple
     *  MUST be created an returned.
     */
    /*
    public void testSingleColumnAttemptJoinSingleTuple()
    {
        ReteTuple tuple = null;

        // ----------------------------------------

        tuple = this.oneColumnJoinMemory.attemptJoin( this.tuple1,
                                                      this.tuple2 );

        assertNotNull( tuple );

        assertEquals( "one-cheese",
                      tuple.get( this.joinDecl1 ) );

        // ----------------------------------------

        tuple = this.oneColumnJoinMemory.attemptJoin( this.tuple1,
                                                      this.tuple3 );

        assertNull( tuple );

        // ---------------------------------------- 

        tuple = this.oneColumnJoinMemory.attemptJoin( this.tuple1,
                                                     this.tuple4 );

        assertNotNull( tuple );

        assertEquals( "one-cheese",
                      tuple.get( this.joinDecl1 ) );

        // ----------------------------------------

        tuple = this.oneColumnJoinMemory.attemptJoin( this.tuple2,
                                                      this.tuple3 );

        assertNull( tuple );

        // ---------------------------------------- 

        tuple = this.oneColumnJoinMemory.attemptJoin( this.tuple2,
                                                      this.tuple4 );
        
        assertNotNull( tuple );
        
        assertEquals( "one-cheese",
                      tuple.get( this.joinDecl1 ) );
    }
    */

    /** A Tuple added to one side of the memory MUST attempt a
     *  join against all tuples in the other side memory, returning
     *  all successfully joined tuples.
     */
    /*
    public void testAddTuples()
    {
        Set joined = null;

        joined = this.oneColumnJoinMemory.addLeftTuple( this.tuple1 );

        assertEquals( 0,
                      joined.size() );

        joined = this.oneColumnJoinMemory.addRightTuple( this.tuple2 );

        assertEquals( 1,
                      joined.size() );

        joined = this.oneColumnJoinMemory.addLeftTuple( this.tuple2 );

        assertEquals( 1,
                      joined.size() );

        assertEquals( 2,
                      this.oneColumnJoinMemory.getLeftTuples().size() );

        assertEquals( 1,
                      this.oneColumnJoinMemory.getRightTuples().size() );
    }
    */
}
